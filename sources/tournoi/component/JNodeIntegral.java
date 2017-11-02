/*
 *
 */
package tournoi.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;

import tournoi.Competition;
import tournoi.Joueur;
import tournoi.Node;

/**
 * destiné à afficher les match pour les tableaux de type intégrales.
 */
public class JNodeIntegral extends JNode
{

	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected Dimension defaultSize = new Dimension(125,40);
	private static final Font fontTitre = new Font("Helvetica",Font.BOLD,11);
	
	public JNodeIntegral(Node node, JNode child1, JNode child2)
	{
		super(node, null, null);
		setSize(defaultSize);
	}
	
	
	protected void paintComponent(Graphics g)
	{				
		if(isOpaque())
		{
			int line = getFont().getSize();
			g.setColor(getBackground());
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(new Color(190,198,220));
			g.fillRect(1, 1, getWidth()-1, getFont().getSize());
		}
		if(model!=null)
		{
			if(model.getMatch()!=null)
			{
				int line = getFont().getSize()+1;
				g.setColor(new Color(190,198,220));
				g.fillRect(1, 1, getWidth()-1, line+1);
				
				Graphics2D g2d = (Graphics2D) g.create();												
				Joueur joueur1 = model.getMatch().getJoueur1();
				Joueur joueur2 = model.getMatch().getJoueur2();
				g2d.setColor(getForeground());
				
				Font fontTemp = getFont();
				g2d.setFont(fontTitre);
				if(model.getMatch().getName()!=null)
				{
					g2d.drawString(model.getMatch().getName(),1,line);
				}
				else
				{
					g2d.drawString("place ??",1,line);
				}
				
				g2d.setFont(fontTemp);
				line += 2;
				g2d.drawLine(0,line,getWidth(),line);

				int debut = line;
				int fin = (int)getSize().getHeight();
				
				int decalage=0;
				g2d.setColor(getForeground());				
				if(model.getMatch().getNumTable()!=0)
				{
					decalage += 25;
					g2d.drawString("T"+getMatch().getNumTable(),1,getHeight()/2+8);
					setBackground(BACKGROUND_STARTED);
					g2d.drawLine(decalage,line,decalage,getHeight());
				}
				
				// line += (1+getFont().getSize());
				
				line = debut+((fin-debut)/4)+(getFont().getSize()/2);
				
				if(joueur1!=null)
				{
					g2d.drawString(getStringJoueur(joueur1),decalage+1,line);
				}
				line = debut+((fin-debut)/2);
				debut=line+2;				
				g2d.drawLine(decalage,line,getWidth(),line);
				
				line = debut+((fin-debut)/4)+(getFont().getSize()/2);
				if(joueur2!=null)
				{
					g2d.drawString(getStringJoueur(joueur2),decalage+1,line);
				}									
				g2d.dispose();
			}
				
		}			
	}	

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent mouseEvent)
	{	
		int onmask = MouseEvent.ALT_DOWN_MASK | MouseEvent.CTRL_DOWN_MASK;
		//int offmask = MouseEvent.CTRL_DOWN_MASK;
		if ((mouseEvent.getModifiersEx() & (onmask)) == onmask) 
		{
			Point position = mouseEvent.getPoint();
			if(position.y<getHeight()/2+5)
			{
				getMatch().setWinner(getMatch().getJoueur1());
			}
			else
			{
				getMatch().setWinner(getMatch().getJoueur2());
			}
			revalidate();
			repaint();		
			return;	
		}
		onmask = MouseEvent.ALT_DOWN_MASK;
		if ((mouseEvent.getModifiersEx() & (onmask)) == onmask) 
		{
			if(!getMatch().isTerminated())
			{
				if(getBackground().equals(BACKGROUND_STARTED))
				{
					setBackground(BACKGROUND_DEFAUT);
					if(getMatch().getNumTable()!=0)
					{						
						Competition.freeTable(new Integer(getMatch().getNumTable()));
						getMatch().setNumTable(0);
					}
					
				}
				else
				{
					setBackground(BACKGROUND_STARTED);
					Integer numTable = Competition.getFreeTable();
					if(numTable!=null)
					{
						getMatch().setNumTable(numTable.intValue());
					}
					
				}				
				revalidate();
				repaint();
				return;
			}
		}										
		if(mouseEvent.getButton()==MouseEvent.BUTTON1)
		{
			selected = !selected;		
			if(selected)
			{
				setForeground(FOREGROUND_SELECTED);
			}
			else
			{
				setForeground(FOREGROUND_DEFAUT);
			}
			revalidate();
			repaint();
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent arg0)
	{	
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent mouseEvent)
	{				
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent mouseEvent)
	{				
		int onmask = MouseEvent.BUTTON1_DOWN_MASK | MouseEvent.ALT_DOWN_MASK;
		if ((mouseEvent.getModifiersEx() & (onmask)) == onmask) 
		{
			if(!getMatch().isTerminated())
			{
				if(getBackground().equals(BACKGROUND_STARTED))
				{
					setBackground(BACKGROUND_DEFAUT);
					if(getMatch().getNumTable()!=0)
					{						
						Competition.freeTable(new Integer(getMatch().getNumTable()));
						getMatch().setNumTable(0);
					}
					
				}
				else
				{
					setBackground(BACKGROUND_STARTED);
					Integer numTable = Competition.getFreeTable();
					if(numTable!=null)
					{
						getMatch().setNumTable(numTable.intValue());
					}
					
				}				
				revalidate();
				repaint();
				return;
			}
		}										
		onmask = MouseEvent.BUTTON1_DOWN_MASK;
		if ((mouseEvent.getModifiersEx() & onmask) == onmask) 
		{
			selected = !selected;		
			if(selected)
			{
				setForeground(FOREGROUND_SELECTED);
			}
			else
			{
					setForeground(FOREGROUND_DEFAUT);					
			}
			revalidate();
			repaint();
		}		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent arg0)
	{	
	}


	public Dimension getDefaultSize()
	{
		return defaultSize;
	}

	public void setDefaultSize(Dimension dimension)
	{
		defaultSize = dimension;
	}

}
