/*
 *
 */
package tournoi;

/**
 *
 */
public class OrdreMatch
{
	private int adversaire1 = 0;
	private int adversaire2 = 0;
	private int numMatch = 0;
	
	
	public OrdreMatch()
	{
		super();
	}
	
	public OrdreMatch(int adversaire1, int adversaire2, int numMatch)
	{
		this.adversaire1 = adversaire1;
		this.adversaire2 = adversaire2;
		this.numMatch = numMatch;
	}

	public int getAdversaire1()
	{
		return adversaire1;
	}

	public int getAdversaire2()
	{
		return adversaire2;
	}

	public int getNumMatch()
	{
		return numMatch;
	}

	public void setAdversaire1(int i)
	{
		adversaire1 = i;
	}

	public void setAdversaire2(int i)
	{
		adversaire2 = i;
	}

	public void setNumMatch(int i)
	{
		numMatch = i;
	}

	public boolean equals(Object obj)
	{
		OrdreMatch ordreMatch = (OrdreMatch)obj;
		if(ordreMatch.numMatch!=numMatch)
		{
			return false;
		}
		if(ordreMatch.adversaire1==adversaire1 && ordreMatch.adversaire2==adversaire2)
		{
			return true;
		}
		else if(ordreMatch.adversaire1==adversaire2 && ordreMatch.adversaire2==adversaire1)
		{
			return true;
		}
		return false;
	}
	
	public String toString()
	{		
		return "Матч "+numMatch+" "+adversaire1+" против "+adversaire2;
	}



}
