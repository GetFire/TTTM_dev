/*
 *
 */
package tournoi;

import java.util.ArrayList;

/**
 *
 */
public interface JoueursModifier
{
	public ArrayList getJoueurs();
	public ArrayList getCompetitionJoueurs();
	public boolean changeJoueur(Joueur oldJoueur, Joueur newJoueur);
}
