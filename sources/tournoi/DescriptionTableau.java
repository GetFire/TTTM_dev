/*
 *
 */
package tournoi;

import java.util.ArrayList;

/**
 * permet de stocker les descriptions des différents tableau.
 */
public class DescriptionTableau
{
	private String identifiant;
	private String type;
	private int maxJoueurs;
	private int minJoueurs;
	private int nbTables;
	private int nbPoules = 0;
	private int nbJoueursParPoule = 0;
	private ArrayList ordreMatchList = null;
	private ArrayList sortiePouleList = null;
	private ArrayList classementsJoueurs = null;
	private boolean visible = true;
	/** calcul de l'ordre des matchs automatiques ou entrée manuellement */
	private boolean ordreMatchAuto = false;
	
	
	/**
	 * @return Returns the visible.
	 */
	public boolean isVisible() {
		return visible;
	}
	/**
	 * @param visible The visible to set.
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	public void addOrdreMatch(Integer numJoueur1, Integer numJoueur2)
	{
		if(ordreMatchList==null)
		{
			ordreMatchList = new ArrayList();
		}
		ordreMatchList.add(numJoueur1);
		ordreMatchList.add(numJoueur2);
	}
	
	public void addClassementJoueurs(ClassementJoueurs classement)
	{
		if(classementsJoueurs==null)
		{
			classementsJoueurs = new ArrayList();
		}
		classementsJoueurs.add(classement);
	}
	
	public ArrayList getClassementJoueurList()
	{
		return classementsJoueurs;
	}
	
	public void addSortiePoule(SortiePoule sortiePoule)
	{
		sortiePouleList.add(sortiePoule);
	}
	
	public DescriptionTableau()
	{
		// ordreMatchList = new ArrayList();
		sortiePouleList = new ArrayList();
		// classementsJoueurs = new ArrayList();
	}
	
	
	public String getIdentifiant()
	{
		return identifiant;
	}

	public int getMaxJoueurs()
	{
		return maxJoueurs;
	}

	public int getMinJoueurs()
	{
		return minJoueurs;
	}

	public int getNbTables()
	{
		return nbTables;
	}

	public ArrayList getOrdreMatchList()
	{
		return ordreMatchList;
	}

	public ArrayList getSortiePouleList()
	{
		return sortiePouleList;
	}

	public String getType()
	{
		return type;
	}

	public void setIdentifiant(String string)
	{
		identifiant = string;
	}

	public void setMaxJoueurs(int i)
	{
		maxJoueurs = i;
	}

	public void setMinJoueurs(int i)
	{
		minJoueurs = i;
	}

	public void setNbTables(int i)
	{
		nbTables = i;
	}

	public void setOrdreMatchList(ArrayList list)
	{
		ordreMatchList = list;
	}

	public void setSortiePouleList(ArrayList list)
	{
		sortiePouleList = list;
	}

	public void setType(String string)
	{
		type = string;
	}

	public int getNbPoules()
	{
		return nbPoules;
	}

	public void setNbPoules(int i)
	{
		nbPoules = i;
	}
	
	public String toString()
	{
		return identifiant;
	}

	public int getNbJoueursParPoule()
	{
		return nbJoueursParPoule;
	}

	public void setNbJoueursParPoule(int i)
	{
		nbJoueursParPoule = i;
	}

	/**
	 * @return Returns the classementsJoueurs.
	 */
	public ArrayList getClassementsJoueurs() {
		return classementsJoueurs;
	}
	/**
	 * @param classementsJoueurs The classementsJoueurs to set.
	 */
	public void setClassementsJoueurs(ArrayList classementsJoueurs) {
		this.classementsJoueurs = classementsJoueurs;
	}
	public boolean isOrdreMatchAuto()
	{
		return ordreMatchAuto;
	}

	public void setOrdreMatchAuto(boolean b)
	{
		ordreMatchAuto = b;
	}

}
