/*
 *
 */
package tournoi.component;

import java.awt.*;
import java.util.Iterator;

import javax.swing.*;
import javax.swing.border.Border;


import tournoi.Node;
import tournoi.TourIntegral;

/**
 * permet de représenter un Tour dans un tableau intégral
 */
public class JTourIntegral extends JComponent
{
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Dimension defaultSize = new Dimension(90,50);
	private Dimension preferedSize = new Dimension(90,50);		
	private static final Color BACKGROUND_DEFAUT = new Color(223,154,121);
	private static final Color BACKGROUND_TERMINATED = new Color(99,210,96);		
	private static final Color FOREGROUND_DEFAUT = new Color(0,0,0);	

	private TourIntegral tour;
		
	JTableauIntegral jTableau;
	
	private Font font = new Font("Helevetica", Font.BOLD, 15);

	public JTourIntegral(TourIntegral tour, JTableauIntegral jTableau)
	{
		this.tour = tour;
		this.jTableau = jTableau;
		setLayout(new GridLayout(tour.getNodes().size(), 0));
		int width = 0;
		int height = 0;
		for (Iterator iter =  tour.getNodes().iterator(); iter.hasNext();)
		{
			
			Node element = (Node) iter.next();	
			JNodeIntegral jNode = new JNodeIntegral(element,null,null);
			width = jNode.getWidth();
			height += jNode.getHeight();			
			add(jNode);	
			jNode.addMouseListener(jTableau);
			jTableau.jNodesList.add(jNode);
		}
		defaultSize = new Dimension(width, height);
		preferedSize = new Dimension(width, height);
	}
	
	/**
	 *  Gets the preferredSize attribute of the IconDisplayer object
	 *
	 *@return    The preferredSize value
	 */
	public Dimension getPreferredSize()
	{
		return preferedSize;
	}


	/**
	 *  Gets the minimumSize attribute of the IconDisplayer object
	 *
	 *@return    The minimumSize value
	 */
	public Dimension getMinimumSize()
	{
			return defaultSize;
	}

	public Border getBorder()
	{
		return BorderFactory.createLineBorder(getForeground());
	}
	

	public TourIntegral getTour()
	{
		return tour;
	}

	public void setTour(TourIntegral integral)
	{
		tour = integral;
	}

	protected void paintComponent(Graphics g)
	{				
		if(isOpaque())
		{
			g.setColor(BACKGROUND_TERMINATED);
			g.fillRect(0, 0, getWidth(), getHeight());
		}
		if(tour!=null)
		{
			Graphics2D g2d = (Graphics2D) g.create();
			g2d.setColor(getForeground());
			g2d.setFont(font);
			int line = 0;
			line+=font.getSize();
			g2d.drawString(tour.getName(),4,line);
			line+=2;
			g2d.drawLine(0,line,getWidth(),line);
			// ajouter la condition isSelected
			g2d.dispose();						
		}			
	}

}
