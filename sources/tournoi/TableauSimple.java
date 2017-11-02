
package tournoi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;

import org.apache.log4j.Logger;

import tournoi.exception.TournoiException;


/**
 * Repr�sentation d'un tableau sans poule.
 */
public abstract class TableauSimple extends Observable implements Tableau, JoueursModifier
{
	protected String type = "";
	protected String name = "";
	protected int max_joueurs = 0; 
	protected int min_joueurs = 0;
	protected Competition competition = null; 
	//== contient tous les tours du tableau ==
	protected ArrayList tours = new ArrayList();
	//== contient tous les joueurs du tournoi ==
	public ArrayList joueurs = new ArrayList();
	//== contient l'ordre des matchs pour le premier tour du tournoi ==
	public ArrayList ordreMatchList = new ArrayList();
		
	public abstract boolean creerPremierTour();
	
	public abstract boolean genererTourSuivant();
	
	public abstract boolean genererAllTours();
	
	public abstract ArrayList getMatchs(int numeroTour);

	

	private Logger logger = Logger.getLogger(TableauSimple.class);


	public TableauSimple()
	{
	}

	public TableauSimple(Competition competition)
	{
		this.competition = competition;
	}

	/**
	 * cr�er la liste des matchs selon l'ordre du tableau qui contient plus de joueurs
	 * que le nbJoueurs pass� en param�tre
	 * @param nbJoueurs le nombre minimal de joueurs dans le tableau
	 * @return la liste des matchs dans du haut du tableau vers le bas
	 */
	public static ArrayList getOrdreMatchList(int nbJoueurs)
	{		   
			ArrayList temp = new ArrayList();
			int nb_Joueur = 0;
			for(nb_Joueur=1;nb_Joueur<nbJoueurs;nb_Joueur=nb_Joueur*2);	
			for(int x=0; x<(nb_Joueur/2); x++)
			{
				OrdreMatch ordreMatch = new OrdreMatch();
				ordreMatch.setNumMatch(x+1);
				temp.add(ordreMatch);
			}
			int n=1;
			int numCurrentMatch = 0;				
			int coef=1;
			int j=1;					
			while(!ordreMatchListIsFull(temp))		
			{
				while((numCurrentMatch+(coef*n)-coef)>=0&&(numCurrentMatch+(coef*n)-coef)<(nb_Joueur/2))
				{
					OrdreMatch currentMatch = (OrdreMatch)temp.get((numCurrentMatch+(coef*n))-coef);
					if(currentMatch.getAdversaire1()==0)
					{
						currentMatch.setAdversaire1((nb_Joueur/n)+1-j);
						currentMatch.setAdversaire2((nb_Joueur+1)-currentMatch.getAdversaire1());	
					}
					else if(currentMatch.getAdversaire2()==0)
					{
						currentMatch.setAdversaire2((nb_Joueur/n)+1-j);
						currentMatch.setAdversaire1((nb_Joueur+1)-currentMatch.getAdversaire2());					
					}				
					n=(n==0)?1:n*2;	
				}
				n=1;
				j=j+1;
				numCurrentMatch = getNumOrdreMatch(temp, j);
				coef = -coef;			
			}
			return temp;
	}
	
	public void calculOrdreMatchList()
	{
		logger.debug("В списке соответствия расписания");
		ArrayList temp = TableauSimple.getOrdreMatchList(joueurs.size());
		ordreMatchList.clear();		
		for (Iterator it = temp.iterator(); it.hasNext();)
		{
			OrdreMatch element = (OrdreMatch) it.next();
			ordreMatchList.add(new Integer(element.getAdversaire1()));
			ordreMatchList.add(new Integer(element.getAdversaire2()));
		}
	}
	
	private static int getNumOrdreMatch(ArrayList temp, int joueur)
	{
		for (Iterator iter = temp.iterator(); iter.hasNext();)
		{
			OrdreMatch element = (OrdreMatch) iter.next();
			if(element.getAdversaire1()==joueur || element.getAdversaire2()==joueur)
			{
				return temp.indexOf(element);
			}
		}
		return 0;
	}
	
	private static boolean ordreMatchListIsFull(ArrayList temp)
	{
		for (Iterator iter = temp.iterator(); iter.hasNext();)
		{
			OrdreMatch element = (OrdreMatch) iter.next();
			if(element.getAdversaire1()==0 || element.getAdversaire2()==0)
			{
				return false;
			}
		}
		return true;
		
	}
	
	public void modifierJoueur(Joueur oldJoueur, Joueur newJoueur)
	{
		int index = joueurs.indexOf(oldJoueur);
		if(index!=-1)
		{
			joueurs.set(index,newJoueur);
			setChanged();
			notifyObservers();
		}		
		for (Iterator iter = tours.iterator(); iter.hasNext();)
		{
			Tour element = (Tour) iter.next();
			element.modifierJoueur(oldJoueur, newJoueur);
		}
	}
	
	/**
	 * permet d'�changer un joueur du tableau
	 * si le nouveau joueur n'est pas pr�sent dans le tableau, l'ancien joueur est
	 * rempla�� par le nouveau.
	 * si le nouveau joueur est pr�sent dans le tableau, les deux joueurs permuttent
	 * @return vrai si le nouveau joueur ne se trouvait pas d�j� dans le tableau
	 */
	public boolean changeJoueur(Joueur oldJoueur, Joueur newJoueur)
	{
		if(getJoueurs().contains(newJoueur))
		{
			int indexOld = getJoueurs().indexOf(oldJoueur);
			int indexNew = getJoueurs().indexOf(newJoueur);
			getJoueurs().set(indexOld, newJoueur);
			getJoueurs().set(indexNew, oldJoueur);
			setChanged();
			notifyObservers();
			for (Iterator iter = tours.iterator(); iter.hasNext();)
			{
				Tour element = (Tour) iter.next();
				element.modifierJoueur(oldJoueur, newJoueur);
				
			}	
			return true;										
		}
		else
		{
			modifierJoueur(oldJoueur,newJoueur);
		}
		return true;
	}
		
	
	public ArrayList getJoueurs()
	{
		return joueurs;
	}
	
