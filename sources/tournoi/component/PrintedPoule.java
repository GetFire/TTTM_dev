/*
 * Repr�sente une feuille de poule destin�e � l'impression
 */
package tournoi.component;

import tournoi.Poule;

/**
 * Repr�sente une feuille de poule destin�e � l'impression
 */
public class PrintedPoule
{
	private Poule poule = null;
	private String tabName = "";
	
	public PrintedPoule(Poule poule, String tabName)
	{
		super();
		this.poule = poule;
		this.tabName = tabName;
	}
	
	

	public Poule getPoule()
	{
		return poule;
	}

	public String getTabName()
	{
		return tabName;
	}

	public void setMatch(Poule poule)
	{
		this.poule = poule;
	}

	public void setTabName(String string)
	{
		tabName = string;
	}

}
