package tournoi;

import java.util.*;

import org.apache.log4j.Logger;


/**
 *  Gestion d'une poule dans un tableau.
 */
public class Poule extends Observable implements MatchListener
{

	private static Logger logger = Logger.getLogger(Poule.class);

	private static final int MATCH_AVERAGE = 0;
	private static final int SET_AVERAGE = 1;
	private static final int POINT_AVERAGE = 2;

	private boolean changed = false;
	
	private ArrayList joueursParPosition = new ArrayList();

	/**  Description of the Field */
	protected ArrayList joueurs;
	/**  Description of the Field */
	protected ArrayList matchs;
	/**  Description of the Field */
	protected Joueur joueurX;
	/**  Description of the Field */
	protected String name = "";

	public String getName()
	{
		return name;
	}

	public ArrayList getJoueurs()
	{
		return joueurs;
	}

	public boolean savePosition(ArrayList joueurs)
	{
		for (Iterator iter = matchs.iterator(); iter.hasNext();)
		{
			Match match = (Match) iter.next();
			int index1 = joueurs.indexOf(match.getJoueur1());
			int index2 = joueurs.indexOf(match.getJoueur2());
			if (index1 == -1 || index2 == -2)
				return false;
			if (index1 < index2)
			{
				match.setWinner((Joueur) joueurs.get(index1));
			}
			else
			{
				match.setWinner((Joueur) joueurs.get(index2));
			}
		}
		return true;
	}

	public int getFirstDrawIndex(int beginIndex, ArrayList performances, int average)
	{
		if (beginIndex >= (performances.size() - 1))
			return -1;
		double count = -1;
		for (int x = beginIndex; x < performances.size(); x++)
		{
			Performance perf = (Performance) performances.get(x);
			if (count == getCountAverage(perf, average))
			{
				return x - 1;
			}
			count = getCountAverage(perf, average);
		}
		return -1;
	}
	
	public void modifierJoueur(Joueur oldJoueur, Joueur newJoueur)
	{
		int index = joueurs.indexOf(oldJoueur);
		if(index!=-1)
		{
			joueurs.set(index,newJoueur);
		}
		index = joueursParPosition.indexOf(oldJoueur);
		if(index!=-1)
		{
			joueursParPosition.set(index,newJoueur);
		}
		for (Iterator iter = matchs.iterator(); iter.hasNext();)
		{
			Match element = (Match) iter.next();
			element.modifierJoueur(oldJoueur, newJoueur);
		}	
		setChanged();
		notifyObservers();

			
	}
	public ArrayList getDrawJoueurs(int beginIndex, ArrayList performances, int average)
	{
		double count = getCountAverage((Performance) performances.get(beginIndex), average);
		ArrayList joueurs = new ArrayList();
		for (int x = beginIndex; x < performances.size(); x++)
		{
			Performance perf = (Performance) performances.get(x);
			if (count == getCountAverage(perf, average))
			{
				joueurs.add(perf.getJoueur());
			}
		}
		return joueurs;
	}

	/**
	 * donne les performances des joueurs pass�e en param�tre.
	 * @return null si la poule n'est pas termin�e
	 */
	private ArrayList getPerformances(ArrayList joueurs)
	{
		if (!isTerminated())
		{
			return null;
		}
		ArrayList performances = new ArrayList();
		for (Iterator iter = joueurs.iterator(); iter.hasNext();)
		{
			Joueur element = (Joueur) iter.next();
			performances.add(new Performance(element));
		}
		for (Iterator iterator = matchs.iterator(); iterator.hasNext();)
		{
			Match match = (Match) iterator.next();
			if (joueurs.contains(match.getJoueur1()) && joueurs.contains(match.getJoueur2()))
			{
				setPerformances(performances, match);
			}
		}
		return performances;
	}

	private Performance getPerfJoueur(ArrayList performances, Joueur joueur)
	{
		for (Iterator iter = performances.iterator(); iter.hasNext();)
		{
			Performance perf = (Performance) iter.next();
			if (perf.getJoueur().equals(joueur))
			{
				return perf;
			}
		}
		return null;
	}

