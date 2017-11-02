/*
 *
 */
package tournoi;

/**
 *
 */
public class SortiePoule
{
	private int nbJoueurs = 0;
	private String refTableau = "";
	private String type = "";
	
	public SortiePoule()
	{
	}
	
	public int getNbJoueurs()
	{
		return nbJoueurs;
	}

	public String getRefTableau()
	{
		return refTableau;
	}

	public void setNbJoueurs(int i)
	{
		nbJoueurs = i;
	}

	public void setRefTableau(String string)
	{
		refTableau = string;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String string)
	{
		type = string;
	}

}
