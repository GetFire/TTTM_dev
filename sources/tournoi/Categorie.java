package tournoi;

import java.util.*;

/**
 *  Catégorie d'un joueur de tennis de table.
 */
public class Categorie implements Comparable
{
	/**  Description of the Field */
	public final static String SENIOR = "Sénior";
	/**  Description of the Field */
	public final static String JUNIOR = "Junior";
	/**  Description of the Field */
	public final static String VETERANT = "Vétéran";
	/**  Description of the Field */
	public final static String BENJAMIN = "Benjamin";
	/**  Description of the Field */
	public final static String MINIME = "Minime";
	/**  Description of the Field */
	public final static String CADET = "Cadet";

	/**  Description of the Field */
	public final static String tCategorie[] = {VETERANT, SENIOR, JUNIOR, CADET, MINIME, BENJAMIN};
	private static Vector listCategorie = new Vector();

	private String categorie;


	public Categorie()
	{
	}
	
	/**
	 *  Retourne la liste des catégories sous forme de Vector.
	 *
	 * @return    The catedorieList value
	 */
	public static Vector getCategorieList()
	{
		if (listCategorie.isEmpty())
		{
			for (int x = 0; x < tCategorie.length; x++)
			{
				listCategorie.add(tCategorie[x]);
			}
		}
		return listCategorie;
	}

	/**
	 *  Constructor for the Categorie object
	 *
	 * @param  categorie  Catégorie d'âge d'un licencié
	 */
	public Categorie(String categorie)
	{
		this.categorie = categorie;
	}

	/**
	 *  Gets the categorie attribute of the Categorie object
	 *
	 * @return    The categorie value
	 */
	public String getCategorie()
	{
		return categorie;
	}

	/**
	 *  Description of the Method
	 *
	 * @param  o  Description of the Parameter
	 * @return    Description of the Return Value
	 */
	public int compareTo(Object o)
	{
		return categorie.compareTo(((Categorie)o).getCategorie());
	}

	public void setCategorie(String string)
	{
		categorie = string;
	}

	public String toString()
	{
		return categorie;
	}

}