	private void setPerformances(ArrayList performances, Match match)
	{
		Joueur winner = match.getWinner();
		Joueur loser = match.getLoser();
		Performance winnerPerf = getPerfJoueur(performances, winner);
		Performance loserPerf = getPerfJoueur(performances, loser);
		int nbMancheWinner = match.nbManchesWin(winner);
		int nbPointWinner = match.nbPointsWin(winner);
		int nbMancheLoser = match.nbManchesWin(loser);
		int nbPointLoser = match.nbPointsWin(loser);
		winnerPerf.incrementNbMatchWin(1);
		winnerPerf.incrementNbSetWin(nbMancheWinner);
		winnerPerf.incrementNbPointWin(nbPointWinner);
		winnerPerf.incrementNbSetLose(nbMancheLoser);
		winnerPerf.incrementNbPointLose(nbPointLoser);
		loserPerf.incrementNbMatchLose(1);
		loserPerf.incrementNbSetWin(nbMancheLoser);
		loserPerf.incrementNbPointWin(nbPointLoser);
		loserPerf.incrementNbSetLose(nbMancheWinner);
		loserPerf.incrementNbPointLose(nbPointWinner);
	}

	private double getCountAverage(Performance perf, int average)
	{
		if (average == MATCH_AVERAGE)
		{
			return perf.getMatchAverage();
		}
		if (average == SET_AVERAGE)
		{
			return perf.getSetAverage();
		}
		if (average == POINT_AVERAGE)
		{
			return perf.getPointAverage();
		}
		return -1;
	}

	private double getFisrtCountAverage(ArrayList perfListe, int average)
	{
		if (average == MATCH_AVERAGE)
		{			
			return ((Performance) perfListe.get(0)).getMatchAverage();
		}
		if (average == SET_AVERAGE)
		{			
			return ((Performance) perfListe.get(0)).getSetAverage();
		}
		if (average == POINT_AVERAGE)
		{			
			return ((Performance) perfListe.get(0)).getPointAverage();
		}
		return -1;
	}

	private boolean isSameAverage(double count, Performance perf, int average)
	{
		if (average == MATCH_AVERAGE && (perf.getMatchAverage() == count))
			return true;
		if (average == SET_AVERAGE && (perf.getSetAverage() == count))
			return true;
		if (average == POINT_AVERAGE && (perf.getPointAverage() == count))
			return true;
		return false;
	}

	/**
	 * trie une liste de joueurs selon leurs performances
	 * @param joueurs les joueurs � trier
	 * @param performances leurs performances
	 */
	private void trie(ArrayList joueurs, ArrayList performances)
	{
		joueurs.clear();
		for (int x = 0; x < performances.size(); x++)
		{
			Performance perf = (Performance) (performances.get(x));
			joueurs.add(perf.getJoueur());
		}
	}

	/**
	 * permet de d�partager les joueurs dans une poule
	 * @param joueurs les joueurs � d�partager
	 * @param average la profondeur du d�partage
	 */
	private void departageJoueurs(ArrayList joueurs, int average)
	{
		if (joueurs == null || joueurs.size() <= 1)
			return;
		// 2 joueurs � d�partager ??
		if (joueurs.size() == 2)
		{
			if (!getWinner((Joueur) joueurs.get(0), (Joueur) joueurs.get(1)))
			{
				// oui : on retrie la liste des perf.
				Joueur j1 = (Joueur) joueurs.get(0);
				Joueur j2 = (Joueur) joueurs.get(1);
				joueurs.set(0, j2);
				joueurs.set(1, j1);
			}
			return;
		}
		// + de 2 joueurs �a se complique ;)
		ArrayList perfListe = getPerformances(joueurs);
		Collections.sort(perfListe, Collections.reverseOrder());
		trie(joueurs, perfListe);
		//=== les joueurs sont 'ils d�ja d�partag�s ? ===
		if (getFirstDrawIndex(0, perfListe, average) == -1)
		{
			//== ok, c'�tait facile... STOP ;)
			return;
		}
		//=== tous les joueurs sont � �galit�s ?? ====
		double count = getFisrtCountAverage(perfListe, average);
		boolean draw = true;
		for (Iterator iter = perfListe.iterator(); iter.hasNext();)
		{
			Performance perf = (Performance) iter.next();
			if (!isSameAverage(count, perf, average))
			{
				draw = false;
				break;
			}
		}
		// si tous les joueurs sont � �galit�s, on d�partage � une profondeur sup�rieur
		if (draw)
		{
			if (average != POINT_AVERAGE)
			{
				departageJoueurs(joueurs, average + 1);
			}
			return;
		}
		// sinon, on d�partage seulement les �galit�es

		int index = 0;
		int firstDrawIndex = 0;
		// tant qu'il y a des �galit�es
		while ((firstDrawIndex = getFirstDrawIndex(index, perfListe, average)) != -1)
		{
			ArrayList newJoueursList = getDrawJoueurs(firstDrawIndex, perfListe, average);
			departageJoueurs(newJoueursList, MATCH_AVERAGE);
			replaceJoueurs(joueurs, newJoueursList);
			index += newJoueursList.size();
		}
	}

