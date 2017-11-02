
package tournoi;

/**
 *
 */
public class Resultat
{
	private Joueur joueur;
	private int place;
	private String tableau;

	
	/**
	 * construit un resultat pour un joueur
	 * @param joueur le joueur ayant obtenu ce resultat
	 * @param place la place du joueur
	 * @param tableau le nom du tableau
	 */
	public Resultat(Joueur joueur, int place, String tableau)
	{
		this.joueur = joueur;
		this.place = place;
		this.tableau = tableau;
	}
	
	public Joueur getJoueur()
	{
		return joueur;
	}

	public int getPlace()
	{
		return place;
	}

	public String getTableau()
	{
		return tableau;
	}

	public void setJoueur(Joueur joueur)
	{
		this.joueur = joueur;
	}

	public void setPlace(int i)
	{
		place = i;
	}

	public void setTableau(String tableau)
	{
		this.tableau = tableau;
	}

}
