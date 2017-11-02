package tournoi;

public class JoueurInTab
{
    private int numMatch = 0;
    private int numJoueur = 0;
    private int numDossard = 0;
    
    public JoueurInTab()
    {
    }
    
    public JoueurInTab(int numMatch, int numJoueur, int numDossard)
    {
        this.numMatch = numMatch;
        this.numJoueur = numJoueur;
        this.numDossard = numDossard;
    }
    
    public int getNumDossard()
    {
        return numDossard;
    }
    public void setNumDossard(int numDossard)
    {
        this.numDossard = numDossard;
    }
    public int getNumJoueur()
    {
        return numJoueur;
    }
    public void setNumJoueur(int numJoueur)
    {
        this.numJoueur = numJoueur;
    }
    public int getNumMatch()
    {
        return numMatch;
    }
    public void setNumMatch(int numMatch)
    {
        this.numMatch = numMatch;
    }
    
    public String toString()
    {
        return "номУчаст="+numJoueur+" номМатча="+numMatch+" номНагруд"+numDossard;
    }
}
