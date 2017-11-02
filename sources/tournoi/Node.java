/*
 * Created on 25 févr. 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package tournoi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;


/**
 * Correspond à un noeud dans un tournoi simple.
 */
public class Node extends Observable implements MatchListener
{
	private Node winNode;
	private Node lostNode;
	private Node child1;
	private Node child2;
	private Match match;	
	private String name;
	private ArrayList nodeListenersList = new ArrayList();
	private JoueursModifier joueursModifier = null;			
	private boolean nodeIsTerminated = false;
		
	public Node()
	{	
	}
	
	/**
	 * @return
	 */
	public Node getLostNode()
	{
		return lostNode;
	}

	/**
	 * @return
	 */
	public Match getMatch()
	{
		return match;
	}

	/**
	 * @return
	 */
	public Node getWinNode()
	{
		return winNode;
	}

	/**
	 * @param node
	 */
	public void setLostNode(Node node)
	{
		lostNode = node;
		if(node==null) return;
		if(node.getChild1()==null)
		{
			node.setChild1(this);
		}
		else if(node.getChild2()==null)
		{
			node.setChild2(this);
		}		
		if(match!=null && match.isTerminated())
		{
			// mancheChanged(match);
		}		
	}

	/**
	 * @param match
	 */
	public void setMatch(Match match)
	{
		this.match = match;
		match.addMatchListener(this);
		if(name!=null && !name.equals(""))
		{
			match.setName(name);
		}		
	}

	/**
	 * @param node
	 */
	public void setWinNode(Node node)
	{
		winNode = node;
		if(node==null) return;
		if(node.getChild1()==null)
		{
			node.setChild1(this);
		}
		else if(node.getChild2()==null)
		{
			node.setChild2(this);
		}		
		if(match!=null && match.isTerminated())
		{
			//mancheChanged(match);
		}
	}

	/**
	 * @return
	 */
	public Node getChild1()
	{
		return child1;
	}

	/**
	 * @return
	 */
	public Node getChild2()
	{
		return child2;
	}

	/**
	 * @param node
	 */
	private void setChild1(Node node)
	{
		child1 = node;
	}

	/**
	 * @param node
	 */
	private void setChild2(Node node)
	{
		child2 = node;
	}
	
	public void addNodeListener(NodeListener nodeListener)
	{
		nodeListenersList.add(nodeListener);
	}

	public boolean deleteNodeListener(NodeListener nodeListener)
	{
		return nodeListenersList.remove(nodeListener);
	}



	private void notifyChangedEvent()
	{
		for (Iterator iter = nodeListenersList.iterator(); iter.hasNext();)
		{
			NodeListener element = (NodeListener) iter.next();
			element.nodeChanged(this);		
		}
	}

	public void joueurChanged(Match match)
	{
		if(child1!=null && child2!=null)
		{
			if(child1.getMatch()!=null && child2.getMatch()!=null)
			{
				if(child2.getMatch().isTerminated() && child1.getMatch().isTerminated())
				{
					if(match.getJoueur1()==null)
					{
						match.setJoueur1(Joueur.EMPTY_JOUEUR);										
					}
					if(match.getJoueur2()==null)
					{
						match.setJoueur2(Joueur.EMPTY_JOUEUR);										
					}					
					setWinnerNode(winNode);
					setLoserNode(lostNode);			
				}
			}
		}
		notifyChangedEvent();
		setChanged();				
		notifyObservers();		
	}
	private void setWinnerNode(Node winNode)
	{
		if(winNode==null) return;
		if(winNode.getMatch()==null)
		{ 
			winNode.setMatch(new Match(null,null,5));		
		}
		//== joueur1 joueur2 ==
		Joueur winner = getMatch().getWinner();
		if(this.equals(winNode.getChild1()))
		{
			winNode.getMatch().setJoueur1(winner);
		}
		else if(this.equals(winNode.getChild2()))
		{
			winNode.getMatch().setJoueur2(winner);
		}
	}
	
	private void setLoserNode(Node loseNode)
	{		
		if(loseNode==null) return;
		if(loseNode.getMatch()==null)
		{ 
			loseNode.setMatch(new Match(null,null,5));			
		}
		//== joueur1 joueur2 ==
		Joueur loser = null;
		if(match.getWinner().equals(match.getJoueur1()))
		{
			loser=match.getJoueur2();
		}
		else
		{
			loser=match.getJoueur1();
		}
		if(this.equals(loseNode.getChild1()))
		{
			loseNode.getMatch().setJoueur1(loser);
		}
		else if(this.equals(loseNode.getChild2()))
		{
			loseNode.getMatch().setJoueur2(loser);
		}
	}
	
	public void modifierJoueur(Joueur oldJoueur, Joueur newJoueur)
	{
		if(match!=null)
		{
			match.modifierJoueur(oldJoueur, newJoueur);
		}
		if(winNode!=null)
		{
			winNode.modifierJoueur(oldJoueur, newJoueur);
		}
		if(lostNode!=null)
		{
			lostNode.modifierJoueur(oldJoueur, newJoueur);
		}						
	}	
	
	public void mancheChanged(Match match)
	{	
		if(match.isTerminated())
		{
			nodeIsTerminated = true;
			setWinnerNode(winNode);
			setLoserNode(lostNode);			
			notifyChangedEvent();
			setChanged();				
			notifyObservers();				
		}
		else if(nodeIsTerminated)
		{
			nodeIsTerminated=false;
			setWinnerNode(winNode);
			setLoserNode(lostNode);			
			
			notifyChangedEvent();
			setChanged();				
			notifyObservers();							
		}
		
	}

	public String getName()
	{
		return name;
	}

	public void setName(String string)
	{
		name = string;
	}

	public boolean isNodeIsTerminated()
	{
		return nodeIsTerminated;
	}

	public ArrayList getNodeListenersList()
	{
		return nodeListenersList;
	}

	public void setNodeIsTerminated(boolean b)
	{
		nodeIsTerminated = b;
	}

	public void setNodeListenersList(ArrayList list)
	{
		nodeListenersList = list;
	}

	public JoueursModifier getJoueursModifier()
	{
		return joueursModifier;
	}

	public void setJoueursModifier(JoueursModifier modifier)
	{
		joueursModifier = modifier;
	}

}
