
package tournoi;

import java.util.*;

/**
 * un tour représente une liste de matchs dans un tableau
 */
public class Tour
{
	private ArrayList nodes = new ArrayList();
	private String name = "";
	
	public ArrayList getNodes()
	{
		return nodes;
	}
	
	public Tour(String name)
	{
		this.name = name;
	}
	
	public Tour()
	{
		this("");
	}
		 	
	/**
	 * @param list
	 */
	public void addNode(Node node)
	{
		node.setName(name);
		nodes.add(node);		
	}

	/**
	 * @return
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param string
	 */
	public void setName(String string)
	{
		name = string;
	}

	public void setNodes(ArrayList list)
	{
		nodes = list;
	}
	
	public void modifierJoueur(Joueur oldJoueur, Joueur newJoueur)
	{
		int indexNewJoueur = -1;
		int indexOldJoueur = -1;
		for (int i=0; i<getNodes().size(); i++)
		{
			Node element = (Node) getNodes().get(i);
			if(element.getMatch()!=null)
			{
				if(element.getMatch().getJoueur1()!=null&&element.getMatch().getJoueur1()==newJoueur)
				{
					indexNewJoueur=i;
				}
				if(element.getMatch().getJoueur2()!=null&&element.getMatch().getJoueur2()==newJoueur)
				{
					indexNewJoueur=i;
				}
				if(element.getMatch().getJoueur1()!=null&&element.getMatch().getJoueur1()==oldJoueur)
				{
					indexOldJoueur=i;
				}
				if(element.getMatch().getJoueur2()!=null&&element.getMatch().getJoueur2()==oldJoueur)
				{
					indexOldJoueur=i;
				}				
			}						
		}
		//== on permutte les deux joueurs ==
		if(indexNewJoueur!=-1&&indexOldJoueur!=-1)
		{
			Node element = (Node) getNodes().get(indexOldJoueur);
			element.modifierJoueur(oldJoueur, newJoueur);
			element = (Node) getNodes().get(indexNewJoueur);
			element.modifierJoueur(newJoueur,oldJoueur);
		}
		//== sinon on remplace l'ancien joueur==
		else if(indexOldJoueur!=-1)
		{		
			Node element = (Node) getNodes().get(indexOldJoueur);
			element.modifierJoueur(oldJoueur, newJoueur);			
		}
	}
}
