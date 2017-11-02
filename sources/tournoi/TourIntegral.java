
package tournoi;

import java.util.ArrayList;
import java.util.Iterator;
import org.apache.log4j.Logger;


/**
 * représente un tour pour les tableaux de types intégrales
 */
public class TourIntegral extends Tour implements NodeListener
{	
	
	/**
	 * permet de connaitre le nombre de place qu'un joueur peut gagner (ou perdre)
	 * après ce tour
	 */ 
	private int dif_place = 0;
	protected ArrayList tourIntegralListenerList = new ArrayList();
	
    Logger logger = Logger.getLogger(TourIntegral.class);
    
	public void addTourIntegralListener(TourIntegralListener tourListener)
	{
		tourIntegralListenerList.add(tourListener);
	}
	
	public boolean deleteTourIntegralListener(TourIntegralListener tourListener)
	{
		return tourIntegralListenerList.remove(tourListener);
	}
	
	public TourIntegral(String name, int dif_place)
	{
		super(name);
		this.dif_place = dif_place;
	}
	
	public TourIntegral()
	{		
	}

	public int getDif_place()
	{
		return dif_place;
	}

	/**
	 * permet de créer les noeuds pour le tour suivant
	 * @return les noeuds "nodes" du tour suivant
	 */
	public ArrayList createNextTour()
	{		
		if(dif_place==1) return null;
		int nbMatchs = dif_place/2;
		ArrayList resultNode = new ArrayList();		
		ArrayList winners = getWinners();
		ArrayList losers = getLosers();
		Iterator iterWinners = winners.iterator();
		Iterator iterLosers = losers.iterator();
		while(iterWinners.hasNext())
		{
			for(int i=0; i<((nbMatchs/2)+1); i++)
			{
				Node node = new Node();
				Joueur w1 = (Joueur) iterWinners.next();
				Joueur w2 = (Joueur) iterWinners.next();
				node.setMatch(new Match(w1,w2,5));
				resultNode.add(node);
			}
			for(int i=0; i<((nbMatchs/2)+1); i++)
			{												
				Node node2 = new Node();
				Joueur l1 = (Joueur) iterLosers.next();
				Joueur l2 = (Joueur) iterLosers.next();
				node2.setMatch(new Match(l1,l2,5));
				resultNode.add(node2);													
			}
			
		}
		return resultNode;
	}
	
	/**
	 * ajoute un noeud au tour. Cette méthode donne aussi un nom 
	 * au match contenu dans le noeud.
	 * @param node le noeud à ajouter
	 */
	public void addNode(Node node)
	{
		//== ajout automatique du nom donné à ce match == 
		// ya surement plus simple mais j'ai pas trouvé :(
		int nbMatchs = getNodes().size();
		int mod = dif_place + 1;
		int numJoueur = (nbMatchs*2)+1;
		int place1 = 0;
		logger.debug("numJoueur="+numJoueur+" mod="+mod);
		place1 = numJoueur - ((numJoueur%mod)-1);
		int place2 = place1 + dif_place;
		node.getMatch().setName("Place "+place1+" à "+place2);
		getNodes().add(node);
		node.addNodeListener(this);
	}

	/**
	 * donne les vainqueurs des matchs de ce tours ou rempli
	 * la liste de joueurs vides si le match n'est pas terminé
	 * @return
	 */
	public ArrayList getWinners()
	{
		ArrayList list = new ArrayList();
		ArrayList matchs = getMatchs();
		for (Iterator iter = matchs.iterator(); iter.hasNext();)
		{
			Match match = (Match) iter.next();
			if(match.isTerminated())
			{
				list.add(match.getWinner());
			}
			else
			{
				//list.add(Joueur.EMPTY_JOUEUR);
				list.add(null);
			}
		}
		return list;
	}

	public ArrayList getMatchs()
	{
		ArrayList matchs = new ArrayList();
		for (Iterator iter = getNodes().iterator(); iter.hasNext();)
		{
			Node node = (Node) iter.next();
			matchs.add(node.getMatch());
		}
		return matchs;
	}

	/**
	 * donne les vainqueurs des matchs de ce tours ou rempli
	 * la liste de joueurs vides si le match n'est pas terminé
	 * @return
	 */	
	public ArrayList getLosers()
	{
		ArrayList list = new ArrayList();
		ArrayList matchs = getMatchs();
		for (Iterator iter = matchs.iterator(); iter.hasNext();)
		{
			Match match = (Match) iter.next();
			if(match.isTerminated())
			{
				if(match.getWinner().equals(match.getJoueur1()))
				{
					list.add(match.getJoueur2());
				}
				else
				{
					list.add(match.getJoueur1());
				}				
			}
			else
			{
				// list.add(Joueur.EMPTY_JOUEUR);
				list.add(null);
			}
		}
		return list;
	}

	public void nodeChanged(Node node)
	{
		for (Iterator iter = tourIntegralListenerList.iterator(); iter.hasNext();)
		{
			TourIntegralListener element = (TourIntegralListener) iter.next();
			element.tourIntegralChanged(this);
		}
		
	}

	public ArrayList getTourIntegralListenerList()
	{
		return tourIntegralListenerList;
	}

	public void setDif_place(int i)
	{
		dif_place = i;
	}

	public void setTourIntegralListenerList(ArrayList list)
	{
		tourIntegralListenerList = list;
	}
	
	public void modifierJoueur(Joueur oldJoueur, Joueur newJoueur)
	{
		for (Iterator iter = getNodes().iterator(); iter.hasNext();)
		{
			Node element = (Node) iter.next();
			element.modifierJoueur(oldJoueur,newJoueur);
		}
	}	

}
