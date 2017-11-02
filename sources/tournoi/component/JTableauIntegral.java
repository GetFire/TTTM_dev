/*
 *
 */
package tournoi.component;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import tournoi.*;

/**
 * représentation graphique d'un tableau intégrale
 */
public class JTableauIntegral extends JTableau implements Observer
{
	/**
     * 
     */
    
    private static final long serialVersionUID = 1L;

    public JTableauIntegral(TableauIntegral tableau)
	{
		super(tableau);		
		initialize();
		tableau.addObserver(this);
	}

	private void initialize()
	{
		ArrayList tours = tableau.getTours();
		setLayout(new GridLayout(1, tours.size()));
		jNodesList.clear();
		for (Iterator iter =  tours.iterator(); iter.hasNext();)
		{
			TourIntegral tour = (TourIntegral) iter.next();
			JTourIntegral jTour = new JTourIntegral(tour, this);			
			add(jTour);		
		}
	}

	public void update(Observable arg0, Object arg1)
	{
		repaint();
	}

}
