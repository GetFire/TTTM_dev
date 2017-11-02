package tournoi;

import java.util.ArrayList;

import tournoi.exception.TournoiException;
;

/**
 * décrit les méthodes obligatoires pour tout les objets Tableau.
 */
public interface Tableau
{
	public static final int CONSOLANTE = 0;
	public static final int PRINCIPAL = 1;
	
		
	/** @return le type du tableau */
	public String getType();
	
	/** @return le nom du tableau */
	public String getName();
	
	/** @param name le nouveau nom du tableau */
	public void setName(String name);
	
	/** @param joueurs les joueurs participant au tableau */
	public void setJoueurs(ArrayList joueurs) throws TournoiException;
	
	/** @return les joueurs participant au tableau */
	public ArrayList getJoueurs();
	
	/** 
	 * ajoute un joueur au tableau.
	 * @param joueur le joueur à ajouter
	 */
	public void addJoueur(Joueur joueur);
	
	/** @return le nombre maximum de joueurs supportés par le tableau */	
	public int getMax_joueurs();
	
	/** @return le nombre minimum de joueurs pour lancer le tableau */
	public int getMin_joueurs();
	
	/**
	 * modifie le nombre maximum de joueurs pour le tableau
	 * @param i nouveau nombre max.
	 */ 
	public void setMax_joueurs(int i);
	
	/**
	 * modifie le nombre minimum de joueurs pour le tableau
	 * @param i nouveau nombre minimum
	 */
	public void setMin_joueurs(int i);
	
	/**
	 * permet de savoir si le tableau est terminé ou non
	 * @return vrai si le tableau est terminé
	 */
	public boolean isTerminated();
	
	public ArrayList getResultats();
	
	/**
	 * donne une nouvelle copie du tableau
	 * @return un nouvel Object Tableau
	 * @throws CloneNotSupportedException
	 */
	public Object clone()  throws CloneNotSupportedException;
	
	public void modifierJoueur(Joueur oldJoueur, Joueur newJoueur);	
}
