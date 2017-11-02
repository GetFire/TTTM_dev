package tournoi;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * modélisation d'un tableau de type intégral.
 * L'ordre des match doit être indiqué pour le premier tour.
 * Le traitement des tours suivants est automatique.
 */
public class TableauIntegral extends TableauSimple implements TourIntegralListener
{
	public static String TYPE = "TABLEAU_INTEGRAL";
	
	private int getDiff(int size)
	{
		return size-1;
	}
	
	public TableauIntegral(Competition competition)
	{
		super(competition);
	}

	public TableauIntegral()
	{
	}

	
	/**
	 * Créer le premier tour d'un tableau de type intégral
	 * @return true en cas de succès
	 */
	public boolean creerPremierTour()
	{
		tours.clear();
		if(max_joueurs!=0 && joueurs.size()>max_joueurs) return false;
		ArrayList matchs = getMatchsPremierTour();	
        if(max_joueurs==0)
        {
            max_joueurs=matchs.size()*2;
        }
		TourIntegral tour = new TourIntegral("Tour n°1",getDiff(max_joueurs));
		for (Iterator iter = matchs.iterator(); iter.hasNext();)
		{
			Match match1 = (Match) iter.next();			
			Node node1 = new Node();
			node1.setLostNode(null);
			node1.setWinNode(null);					
			node1.setMatch(match1);
			Match match2 = (Match) iter.next();			
			Node node2 = new Node();
			node2.setLostNode(null);
			node2.setWinNode(null);						
			node2.setMatch(match2);
			node1.setJoueursModifier(this);
			node2.setJoueursModifier(this);
			tour.addNode(node1);
			tour.addNode(node2);
		}		
		tours.add(tour);
		tour.addTourIntegralListener(this);
		return true;
	}

	 
	 /**
	  * Créer le tour suivant d'un tableau de type intégral
	  * @return true si le tour peut être créer 
	  */
	public boolean genererTourSuivant()
	{
		if(tours.size()-1<0) return false;
		TourIntegral tourPrecedent = (TourIntegral)tours.get(tours.size()-1);		
		ArrayList nodes = tourPrecedent.createNextTour();
		if(nodes==null)
		{
			return false;
		}		
		TourIntegral tour = new TourIntegral("Tour n°"+tours.size()+1, tourPrecedent.getDif_place()/2);
		for (Iterator iter = nodes.iterator(); iter.hasNext();)
		{
			Node node = (Node) iter.next();
			node.setJoueursModifier(this);
			tour.addNode(node);			
		}		
		tours.add(tour);
		tour.addTourIntegralListener(this);
		return true;
	}
	
	 
	/**
	 * génère tous les tour d'un tableau de type integrale.
	 * le premier tour doit être créer avant. 
	 */
	public boolean genererAllTours()
	{
		while(genererTourSuivant());
		return true;
	}
	/* (non-Javadoc)
	 * @see tournoi.TableauSimple#getMatchs(int)
	 */
	public ArrayList getMatchs(int numeroTour)
	{
		return ((TourIntegral)tours.get(numeroTour)).getMatchs();
	}

	public ArrayList getResultats()
	{
		ArrayList resultats = new ArrayList();
		TourIntegral lastTour = (TourIntegral)tours.get(tours.size()-1);
		for (Iterator iter = lastTour.getNodes().iterator(); iter.hasNext();)
		{
			Node node = (Node) iter.next();
			Joueur j1 = node.getMatch().getWinner();
			Joueur j2 = node.getMatch().getLoser();
			if(!Joueur.EMPTY_JOUEUR.equals(j1))
			{
				resultats.add(j1);
			}
			if(!Joueur.EMPTY_JOUEUR.equals(j2))
			{
				resultats.add(j2);
			}
		}
		return resultats;
	}
	
	public String getType()
	{
		return TYPE;
	}
	
	/**
	 * met à jour le tour correspondant à l'index
	 */
	private boolean updateTour(int index)
	{
		if(index>=tours.size()) return false;
		TourIntegral tour = (TourIntegral)tours.get(index);
		TourIntegral tourPrecedent = (TourIntegral)tours.get(index-1);		
		ArrayList nodes = tourPrecedent.createNextTour();
		if(nodes==null)
		{
			return false;
		}		
		//tour.getNodes().clear();
		for (int i=0; i<nodes.size(); i++)
		{
			
			Node newNode = (Node)nodes.get(i);
			Node lastNode = (Node)tour.getNodes().get(i);			
			//on ne remet à jour les matchs que si les joueurs ont changés
			if(joueursChanged(lastNode, newNode))
			{
				lastNode.deleteNodeListener(tour);
				String name = lastNode.getMatch().getName();			
				lastNode.setMatch(newNode.getMatch());
				lastNode.getMatch().setName(name);
				lastNode.addNodeListener(tour);
			}			
		}		
		return true;		
	}

	private boolean joueursChanged(Node lastNode, Node newNode)
	{
		Joueur lastJ1 = lastNode.getMatch().getJoueur1();
		Joueur lastJ2 = lastNode.getMatch().getJoueur2();
		Joueur newJ1 = newNode.getMatch().getJoueur1();
		Joueur newJ2 = newNode.getMatch().getJoueur2();
		if(lastJ1==null && newJ1!=null) return true;
		if(lastJ2==null && newJ2!=null) return true;
		if(lastJ1==null || lastJ2==null) return false;
		if(!lastJ1.equals(newJ1)) return true;
		if(!lastJ2.equals(newJ2)) return true;
		return false;
	}

	public void tourIntegralChanged(TourIntegral tour)
	{
		tour.deleteTourIntegralListener(this);
		// on recupere l'index du tour modifié
		int index = tours.indexOf(tour);
		// on met à jour le tour suivant si il existe
		updateTour(index+1);
		// on previent l'interface graphique des modifications
		setChanged();
		notifyObservers();
		tour.addTourIntegralListener(this);
	}
	
	public ArrayList getCompetitionJoueurs()
	{		
		return competition.getJoueurs();
	}		 
}
