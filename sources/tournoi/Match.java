package tournoi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 *  Représentation d'un match de tennis de table
 */
public class Match implements Serializable
{
	/**
     * 
     */
    private static final long serialVersionUID = 1L;

    ArrayList matchListenersList = new ArrayList();

	private Joueur joueur1;
	private Joueur joueur2;
	private ArrayList manches;
	private int manchesGagnantes = 3;
	private String name = "";
	private int numTable = 0;
	private static Logger logger = Logger.getLogger(Match.class);

	public void addMatchListener(MatchListener matchListener)
	{
		matchListenersList.add(matchListener);
		matchListener.joueurChanged(this);
		matchListener.mancheChanged(this);
	}

	public void removeAllMatchListeners()
	{
		matchListenersList.clear();
	}

	public void modifierJoueur(Joueur oldJoueur, Joueur newJoueur)
	{
		// si les deux joueurs sont dans le match, on ne fait rien
		if(joueur1!=null && joueur2!=null)
		{
			if(joueur1.equals(oldJoueur)&&joueur2.equals(newJoueur))
			{
				return;
			}
			if(joueur1.equals(newJoueur)&&joueur2.equals(oldJoueur))
			{
				return;
			}
		}
		if (joueur1 != null)
		{
			if (joueur1.equals(oldJoueur))
			{
				//setJoueur1(newJoueur);
				joueur1=newJoueur;
			}
		}
		if (joueur2 != null)
		{
			if (joueur2.equals(oldJoueur))
			{
				//setJoueur2(newJoueur);
				joueur2=newJoueur;
			}
		}
		for (Iterator iter = manches.iterator(); iter.hasNext();)
		{
			Manche element = (Manche) iter.next();
			element.modifierJoueur(oldJoueur,newJoueur);
		}
	}

	private void notifyJoueurChanged()
	{
		for (Iterator iter = matchListenersList.iterator(); iter.hasNext();)
		{
			MatchListener element = (MatchListener) iter.next();
			element.joueurChanged(this);
		}
	}

	public void notifyMancheChanged()
	{
		for (Iterator iter = matchListenersList.iterator(); iter.hasNext();)
		{
			MatchListener element = (MatchListener) iter.next();
			element.mancheChanged(this);
		}
	}

	/**
	 * Rensigne le gagnant du match en mettant des scores bidon (11-0...)
	 * @param winner le gagnant du match
	 */
	public boolean setWinner(Joueur winner)
	{
		//Random random = new Random();
		if (joueur1 == null || joueur1.equals(Joueur.EMPTY_JOUEUR))
			return false;
		if (joueur2 == null || joueur2.equals(Joueur.EMPTY_JOUEUR))
			return false;
		if (winner != null && !Joueur.EMPTY_JOUEUR.equals(winner))
		{
			if (winner.equals(joueur1))
			{
				for (int i = 0; i < manchesGagnantes; i++)
				{
					Score score1 = new Score(joueur1, 11);
					Score score2 = new Score(joueur2, 0);
					setManche(i, score1, score2);
				}
			}
			if (winner.equals(joueur2))
			{
				for (int i = 0; i < manchesGagnantes; i++)
				{
					Score score1 = new Score(joueur1, 0);
					Score score2 = new Score(joueur2, 11);
					setManche(i, score1, score2);
				}
			}
			return true;
		}
		return false;
	}

	public void setJoueur1(Joueur joueur)
	{
		this.joueur1 = joueur;
		notifyJoueurChanged();
	}

	public void setJoueur2(Joueur joueur)
	{
		this.joueur2 = joueur;
		notifyJoueurChanged();
	}

	/**
	 *  Gets the joueur1 attribute of the Match object
	 *
	 * @return    The joueur1 value
	 */
	public Joueur getJoueur1()
	{
		return joueur1;
	}

	/**
	 *  Gets the joueur2 attribute of the Match object
	 *
	 * @return    The joueur2 value
	 */
	public Joueur getJoueur2()
	{
		return joueur2;
	}

	public int nbManchesWin(Joueur joueur)
	{
		int nbManches = 0;
		for (Iterator iter = manches.iterator(); iter.hasNext();)
		{

			Manche manche = (Manche) iter.next();
			if (manche.isTerminated())
			{
				if (manche.getWinner().equals(joueur))
				{
					nbManches += 1;
				}
			}
		}
		return nbManches;
	}

	public int nbPointsWin(Joueur joueur)
	{
		int nbPoints = 0;
		for (Iterator iter = manches.iterator(); iter.hasNext();)
		{
			Manche manche = (Manche) iter.next();
			if (manche.isTerminated())
			{
				if (manche.getScore1().getJoueur().equals(joueur))
				{
					nbPoints += manche.getScore1().getScore();
				}
				if (manche.getScore2().getJoueur().equals(joueur))
				{
					nbPoints += manche.getScore2().getScore();
				}
			}
		}
		return nbPoints;

	}