	/**
	 * change l'ordre des joueurs pour une partie de la liste.
	 * @param firstList la liste � modifier
	 * @param lastList les joueurs tri�s � modifier
	 */
	private void replaceJoueurs(ArrayList firstList, ArrayList lastList)
	{
		for (int x = 0; x < firstList.size(); x++)
		{
			Joueur j = (Joueur) firstList.get(x);
			if (lastList.contains(j))
			{
				for (int y = 0; y < lastList.size(); y++)
				{
					firstList.set(x + y, lastList.get(y));
				}
				break;
			}

		}
	}
	
	public void replaceJoueur(Joueur newJoueur, Joueur oldJoueur)
	{
		int index = getJoueurs().indexOf(oldJoueur);
		if(newJoueur==null)
		{
			getJoueurs().remove(index);
		} 
		else if(oldJoueur==null)
		{
			getJoueurs().add(newJoueur);
		}
		else 
		{
			getJoueurs().set(index,newJoueur);
		}
		calculMatch();
	}

	/**
	 * return vrai si le gagnant est le joueur 1
	 * @param j1
	 * @param j2
	 * @return
	 */
	private boolean getWinner(Joueur j1, Joueur j2)
	{
		for (Iterator iter = matchs.iterator(); iter.hasNext();)
		{
			Match match = (Match) iter.next();
			if (j1.equals(match.getWinner()) && j2.equals(match.getLoser()))
			{
				return true;
			}
			if (j1.equals(match.getLoser()) && j2.equals(match.getWinner()))
			{
				return false;
			}
		}
		throw new RuntimeException("Erreur dans getWinner");
	}

	/**
	 * donne la liste des joueurs class�s selon leurs r�sultats
	 * dans cette poule.
	 * @return null si la poule n'est pas termin�e.
	 */
	public synchronized ArrayList getJoueursParPosition()
	{
		if (!isTerminated())
			return null;
		if(changed)
		{
			joueursParPosition = (ArrayList) joueurs.clone();
			departageJoueurs(joueursParPosition, MATCH_AVERAGE);
			changed=false;			
		}
		return joueursParPosition;
	}

	/**
	 *  Gets the matchs attribute of the Poule object
	 *
	 * @return    The matchs value
	 */
	public ArrayList getMatchs()
	{
		return matchs;
	}

	public Poule()
	{
		this("", new ArrayList());
	}
	
	/**
	 *  Constructor for the Poule object
	 *
	 * @param  joueurs    Description of the Parameter
	 * @param  pouleName  Description of the Parameter
	 */
	public Poule(String pouleName, ArrayList joueurs)
	{
		this.name = pouleName;
		this.joueurs = joueurs;
		trieListJoueurs();
		joueurX = new Joueur("Virtual", "Player", true, new Categorie(Categorie.SENIOR), null);
		calculMatch();
	}

	public boolean isTerminated()
	{
		for (Iterator iter = matchs.iterator(); iter.hasNext();)
		{
			if (!((Match) iter.next()).isTerminated())
				return false;
		}
		return true;
	}

	/**
	 *  Constructor for the Poule object
	 *
	 * @param  pouleName  Description of the Parameter
	 */
	public Poule(String pouleName)
	{
		this.name = pouleName;
		joueurs = new ArrayList();
		joueurX = new Joueur("Virtual", "Player", true, new Categorie(Categorie.SENIOR), null);
	}

	/**  Description of the Method */
	public void trieListJoueurs()
	{
		Collections.sort(joueurs, Collections.reverseOrder());
	}

	/**
	 *  Adds a feature to the Joueur attribute of the Poule object
	 *
	 * @param  joueur  The feature to be added to the Joueur attribute
	 */
	public void addJoueur(Joueur joueur)
	{
		joueurs.add(joueur);
		trieListJoueurs();
		calculMatch();		
	}

	public void removeJoueur(Joueur joueur)
	{
		joueurs.remove(joueur);
		calculMatch();
	}

	public void addJoueurAtEnd(Joueur joueur)
	{
		joueurs.add(joueur);
		calculMatch();
		setChanged(true);
		setChanged();
		notifyObservers();
	}

