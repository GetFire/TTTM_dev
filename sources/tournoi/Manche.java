package tournoi;

import java.io.Serializable;

/**
 *  Représentation d'une manche dans un match
 */
public class Manche implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Score score1;
	private Score score2;

	public Manche()
	{
	}

	public void modifierJoueur(Joueur oldJoueur, Joueur newJoueur)
	{
		if(score1!=null) 
		{
			score1.modifierJoueur(oldJoueur,newJoueur);
		}
		if(score2!=null)
		{
			score2.modifierJoueur(oldJoueur,newJoueur);	 
		}		
	}

	/**
	 *  Sets the score attribute of the Manche object
	 *
	 * @param  score1  The new score value
	 * @param  score2  The new score value
	 */
	public void setScores(Score score1, Score score2)
	{
		this.score1 = score1;
		this.score2 = score2;
	}

	public Score getScore1()
	{
		return score1;
	}
	
	public Score getScore2()
	{
		return score2;
	}
	
	/**
	 *  Gets the terminated attribute of the Manche object
	 *
	 * @return    The terminated value
	 */
	public boolean isTerminated()
	{
		return (score1 != null && score2 != null) ? true : false;
	}

	public Joueur getWinner()
	{
		if(isTerminated())
		{
			if(score1.getScore()>score2.getScore())
			{
				return score1.getJoueur();
			}
			else if(score2.getScore()>score1.getScore())
			{
				return score2.getJoueur();
			}
		}
		return null;
	}
	public void setScore1(Score score)
	{
		score1 = score;
	}

	public void setScore2(Score score)
	{
		score2 = score;
	}

}

