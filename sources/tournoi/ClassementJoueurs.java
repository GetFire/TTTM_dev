/*
 *
 */
package tournoi;

/**
 *
 */
public class ClassementJoueurs implements Comparable
{
	private int numPoule = 0;
	private int numJoueur = 0;
	private int classement = 0;
	public int getClassement()
	{
		return classement;
	}

	public int getNumJoueur()
	{
		return numJoueur;
	}

	public int getNumPoule()
	{
		return numPoule;
	}

	public void setClassement(int i)
	{
		classement = i;
	}

	public void setNumJoueur(int i)
	{
		numJoueur = i;
	}

	public void setNumPoule(int i)
	{
		numPoule = i;
	}

	public int compareTo(Object arg0)
	{
		ClassementJoueurs clt = (ClassementJoueurs)arg0;
		return (new Integer(getClassement())).compareTo(new Integer(clt.getClassement()));
	}

    public String toString()
    {
        if(this==null)
        {
            return "null";
        }
        return "P"+getNumPoule()+" J"+getNumJoueur()+" C"+getClassement();
    }
	public boolean equals(Object obj)
	{
		ClassementJoueurs clt = (ClassementJoueurs)obj;
		if(clt.getNumJoueur()==getNumJoueur()&&clt.getNumPoule()==getNumPoule())
		{
			return true;
		}
		else
		{
			return false;
		}
	}


}