	public void addJoueur(Joueur joueur)
	{
		joueurs.add(joueur);
	}
	
	protected ArrayList getMatchsPremierTour()
	{
		ArrayList matchsList = new ArrayList();
		logger.debug("dans getMatchPrmeioerTour()");
		logger.debug(""+joueurs.size());
		if(ordreMatchList.isEmpty())
		{
			logger.debug("dans boucle");
			ArrayList list = getOrdreMatchList(joueurs.size());
			for (Iterator iter = list.iterator(); iter.hasNext();)
			{
				OrdreMatch ordre = (OrdreMatch)iter.next();		
				int indexJ1 = ordre.getAdversaire1()-1;
				int indexJ2 = ordre.getAdversaire2()-1;
				Joueur joueur1 = Joueur.EMPTY_JOUEUR;
				Joueur joueur2 = Joueur.EMPTY_JOUEUR;
				if(indexJ1<joueurs.size())
				{
					joueur1 = (Joueur)joueurs.get(indexJ1);					
				}
                joueur1.setDossard(indexJ1+1);
                logger.debug("нагрудник 1 = "+indexJ1);
				if(indexJ2<joueurs.size())
				{
					joueur2 = (Joueur)joueurs.get(indexJ2);                    
				}			
                joueur2.setDossard(indexJ2+1);
                logger.debug("нагрудник 2 = "+indexJ2);
				Match match = new Match(joueur1, joueur2, 5);
				matchsList.add(match);
			}
			
		}
		else
		{
			for (Iterator iter = ordreMatchList.iterator(); iter.hasNext();)
			{
				logger.debug("в цикле");
				int indexJ1 = ((Integer) iter.next()).intValue()-1;
				int indexJ2 = ((Integer) iter.next()).intValue()-1;
				Joueur joueur1 = Joueur.EMPTY_JOUEUR;
				Joueur joueur2 = Joueur.EMPTY_JOUEUR;
				if(indexJ1<joueurs.size())
				{
					joueur1 = (Joueur)joueurs.get(indexJ1);					
				}
                joueur1.setDossard(indexJ1+1);
				if(indexJ2<joueurs.size())
				{
					joueur2 = (Joueur)joueurs.get(indexJ2);					
				}			
                joueur2.setDossard(indexJ2+1);
				Match match = new Match(joueur1, joueur2, 5);
				matchsList.add(match);
			}
		}
		return matchsList;
	}
	
	public String toString()
	{		
		return name;
	}
	
	public void setJoueurs(ArrayList joueurs) throws TournoiException
	{		
		if(max_joueurs!=0 && joueurs.size()>max_joueurs)
		{ 
			throw TournoiException.MAX_SIZE_EXCEPTION;
		}
		this.joueurs = joueurs;
	}
	
	
	/**
	 * @return
	 */
	public int getMax_joueurs()
	{
		return max_joueurs;
	}

	/**
	 * @return
	 */
	public int getMin_joueurs()
	{
		return min_joueurs;
	}

	/**
	 * @return
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return
	 */
	public ArrayList getTours()
	{
		return tours;
	}

	/**
	 * @return
	 */
	public String getType()
	{
		return type;
	}

	/**
	 * @param i
	 */
	public void setMax_joueurs(int i)
	{
		max_joueurs = i;
	}

	/**
	 * @param i
	 */
	public void setMin_joueurs(int i)
	{
		min_joueurs = i;
	}

	/**
	 * @param string
	 */
	public void setName(String string)
	{
		name = string;
	}

	/**
	 * @param list
	 */
	public void setTours(ArrayList list)
	{
		tours = list;
	}
	
	protected int getNBJoueurs(ArrayList matchs)
	{
		int count = 0;
		for (Iterator iter = matchs.iterator(); iter.hasNext();)
		{
			Match match = (Match) iter.next();
			if(match.getJoueur1()!=null && !match.getJoueur1().equals(Joueur.EMPTY_JOUEUR))
			{
				count++;
			}
			if(match.getJoueur2()!=null && !match.getJoueur2().equals(Joueur.EMPTY_JOUEUR))
			{
				count++;
			}			
		}
		return count;
	}

	/**
	 * @param string
	 */
	public void setType(String string)
	{
		type = string;
	}
	
	public boolean isTerminated()
	{
		for (Iterator iter = tours.iterator(); iter.hasNext();)
		{
			Tour tour = (Tour) iter.next();
			for (Iterator iter2 = tour.getNodes().iterator(); iter2.hasNext();)
			{
				Node node = (Node) iter2.next();
				if(node.getMatch()!=null&&!node.getMatch().isTerminated())
				{
					return false;
				}				
			}
		}
		return true;
	}
	
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}


	public ArrayList getOrdreMatchList()
	{
		return ordreMatchList;
	}

	public void setOrdreMatchList(ArrayList list)
	{
		ordreMatchList = list;
	}

	public Competition getCompetition()
	{
		return competition;
	}

	public void setCompetition(Competition competition)
	{
		this.competition = competition;
	}

}
