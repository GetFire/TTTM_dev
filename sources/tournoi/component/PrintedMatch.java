/*
 *
 */
package tournoi.component;

import tournoi.Match;

/**
 * Représente une feuille de match destinée à l'impression
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
