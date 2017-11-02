
package tournoi;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 * modélisation d'un tableau de type double KO.
 * L'ordre des match doit être indiqué pour le premier tour.
 * Le traitement des tours suivants est automatique.
 */
public class TableauDoubleKO extends TableauSimple implements NodeListener
{
	public static String TYPE = "TABLEAU_DOUBLE_KO";
	
	private static Logger logger = Logger.getLogger(TableauDoubleKO.class);
	
	public TableauDoubleKO(Competition competition)
	{
		super(competition);
	}

	public TableauDoubleKO()
	{
	}

	
	public Object clone()
	{
		TableauDoubleKO tableauDoubleKO = new TableauDoubleKO(competition);		
		tableauDoubleKO.max_joueurs = max_joueurs;
		tableauDoubleKO.min_joueurs = min_joueurs;
		tableauDoubleKO.joueurs = (ArrayList)getJoueurs().clone();
		tableauDoubleKO.ordreMatchList = (ArrayList)ordreMatchList.clone();		
		tableauDoubleKO.name = name;
		tableauDoubleKO.type = type;
		tableauDoubleKO.tours = (ArrayList)tours.clone();		
		return tableauDoubleKO;
	}
	
	/**
	 * permet d'obtenit la liste des joueurs suivant leur résultat
	 * dans le tableau.
	 * @return la liste des joueurs 
	 */	
	public ArrayList getResultats()
	{
		int tourWinner = 2;
		int tourLoser = 1;
		ArrayList matchsWinners = getMatchs(tourWinner);		
		Joueur[] placesWinners = new Joueur[getMatchs(0).size()*2+1];//new Joueur[getNBJoueurs(matchsWinners)];
		
		ArrayList matchsLosers = getMatchs(tourLoser);
		Joueur[] placesLosers = new Joueur[getMatchs(0).size()*2+1];//new Joueur[getNBJoueurs(matchsLosers)];
		
		ArrayList joueurs = new ArrayList();
		// classement des Joueurs du tableau principal
		for (Iterator iter = matchsWinners.iterator(); iter.hasNext();)
		{
			Match match = (Match) iter.next();
			int place1 = getPlace(match.getJoueur1(),tourWinner);
			int place2 = getPlace(match.getJoueur2(),tourWinner);
			if(place1!=-1)
			{
				placesWinners[place1-1] = match.getJoueur1();
			}
			if(place2!=-1)
			{			
				placesWinners[place2-1] = match.getJoueur2();
			}
		}
		// classement des Joueurs du tableau consolante
		for (Iterator iter = matchsLosers.iterator(); iter.hasNext();)
		{
			Match match = (Match) iter.next();
			int place1 = getPlace(match.getJoueur1(),tourLoser);
			int place2 = getPlace(match.getJoueur2(),tourLoser);
			if(place1!=-1)
			{
				placesLosers[place1-1] = match.getJoueur1();
			}
			if(place2!=-1)
			{			
				placesLosers[place2-1] = match.getJoueur2();
			}
		}		
		// on ajoute les joueurs du tournoi principal
		for(int i=0; i<placesWinners.length; i++)
		{
			if(placesWinners[i]!=null && !placesWinners[i].equals(Joueur.EMPTY_JOUEUR))
			{
				joueurs.add(placesWinners[i]);
			}				
		}
		// on ajoute les joueurs du tournoi consolante
		for(int i=0; i<placesLosers.length; i++)
		{
			if(placesLosers[i]!=null && !placesLosers[i].equals(Joueur.EMPTY_JOUEUR))
			{
				joueurs.add(placesLosers[i]);
			}			
		}
		return joueurs;
	}
	
	/* 
	 * permet de créer le premier tour d'un tournoi de type double KO.
	 */
	public boolean creerPremierTour()
	{
		tours.clear();
		if(max_joueurs!=0 && joueurs.size()>max_joueurs) return false;
		ArrayList matchs = getMatchsPremierTour();		
		Tour tour = new Tour("1/"+matchs.size()+" de finale");
		for (Iterator iter = matchs.iterator(); iter.hasNext();)
		{
			Node nodeEmpty = new Node();			
			Match match1 = (Match) iter.next();
			match1.setName(tour.getName());			
			Node node1 = new Node();			
			node1.setLostNode(nodeEmpty);
			node1.setWinNode(nodeEmpty);
			node1.setMatch(match1);		
			Match match2 = (Match) iter.next();			
			Node node2 = new Node();			
			node2.setLostNode(nodeEmpty);
			node2.setWinNode(nodeEmpty);
			node2.setMatch(match2);
			match2.setName(tour.getName());			
			tour.addNode(node1);
			tour.addNode(node2);	
			node1.addNodeListener(this);
			node2.addNodeListener(this);
			node1.setJoueursModifier(this);
			node2.setJoueursModifier(this);				
		}		
		tours.add(tour);
		return true;
	}

	public void clearTableaux()
	{			
		getJoueurs().clear();
	}