	/**  Description of the Method */
	private void calculMatch()
	{
		if (matchs == null)
		{
			matchs = new ArrayList();
		}
		matchs.clear();
		boolean addPlayer = false;
		if ((joueurs.size() % 2) > 0)
		{
			joueurs.add(joueurX);
			addPlayer = true;
		}
		int nbJoueurs = joueurs.size();
		int j1 = 1;
		int j2 = nbJoueurs;
		int tab[][] = new int[2][nbJoueurs / 2];
		// creation du premier tableau pour l'ordre des matchs
		for (int x = 0; x < nbJoueurs / 2; x++)
		{
			tab[0][x] = j1;
			tab[1][x] = j2;
			j1++;
			j2--;
		}
		// ajout des joueurs
		addMatch(tab, nbJoueurs / 2);
		for (int x = 0; x < (nbJoueurs - 2); x++)
		{
			tab = getPivotArray(tab, nbJoueurs / 2);
			addMatch(tab, nbJoueurs / 2);
		}
		if (addPlayer)
		{
			while (removeVirtualMatch())
			{
				;
			}
			joueurs.remove(joueurX);
		}
	}

	/**
	 *  Adds a feature to the Match attribute of the Poule object
	 *
	 * @param  tab        The feature to be added to the Match attribute
	 * @param  nbElement  The feature to be added to the Match attribute
	 */
	private void addMatch(int[][] tab, int nbElement)
	{
		for (int x = 0; x < nbElement; x++)
		{
			Joueur j1 = (Joueur) joueurs.get(tab[0][x] - 1);
			Joueur j2 = (Joueur) joueurs.get(tab[1][x] - 1);
			Match match = new Match(j1, j2, 5);
			matchs.add(match);
			match.addMatchListener(this);
		}
	}

	/**
	 *  Description of the Method
	 *
	 * @return    Description of the Return Value
	 */
	private boolean removeVirtualMatch()
	{
		Joueur virtual = (Joueur) joueurs.get(joueurs.size() - 1);
		Joueur j1;
		Joueur j2;
		for (int x = 0; x < matchs.size(); x++)
		{
			j1 = ((Match) (matchs.get(x))).getJoueur1();
			j2 = ((Match) (matchs.get(x))).getJoueur2();
			if (j1.equals(virtual) || j2.equals(virtual))
			{
				matchs.remove(x);
				return true;
			}
		}
		return false;
	}

	/**
	 *  Gets the pivotArray attribute of the Poule object
	 *
	 * @param  tab        Description of the Parameter
	 * @param  nbElement  Description of the Parameter
	 * @return            The pivotArray value
	 */
	private int[][] getPivotArray(int[][] tab, int nbElement)
	{
		int tableau[][] = new int[2][nbElement];
		for (int x = 0; x < nbElement; x++)
		{
			// cas de la premi�re ligne
			if (x == 0)
			{
				tableau[0][x] = tab[0][0];
				tableau[1][x] = tab[1][1];
			}
			else
			{
				//== gestion de la premiere colonne ==
				if ((x - 1) == 0)
				{
					tableau[0][x] = tab[1][x - 1];
				}
				else
				{
					tableau[0][x] = tab[0][x - 1];
				}
				//== gestion de la deuxi�me colonne ==
				// si derni�re ligne
				if (x == (nbElement - 1))
				{
					tableau[1][x] = tab[0][x];
				}
				// ligne interm�diaire
				else
				{
					tableau[1][x] = tab[1][x + 1];
				}
			}
		}
		return tableau;
	}

	/**
	 *  Description of the Method
	 *
	 * @return    Description of the Return Value
	 */
	public String toString()
	{
		Joueur j;
		Match m;
		String str = "___________  СПИСОК ИГРОКОВ ______________\n";
		for (int x = 0; x < joueurs.size(); x++)
		{
			j = ((Joueur) joueurs.get(x));
			str = str + (x + 1) + "\t" + j + "\n";
		}
		str += "___________  СПИСОК МАТЧА ______________\n";
		for (int x = 0; x < matchs.size(); x++)
		{
			m = ((Match) matchs.get(x));
			str = str + (joueurs.indexOf(m.getJoueur1()) + 1) + "\t" + (joueurs.indexOf(m.getJoueur2()) + 1) + "\n";
		}

		return str;
	}

	public void joueurChanged(Match match)
	{
		changed=true;
	}

	public void mancheChanged(Match match)
	{
		changed=true;		
	}

	public boolean isChanged()
	{
		return changed;
	}

	public Joueur getJoueurX()
	{
		return joueurX;
	}

	public void setChanged(boolean b)
	{
		changed = b;
	}

	public void setJoueurs(ArrayList list)
	{
		joueurs = list;
	}

	public void setJoueursParPosition(ArrayList list)
	{
		joueursParPosition = list;
	}

	public void setJoueurX(Joueur joueur)
	{
		joueurX = joueur;
	}

	public void setMatchs(ArrayList list)
	{
		matchs = list;
	}

	public void setName(String string)
	{
		name = string;
	}

}
