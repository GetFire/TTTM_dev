
package tournoi;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 * mod�lisation d'un tableau de type KO.
 * L'ordre des match doit �tre indiqu� pour le premier tour.
 * Le traitement des tours suivants est automatique.
 */
public class TableauKO extends TableauSimple implements NodeListener
{
	private static Logger logger = Logger.getLogger(TableauKO.class);
	
	public static String TYPE = "TABLEAU_KO";
	
	public TableauKO(Competition competition)
	{
		super(competition);
	}
	
	public TableauKO()
	{
	}
	
	public Object clone()
	{
		TableauKO tableauKO = new TableauKO(competition);		
		tableauKO.max_joueurs = max_joueurs;
		tableauKO.min_joueurs = min_joueurs;
		tableauKO.joueurs = (ArrayList)getJoueurs().clone();
		tableauKO.ordreMatchList = (ArrayList)ordreMatchList.clone();		
		tableauKO.name = name;
		tableauKO.type = type;
		tableauKO.tours = (ArrayList)tours.clone();		
		return tableauKO;
	}

	private boolean playMatch(Joueur joueur, Match match)
	{
		if(match.getWinner().equals(joueur)||match.getLoser().equals(joueur))
		{
			return true;
		}
		return false;
	}

	/**
	 * calcul la place de tout les joueurs (m�me les Joueur.EMPTY)
	 * @param joueur
	 * @param numTour
	 * @return la place du joueur
	 */
	private int calculPlace(Joueur joueur, int numTour)
	{
		if(numTour==(tours.size()-1))
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
					return calculPlace(joueur, numTour+1);
				}
				else
				{
					//getMatchs(numTour).size()
					//return (getNBJoueurs(getMatchs(numTour))/2)+calculPlace(match.getWinner(), numTour+1);
					return getMatchs(numTour).size()+calculPlace(match.getWinner(), numTour+1);
				}
			}
		}
		return 0; // ne dois jamais aller i�i
	}

	
	private int getPlace(Joueur joueur)
	{
		if(joueur!=null && !joueur.equals(Joueur.EMPTY_JOUEUR))
		{
			return calculPlace(joueur,0);
		}
		return -1;
	}
	
	public ArrayList getResultats()
	{
		ArrayList matchs = getMatchs(0);
		Joueur[] placesJoueurs = new Joueur[matchs.size()*2+1];
		ArrayList joueurs = new ArrayList();
		// classement de tous les joueurs
		for (Iterator iter = matchs.iterator(); iter.hasNext();)
		{
			Match match = (Match) iter.next();
			int place1 = getPlace(match.getJoueur1());			
			int place2 = getPlace(match.getJoueur2());					
			if(place1!=-1)
			{			
				if(match.getJoueur1()!=null && !match.getJoueur1().equals(Joueur.EMPTY_JOUEUR))
				{
					placesJoueurs[place1-1] = match.getJoueur1();
				}
				
			}
			if(place2!=-1)
			{
				if(match.getJoueur2()!=null && !match.getJoueur2().equals(Joueur.EMPTY_JOUEUR))
				{			
					placesJoueurs[place2-1] = match.getJoueur2();
				}
			}
		}		
		for(int i=0; i<placesJoueurs.length; i++)
		{
			if(placesJoueurs[i]!=null && !placesJoueurs[i].equals(Joueur.EMPTY_JOUEUR))
			{
				joueurs.add(placesJoueurs[i]);
			}			
		}
		return joueurs;
	}

	
	/* 
	 * permet de cr�er le premier tour d'un tournoi de type KO.
	 */
	public boolean creerPremierTour()
	{
		tours.clear();
		if(max_joueurs!=0 && joueurs.size()>max_joueurs) return false;
		logger.debug("перед тем gematchpremiertour()");
		ArrayList matchs = getMatchsPremierTour();		
		Tour tour = new Tour("1/"+matchs.size()+" окончательный");
		for (Iterator iter = matchs.iterator(); iter.hasNext();)
		{
			logger.debug("в матче");
			Node nodeEmpty = new Node();
			nodeEmpty.setLostNode(null);
			Match match1 = (Match) iter.next();		
			match1.setName("1/"+matchs.size()+" окончательный");
			Node node1 = new Node();
			node1.setLostNode(null);
			node1.setWinNode(nodeEmpty);					
			node1.setMatch(match1);
			Match match2 = (Match) iter.next();
			match2.setName("1/"+matchs.size()+" окончательный");
			Node node2 = new Node();
			node2.setLostNode(null);
			node2.setWinNode(nodeEmpty);						
			node2.setMatch(match2);
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
	 * g�n�ration du prochain tour du tableau.
	 * Si un match n'est pas termin�, un noeud vide est cr�er mais
	 * la m�thode return vrai
	 * @return faux si tous les tours du tabeau ont d�ja �t� g�n�r�s
	 */
	public boolean genererTourSuivant()
	{		
		if(tours.size()-1<0) return false;
		Tour tourPrecedent = (Tour)tours.get(tours.size()-1);
		ArrayList nodes = tourPrecedent.getNodes();
		if(nodes.size()<=1)
		{
			return false;
		}	
		String tourName = "";
		if(nodes.size()/2==1) tourName="окончательный";
		else tourName=("1/"+(nodes.size()/2)+" окончательный");
		Tour tour = new Tour(tourName);
		for (Iterator iter = nodes.iterator(); iter.hasNext();)
		{
			Node node1 = (Node) iter.next();
			Node node2 = (Node) iter.next();
			Node nodeEmpty = new Node();									
			nodeEmpty.setLostNode(null);
			node1.setWinNode(nodeEmpty);
			node2.setWinNode(nodeEmpty);						
			tour.addNode(nodeEmpty);		
			nodeEmpty.addNodeListener(this);
			nodeEmpty.setJoueursModifier(this);	
		}		
		tours.add(tour);
		return true;
	}

	/* (non-Javadoc)
	 * @see tournoi.Tableau#getMatchs(int)
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
