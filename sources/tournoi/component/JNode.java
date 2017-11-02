
package tournoi.component;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;
import javax.swing.border.Border;

import org.apache.log4j.Logger;


import tournoi.*;

/**
 * représentation graphique d'un noeud (match) dans un tableau simple.
 */
public class JNode extends JComponent implements MouseListener, Observer
{
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static Logger logger = Logger.getLogger(JNode.class);
	protected Node model;
	protected static final Color BACKGROUND_DEFAUT = new Color(230,156,132);
	protected static final Color BACKGROUND_TERMINATED = new Color(99,210,96);
	protected static final Color FOREGROUND_SELECTED = new Color(255,37,32);
	protected static final Color BACKGROUND_STARTED = new Color(228,194,61);		
	protected static final Color FOREGROUND_DEFAUT = new Color(0,0,0);
	protected boolean selected = false;
	protected Dimension defaultSize = new Dimension(125,35);
	
	protected JComboBox jcbJoueurs1 = new JComboBox();
	protected JComboBox jcbJoueurs2 = new JComboBox();
	
	private JNode jNodeChild1;
	private JNode jNodeChild2;
	
	private boolean isCb1Cleared = false;
	
	public boolean isSelected()
	{
		return selected;
	}
				
	private JNode(Node node)
	{
		GridLayout layout = new GridLayout(2,1);
		
		setLayout(layout);
		
		add(jcbJoueurs1);
		add(jcbJoueurs2);
		jcbJoueurs1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{			
				JoueursModifier jm =getNode().getJoueursModifier();
				if(jcbJoueurs1.getSelectedIndex()!=-1&&jcbJoueurs1.isVisible())
				{
					jm.changeJoueur(getNode().getMatch().getJoueur1(),(Joueur)jcbJoueurs1.getSelectedItem());				
					jcbJoueurs1.setVisible(false);
				}
			}
		});		
		jcbJoueurs2.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{		
				JoueursModifier jm =getNode().getJoueursModifier();
				if(jcbJoueurs2.getSelectedIndex()!=-1&&jcbJoueurs2.isVisible())
				{
					jm.changeJoueur(getNode().getMatch().getJoueur2(),(Joueur)jcbJoueurs2.getSelectedItem());				
					jcbJoueurs2.setVisible(false);				
				}
			}
		});
		jcbJoueurs2.addKeyListener(new KeyListener()
		{
			public void keyTyped(KeyEvent e)
			{
			}

			public void keyPressed(KeyEvent e)
			{
				if(e.getKeyCode()==KeyEvent.VK_ESCAPE)
				{
					if(jcbJoueurs2.isVisible())
					{
						jcbJoueurs2.setVisible(false);
					}
				}
			}

			public void keyReleased(KeyEvent e)
			{
			}
		});

		jcbJoueurs1.addKeyListener(new KeyListener()
		{
			public void keyTyped(KeyEvent e)
			{
			}

			public void keyPressed(KeyEvent e)
			{
				if(e.getKeyCode()==KeyEvent.VK_ESCAPE)
				{
					if(jcbJoueurs1.isVisible())
					{
						jcbJoueurs1.setVisible(false);
					}
				}
			}

			public void keyReleased(KeyEvent e)
			{
			}
		});		
		jcbJoueurs1.setVisible(false);	
		jcbJoueurs2.setVisible(false);
			
		model = node;		
		model.addObserver(this);		
		setBackground(BACKGROUND_DEFAUT);
		setForeground(FOREGROUND_DEFAUT);
		setOpaque(true);		
		setFont(new Font("Helvetica",Font.PLAIN,10));
		setSize(defaultSize);	
		addMouseListener(this);
		if(model.getMatch()!=null)
		{	
			model.getMatch().notifyMancheChanged();
		}
	}
	
	public JNode(Node node, JNode child1, JNode child2)
	{
		this(node);
		jNodeChild1 = child1;
		jNodeChild2 = child2;	
		//update(model, null);			
	}
	
	public Node getNode()
	{
		return model;
	}
	
	public Match getMatch()
	{
		if(model==null) return null;
		return model.getMatch();
	}
		
	/**
	 *  Gets the preferredSize attribute of the IconDisplayer object
	 *
	 *@return    The preferredSize value
	 */
	public Dimension getPreferredSize()
	{
			return defaultSize;
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
	

	protected String getStringJoueur(Joueur j)
	{
		if(j==null||j.equals(Joueur.EMPTY_JOUEUR)) return "";
		String strJoueur = "";            
		if(Competition.isAfficheDossard())
		{
			strJoueur = j.getDossard()+" "+j.getNom()+" ";
		}
		else
		{
			strJoueur = j.getNom()+" ";
		}
		if(j.getPrenom()!=null&&j.getPrenom().length()>0)
		{
			strJoueur += " "+j.getPrenom().substring(0, 1)+".";
		}

		strJoueur+=(" ("+ j.getStrOldClassement()+")");
		return strJoueur;
	}
	
	protected void paintComponent(Graphics g)
	{				
		if(isOpaque())
		{
			g.setColor(getBackground());
			g.fillRect(0, 0, getWidth(), getHeight());
		}
		if(model!=null)
		{
			if(model.getMatch()!=null)
			{
				if(!jcbJoueurs1.isVisible())
				{
					jcbJoueurs1.removeAllItems();
				}
				if(!jcbJoueurs2.isVisible())
				{
					jcbJoueurs2.removeAllItems();
				}
				
				Graphics2D g2d = (Graphics2D) g.create();												
				Joueur joueur1 = model.getMatch().getJoueur1();
				Joueur joueur2 = model.getMatch().getJoueur2();
				int decalage=0;
				g2d.setColor(getForeground());
				if(model.getMatch().getNumTable()!=0)
				{
					decalage += 25;
					g2d.drawString("T"+getMatch().getNumTable(),1,getHeight()/2+2);
					setBackground(BACKGROUND_STARTED);
				}
						
				if(joueur1!=null)
				{
					g2d.drawString(getStringJoueur(joueur1),1+decalage,getHeight()/2-2);
				}
				if(joueur2!=null)
				{
					g2d.drawString(getStringJoueur(joueur2),1+decalage,getHeight()-getHeight()/2+getFont().getSize()+2);
				}					
				g2d.drawLine(decalage,getHeight()/2,getWidth(),getHeight()/2);
				g2d.drawLine(decalage,0,decalage,getHeight());
				//jcbJoueurs.setLocation(0,0);
				g2d.dispose();
			}
				
		}			
	}

	private void mouseEvent(MouseEvent mouseEvent)
	{
		int onmask = MouseEvent.BUTTON1_DOWN_MASK | MouseEvent.ALT_DOWN_MASK | MouseEvent.CTRL_DOWN_MASK;
		//int offmask = MouseEvent.CTRL_DOWN_MASK;
		if ((mouseEvent.getModifiersEx() & (onmask)) == onmask) 
		{
			Point position = mouseEvent.getPoint();
			if(position.y<getHeight()/2)
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
		onmask = MouseEvent.BUTTON1_DOWN_MASK | MouseEvent.CTRL_DOWN_MASK;
		if ((mouseEvent.getModifiersEx() & (onmask)) == onmask)		
		{
			Point position = mouseEvent.getPoint();
			if(position.y<getHeight()/2)
			{
				if(!jcbJoueurs1.isVisible())
				{
					JoueursModifier jm = getNode().getJoueursModifier();					
					if(jcbJoueurs1.getItemCount()==0)
					{
						for (Iterator iter = jm.getCompetitionJoueurs().iterator(); iter.hasNext();)
						{
							Joueur j = (Joueur) iter.next();
							jcbJoueurs1.addItem(j);							
						}						
					}
					jcbJoueurs1.setVisible(true);					
				}
				else
				{
					jcbJoueurs1.setVisible(false);
				}
				
			}
			else
			{
				if(!jcbJoueurs2.isVisible())
				{				
					JoueursModifier jm = getNode().getJoueursModifier();
					if(jcbJoueurs2.getItemCount()==0)
					{					
						for (Iterator iter = jm.getCompetitionJoueurs().iterator(); iter.hasNext();)
						{
							Joueur j = (Joueur) iter.next();
							jcbJoueurs2.addItem(j);
						}
					}
					jcbJoueurs2.setVisible(true);	
				}								
				else
				{
					jcbJoueurs2.setVisible(false);
				}
			}
			revalidate();
			repaint();		
			return;											
		}		
		onmask = MouseEvent.ALT_DOWN_MASK;
		if ((mouseEvent.getModifiersEx() & (onmask)) == onmask) 
		{
			if(getMatch()!=null && !getMatch().isTerminated())
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
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent mouseEvent)
	{	
		// mouseEvent(mouseEvent);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent mouseEvent)
	{
		mouseEvent(mouseEvent);
		
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
			if(getMatch()!=null && !getMatch().isTerminated())
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
	/**
	 * @return
	 */
	public JNode getJNodeChild1()
	{
		return jNodeChild1;
	}

	/**
	 * @return
	 */
	public JNode getJNodeChild2()
	{
		return jNodeChild2;
	}

	/**
	 * @param node
	 */
	public void setJNodeChild1(JNode node)
	{
		jNodeChild1 = node;
	}

	/**
	 * @param node
	 */
	public void setJNodeChild2(JNode node)
	{
		jNodeChild2 = node;
	}
	
	public void update(Observable arg0, Object arg1)
	{	
		model = (Node)arg0;
		if(getNode()!=null && getNode().getMatch()!=null && getNode().getMatch().isTerminated())
		{
			setBackground(BACKGROUND_TERMINATED);	
			if(getMatch().getNumTable()!=0)
			{
				Competition.freeTable(new Integer(getMatch().getNumTable()));
				getMatch().setNumTable(0);
			}
		}
		else
		{		
			setBackground(BACKGROUND_DEFAUT);
		}
		repaint();
		revalidate();		
	}	

}
