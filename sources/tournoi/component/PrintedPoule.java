/*
 * Représente une feuille de poule destinée à l'impression
 */
package tournoi.component;

import tournoi.Poule;

/**
 * Représente une feuille de poule destinée à l'impression
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