	/**
	 * génération des deux prochains tours du tableau.
	 * Si un match n'est pas terminé, un noeud vide est créer mais
	 * la méthode return vrai
	 * @return faux si tous les tours du tableau ont déja été générés
	 */
	public boolean genererTourSuivant()
	{		
		Tour tourPrecedentRight = (Tour)tours.get(tours.size()-1);
		Tour tourPrecedentLeft;
		// test pour savoir si le précédent tour est le premier tour
		if(tours.size()>1)
		{
			tourPrecedentLeft = (Tour)tours.get(tours.size()-2);
		}
		else
		{
			tourPrecedentLeft = tourPrecedentRight;
		}			
		ArrayList nodes = tourPrecedentRight.getNodes();
		if(nodes.size()<=1)
		{
			return false;
		}
		String place = (nodes.size()/2)==1?"finale":"1/"+(nodes.size()/2+" de finale");
		Tour tour1 = new Tour(place);
		
		//== tableau principal ==
		for (Iterator iter = nodes.iterator(); iter.hasNext();)
		{
			Node node1 = (Node) iter.next();
			Node node2 = (Node) iter.next();
			Node nodeEmpty1 = new Node();
			nodeEmpty1.setLostNode(null);															
			node1.setWinNode(nodeEmpty1);
			node2.setWinNode(nodeEmpty1);
			node1.setLostNode(null);
			node2.setLostNode(null);								
			tour1.addNode(nodeEmpty1);
			nodeEmpty1.addNodeListener(this);
			nodeEmpty1.setJoueursModifier(this);
		}
		Tour tour2 = new Tour(place+" (consolante)");
		nodes = tourPrecedentLeft.getNodes();
		//== tableau consolante ==
		if(tours.size()==1)
		{
			for (Iterator iter = nodes.iterator(); iter.hasNext();)
			{
				Node node1 = (Node) iter.next();
				Node node2 = (Node) iter.next();
				Node nodeEmpty1 = new Node();
				nodeEmpty1.setLostNode(null);														
				node1.setLostNode(nodeEmpty1);
				node2.setLostNode(nodeEmpty1);						
				tour2.addNode(nodeEmpty1);
				nodeEmpty1.addNodeListener(this);
				nodeEmpty1.setJoueursModifier(this);
			}
		}		
		else
		{
			for (Iterator iter = nodes.iterator(); iter.hasNext();)
			{
				Node node1 = (Node) iter.next();
				Node node2 = (Node) iter.next();
				Node nodeEmpty1 = new Node();
				nodeEmpty1.setLostNode(null);														
				node1.setWinNode(nodeEmpty1);
				node2.setWinNode(nodeEmpty1);																
				tour2.addNode(nodeEmpty1);
				nodeEmpty1.addNodeListener(this);
				nodeEmpty1.setJoueursModifier(this);
			}
					
		}		
		tours.add(tour2);
		tours.add(tour1);
		return true;
	}

	private boolean playMatch(Joueur joueur, Match match)
	{
		if(match.getWinner().equals(joueur)||match.getLoser().equals(joueur))
		{
			return true;
		}
		return false;
	}


	private int getPlace(Joueur joueur, int numTour)
	{
		if(joueur!=null && !joueur.equals(Joueur.EMPTY_JOUEUR))
		{
			return calculPlace(joueur,numTour);
		}
		return -1;
	}
	
	/**
	 * calcul la place de tout les joueurs (même les Joueur.EMPTY)
	 * @param joueur
	 * @param numTour
	 * @return la place du joueur
	 */
	private int calculPlace(Joueur joueur, int numTour)
	{		
		if(numTour>=(tours.size()-2))
		{
			Match match = (Match)getMatchs(numTour).get(0);
			if(joueur.equals(match.getWinner())) return 1;
			else return 2;
		}
		for (Iterator iter = getMatchs(numTour).iterator(); iter.hasNext();)
		{
			Match match = (Match) iter.next();
			// le joueur joue le match ?
			if(playMatch(joueur, match))
			{
				// il gagne le match ?
				if(match.getWinner().equals(joueur))
				{
					return calculPlace(joueur, numTour+2);
				}
				else
				{
					// return (getNBJoueurs(getMatchs(numTour))/2)+calculPlace(match.getWinner(), numTour+2);
					return getMatchs(numTour).size()+calculPlace(match.getWinner(), numTour+2);
				}
			}
		}
		return 0; // ne dois jamais aller içi
	}
		 
	 /**
	  * récupère les matchs d'un tour
	  * @param numeroTour le numéro du tour 
	  */
	public ArrayList getMatchs(int numeroTour)
	{
		ArrayList matchs = new ArrayList();
		Tour tour = (Tour)tours.get(numeroTour);
		for (Iterator iter = tour.getNodes().iterator(); iter.hasNext();)
		{
			Node node = (Node) iter.next();
			matchs.add(node.getMatch());
		}
		return matchs;
	}

	/* (non-Javadoc)
	 * @see tournoi.Tableau#genererAllTours()
	 */
	public boolean genererAllTours()
	{
		while(genererTourSuivant());
		return true;
	}

	public String getType()
	{
		return TYPE;
	}

	public void nodeChanged(Node node)
	{
		setChanged();
		notifyObservers();		
	}

	public ArrayList getCompetitionJoueurs()
	{		
		return competition.getJoueurs();
	}



}
