
package tournoi.exception;

/**
 * gestion des erreurs lors 
 */
public class TournoiException extends Exception
{
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static TournoiException MAX_SIZE_EXCEPTION = new TournoiException("Слишком много игроков для этого турнира");
	
	public TournoiException(String message)
	{
		super(message);	
	}
}
