
package tournoi.component;

import tournoi.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Représentation graphique d'une poule.
 */
public class JPoule extends JComponent
{
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final Dimension DEFAUT_SIZE = new Dimension(120,50);
	private Dimension PREFERED_SIZE = new Dimension(90,50);		
	private static final Color BACKGROUND_DEFAUT = new Color(230,156,132);
	private static final Color BACKGROUND_TERMINATED = new Color(99,210,96);		
	private static final Color FOREGROUND_DEFAUT = new Color(0,0,0);
	private static final Color FOREGROUND_SAME_CLUB = new Color(185,0,14);
	
	
	private Font fontTitrePoule = new Font("Helevetica", Font.BOLD, 15);
	private Font fontJoueur = new Font("Helevetica", Font.PLAIN, 10);
	private Font fontSameClub = new Font("Helevetica", Font.BOLD, 10);
		
	private Poule poule;
	
	public JPoule(Poule poule)
	{
		super();
		this.poule = poule;
		setBackground(BACKGROUND_DEFAUT);
		setForeground(FOREGROUND_DEFAUT);
		setOpaque(true);
		PREFERED_SIZE = calculPreferedSize();
	}
	
	public Poule getPoule()
	{
		return poule;
	}
	
	private Dimension calculPreferedSize()
	{
		int size = 0;
		for (Iterator iter = poule.getJoueurs().iterator(); iter.hasNext();)
		{
			Joueur element = (Joueur) iter.next();
			int sizeJoueur = element.toString().length();
			if(sizeJoueur>size)
			{
				size=sizeJoueur;
			}
		}
		int width = (size*3/5)*fontJoueur.getSize();
		int height = (fontTitrePoule.getSize()+4+((poule.getJoueurs().size()+1)*fontJoueur.getSize()));
		return new Dimension(width, height);		
	}
	
	/**
	 *  Gets the preferredSize attribute of the IconDisplayer object
	 *
	 *@return    The preferredSize value
	 */
	public Dimension getPreferredSize()
	{
		return PREFERED_SIZE;
	}


	/**
	 *  Gets the minimumSize attribute of the IconDisplayer object
	 *
	 *@return    The minimumSize value
	 */
	public Dimension getMinimumSize()
	{
			return DEFAUT_SIZE;
	}

	public Border getBorder()
	{
		return BorderFactory.createLineBorder(getForeground());
	}
	
	protected void paintComponent(Graphics g)
	{				
		if(isOpaque())
		{
			if(poule.isTerminated())
			{
				g.setColor(BACKGROUND_TERMINATED);
			}
			else
			{
				g.setColor(BACKGROUND_DEFAUT);
			}
			
			g.fillRect(0, 0, getWidth(), getHeight());
		}
		if(poule!=null)
		{
			ArrayList joueurs;
			if(poule.isTerminated())
			{
				joueurs = poule.getJoueursParPosition();
			}
			else
			{
				joueurs = poule.getJoueurs();
			}
			Graphics2D g2d = (Graphics2D) g.create();
				g2d.setColor(getForeground());
			g2d.setFont(fontTitrePoule);
			int line = 0;
			line+=this.fontTitrePoule.getSize();
			g2d.drawString(poule.getName(),4,line);
			line+=2;
			g2d.drawLine(0,line,getWidth(),line);			
			g2d.setFont(fontJoueur);						
			for (Iterator iter = joueurs.iterator(); iter.hasNext();)
			{
				line+=11;
				Joueur joueur = (Joueur) iter.next();	
				String strJoueur = joueur.getNom();
				if(joueur.getPrenom()!=null&&joueur.getPrenom().length()>0)
				{
					strJoueur += " "+joueur.getPrenom().substring(0, 1)+".";
				}

				strJoueur+=(" ("+ joueur.getStrClassement()+")");
				if(joueur.getClub()!=null&&joueur.getClub().length()>3)
				{
					strJoueur += " "+joueur.getClub().substring(0, 3);
				}

				
				if(areSameClub(joueur,joueurs))
				{
					g2d.setColor(FOREGROUND_SAME_CLUB);		
					g2d.setFont(fontSameClub);	
				}
				else
				{
					g2d.setColor(getForeground());
					g2d.setFont(fontJoueur);
				}
				g2d.drawString(strJoueur,4,line);
			}
			g2d.dispose();			
				
		}			
	}	
	
	private boolean areSameClub(Joueur joueur, ArrayList joueurList)
	{
		int index = joueurList.indexOf(joueur);
		for(int x=0; x<joueurList.size();x++)
		{
			if(x!=index)
			{
				Joueur j2 = (Joueur)joueurList.get(x);
				if(j2.getClub()!=null && !j2.getClub().equals("") && j2.getClub().equals(joueur.getClub()))
				{
					return true;
				}
			}
		}
		return false;
	}
}
