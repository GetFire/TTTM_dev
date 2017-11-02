/*
 * Created on 2 mars 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package tournoi;

/**
 * permet de connaitre les performances d'un joueur dans un tournoi.
 */
public class Performance implements Comparable
{
	private int nbMatchWin = 0;
	private int nbSetWin = 0;
	private int nbPointWin = 0;
	
	private int nbMatchLose = 0;
	private int nbSetLose = 0;
	private int nbPointLose = 0;
	
	private Joueur joueur;
	
	public Performance(Joueur joueur)
	{
		this.joueur = joueur;
	}
	
	public void incrementNbMatchWin(int nbMatchs)
	{
		nbMatchWin += nbMatchs;
	}

	public void incrementNbSetWin(int nbSets)
	{
		nbSetWin += nbSets;
	}
	
	public void incrementNbPointWin(int nbPoints)
	{
		nbPointWin += nbPoints;
	}

	public void incrementNbMatchLose(int nbMatchs)
	{
		nbMatchLose += nbMatchs;
	}

	public void incrementNbSetLose(int nbSets)
	{
		nbSetLose += nbSets;
	}
	
	public void incrementNbPointLose(int nbPoints)
	{
		nbPointLose += nbPoints;
	}

	
	public Joueur getJoueur()
	{
		return joueur;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object arg0)
	{
		Performance perf = (Performance)arg0;
		if(getMatchAverage()>perf.getMatchAverage())
		{
			return 1;
		}
		if(getMatchAverage()<perf.getMatchAverage())
		{
			return -1;
		}
		if(getSetAverage()>perf.getSetAverage())
		{
			return 1;
		}
		if(getSetAverage()<perf.getSetAverage())
		{
			return -1;
		}
		if(getPointAverage()>perf.getPointAverage())
		{
			return 1;
		}
		if(getPointAverage()<perf.getPointAverage())
		{
			return -1;
		}		
		return 0;
	}
	
	
	public double getMatchAverage()
	{
		// évite la division par 0
		if(nbMatchLose==0)
		{
			return (double)(nbMatchWin+0.1);
		}
		return ((double)nbMatchWin/(double)nbMatchLose);
	}


	public double getPointAverage()
	{
		// évite la division par 0
		if(nbPointLose==0)
		{
			return (double)(nbPointWin+0.1);
		}
		return ((double)nbPointWin)/((double)nbPointLose);
	}

	public double getSetAverage()
	{
		// évite la division par 0
		if(nbSetLose==0)
		{
			return (double)(nbSetWin+0.1);
		}		
		return ((double)nbSetWin)/((double)nbSetLose);
	}
}