	/**
	 *  Constructor for the Match object
	 *
	 * @param  joueur1     premier joueur participant au match
	 * @param  joueur2     deuxième joueur participant au match
	 * @param  nbrManches  Nombres de manches pour ce match
	 */
	public Match(Joueur joueur1, Joueur joueur2, int nbrManches)
	{
		this.joueur1 = joueur1;
		this.joueur2 = joueur2;
		manches = new ArrayList();
		for (int i = 0; i < nbrManches; i++)
		{
			manches.add(new Manche());
		}
	}

	public Match()
	{
	}

	/**
	 * controle si le match est terminé
	 * @return vrai si toutes les manches sont terminées
	 */
	public boolean isTerminated()
	{
		if (getWinner() == null)
		{
			return false;
		}
		return true;
	}

	/**
	 * un match est fini si il contient 2 joueurs et que les manches sont terminé.
	 * un match est aussi terminé si il contient un joueur égal à Joueur.getEmptyJoueur et l'autre
	 * joueur renseigné.
	 * @return le gagnant du match ou null si le match n'est pas fini
	 */
	public Joueur getWinner()
	{
		int nbManchesGagnees1 = 0;
		int nbManchesGagnees2 = 0;
		if (joueur1 == null || joueur2 == null)
			return null;
		if (joueur1 != null && joueur1.equals(Joueur.EMPTY_JOUEUR))
		{
			if (joueur2 != null && joueur2.equals(Joueur.EMPTY_JOUEUR))
				return Joueur.EMPTY_JOUEUR;
			return joueur2;
		}
		if (joueur2 != null && joueur2.equals(Joueur.EMPTY_JOUEUR))
		{
			if (joueur1 != null && joueur1.equals(Joueur.EMPTY_JOUEUR))
				return Joueur.EMPTY_JOUEUR;
			return joueur1;
		}
		for (Iterator iter = manches.iterator(); iter.hasNext();)
		{
			Manche manche = (Manche) iter.next();
			if (manche.isTerminated())
			{
				Score score1 = manche.getScore1();
				Score score2 = manche.getScore2();
				if (score1.getJoueur().equals(getJoueur1()))
				{
					if (score1.getScore() > score2.getScore())
					{
						nbManchesGagnees1 += 1;
					}
					else if (score1.getScore() < score2.getScore())
					{
						nbManchesGagnees2 += 1;
					}
				}
				else if (score1.getJoueur().equals(getJoueur2()))
				{
					if (score1.getScore() > score2.getScore())
					{
						nbManchesGagnees2 += 1;
					}
					else if (score1.getScore() < score2.getScore())
					{
						nbManchesGagnees1 += 1;
					}
				}
			}
		}
		if (nbManchesGagnees1 == manchesGagnantes)
			return getJoueur1();
		if (nbManchesGagnees2 == manchesGagnantes)
			return getJoueur2();
		return null;
	}

	public Joueur getLoser()
	{
		Joueur winner = getWinner();
		if (winner == null)
			return null;
		if (winner.equals(getJoueur1()))
			return joueur2;
		return joueur1;

	}

	public ArrayList getManches()
	{
		return manches;
	}

	/**
	 *  renseigne une manches des manches du match
	 *
	 * @param  numManche  le numero de la manche
	 * @param  score1     le premier score
	 * @param  score2     le deuxième score
	 */
	public void setManche(int numManche, Score score1, Score score2)
	{
		if ((score1.getJoueur().equals(joueur1) && score2.getJoueur().equals(joueur2))
			|| (score1.getJoueur().equals(joueur2) && score2.getJoueur().equals(joueur1)))
		{
			((Manche) (manches.get(numManche))).setScores(score1, score2);
		}
		notifyMancheChanged();
	}

	public void setManche(int index, Manche manche)
	{
		manches.set(index, manche);
		notifyMancheChanged();
	}

	/**
	 *  Description of the Method
	 *
	 * @return    Description of the Return Value
	 */
	public String toString()
	{
		String text =
			joueur1.getNom() + " " + joueur1.getPrenom() + "  contre  " + joueur2.getNom() + " " + joueur2.getPrenom();
		if (isTerminated())
		{
			return "<html><font color=\"green\">" + text + "</font></html>";
		}
		else
		{
			return text;
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

	public int getNumTable()
	{
		return numTable;
	}

	public void setNumTable(int i)
	{
		numTable = i;
		notifyJoueurChanged();
	}

	public int getManchesGagnantes()
	{
		return manchesGagnantes;
	}

	public ArrayList getMatchListenersList()
	{
		return matchListenersList;
	}

	public void setManches(ArrayList list)
	{
		manches = list;
	}

	public void setManchesGagnantes(int i)
	{
		manchesGagnantes = i;
	}

	public void setMatchListenersList(ArrayList list)
	{
		matchListenersList = list;
	}

}
