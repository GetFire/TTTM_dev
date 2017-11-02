package tournoi;

/**
 *  Gestion du score d'un match.
 */
public class Score
{
	private Joueur joueur;
	private int score;


	public Score()
	{
	}

	public void modifierJoueur(Joueur oldJoueur, Joueur newJoueur)
	{
		if (joueur != null && joueur.equals(oldJoueur))
		{
			joueur = newJoueur;
		}		
	}
	
	/**
	 *  Constructor for the Score object
	 *
	 * @param  joueur  joueur ayant réalisé le score
	 * @param  score   nombre de points marqué par le joueur dans cette manche
	 */
	public Score(Joueur joueur, int score)
	{
		this.joueur = joueur;
		this.score = score;
	}

	/**
	 *  Gets the joueur attribute of the Score object
	 *
	 * @return    The joueur value
	 */
	public Joueur getJoueur()
	{
		return joueur;
	}

	/**
	 *  Sets the score attribute of the Score object
	 *
	 * @param  score  The new score value
	 */
	public void setScore(int score)
	{
		this.score = score;
	}
	
	/**
	 *  Sets the score attribute of the Score object
	 *
	 * @param  score  The new score value
	 */
	public int getScore()
	{
		return score;
	}	

	public void setJoueur(Joueur joueur)
	{
		this.joueur = joueur;
	}

}

