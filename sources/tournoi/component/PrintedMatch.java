/*
 *
 */
package tournoi.component;

import tournoi.Match;

/**
 * Repr�sente une feuille de match destin�e � l'impression
 */
public class PrintedMatch
{
	private Match match = null;
	private String tabName = "";
	
	public PrintedMatch(Match match, String tabName)
	{
		super();
		this.match = match;
		this.tabName = tabName;
	}
	
	

	public Match getMatch()
	{
		return match;
	}

	public String getTabName()
	{
		return tabName;
	}

	public void setMatch(Match match)
	{
		this.match = match;
	}

	public void setTabName(String string)
	{
		tabName = string;
	}

}
