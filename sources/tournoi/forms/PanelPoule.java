package tournoi.forms;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterJob;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import tournoi.*;
import tournoi.component.JoueursCellRenderer;
import tournoi.component.PrintedPoule;
import tournoi.component.PrintingPoule;

/**
 * La fen�tre qui permet d'afficher une poule.
 */
public class PanelPoule extends JPanel
{
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static Logger logger = Logger.getLogger(PanelPoule.class);
	private Poule poule;
	private JLabel lblTabPrinc = new JLabel("");
	private JLabel lblPouleName = new JLabel("");
	private JLabel lblListeMatch = new JLabel("Список матчей");
	private JLabel lblSet = new JLabel("Сет : ");
	private JLabel lblUn = new JLabel("1");
	private JLabel lblDeux = new JLabel("2");
	private JLabel lblTrois = new JLabel("3");
	private JLabel lblQuatre = new JLabel("4");
	private JLabel lblCinq = new JLabel("5");
	private JButton bSave = new JButton("Сохранить");
	private JButton bUp = new JButton();
	private JButton bDown = new JButton();
	private JButton bValider = new JButton("Утвердить");
	private JButton bImprimer = new JButton("Печать");
	private JButton bSupprimer = new JButton("Удалить");
	private JButton bRight = new JButton();
	private JButton bLeft = new JButton();
	
	private JList listeJoueurs = null;
	
	// private JLabel[5] lblTabNumSet;
	private JTextField[] tfTabJoueur1 = {
		new JTextField(2),
		new JTextField(2),
		new JTextField(2),
		new JTextField(2),
		new JTextField(2)
		};
	private JTextField[] tfTabJoueur2 = {
		new JTextField(2),
		new JTextField(2),
		new JTextField(2),
		new JTextField(2),
		new JTextField(2)
		};
	private JList listeMatchs = new JList();

	private TableauPoules tableau = null;

	private void initialize()
	{		
		//=== ajout des icones aux boutons ===
		URL location;
		location = java.lang.ClassLoader.getSystemResource("fleche_haut.gif");
		bUp.setIcon(new ImageIcon(location));
		location = java.lang.ClassLoader.getSystemResource("fleche_bas.gif");
		bDown.setIcon(new ImageIcon(location));

		location = java.lang.ClassLoader.getSystemResource("fleche_gauche.gif");
		bLeft.setIcon(new ImageIcon(location));
		location = java.lang.ClassLoader.getSystemResource("fleche_droite.gif");
		bRight.setIcon(new ImageIcon(location));

		bUp.setToolTipText("Установить игрока более высокого уровня в пуле.");
		bDown.setToolTipText("Ранг игрока нижнего уровня в пуле.");
		bLeft.setToolTipText("Меняет выбранного игрока на одного из предыдущего пула.");
		bRight.setToolTipText("Меняет выбранного игрока в следующем пуле.");
		bSupprimer.setToolTipText("Удалить игрока, выбранного в пуле.");
        bImprimer.setToolTipText("Распечатайте текущий пул.");
        bSave.setToolTipText("Сохраните результаты выбранного совпадения.");
        bValider.setToolTipText("Подтвердить и сохранить окончательный рейтинг игроков в пуле.");
        
		setBounds(10, 10, 500, 300);
		listeJoueurs = new JList();
		DefaultListModel joueursModel = new DefaultListModel();
		listeJoueurs.setModel(joueursModel);		
		listeJoueurs.setCellRenderer(new JoueursCellRenderer());		
		ArrayList joueurs = poule.isTerminated()?poule.getJoueursParPosition():poule.getJoueurs();
		for (Iterator iter = joueurs.iterator(); iter.hasNext();)
		{
			joueursModel.addElement(iter.next());			
		}
		
		
	GridBagConstraints constraints = new GridBagConstraints();
	JPanel panel = new JPanel(new BorderLayout());
		panel.setLayout(new GridBagLayout());
	int ligne = 0;
		for (int i = 0; i < tfTabJoueur1.length; i++)
		{
			tfTabJoueur1[i].setHorizontalAlignment(JTextField.CENTER);
			tfTabJoueur2[i].setHorizontalAlignment(JTextField.CENTER);
		}
		bUp.addActionListener(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					upJoueur(e);
				}
			});

		bLeft.addActionListener(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					leftJoueur(e);
				}
			});

		bRight.addActionListener(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					rightJoueur(e);
				}
			});


		bValider.addActionListener(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					validerPosition(e);
				}
			});

		bImprimer.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						imprimerPoule(e);
					}
				});

		bSupprimer.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						supprimerJoueur(e);
					}
				});

		
		bDown.addActionListener(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					downJoueur(e);
				}
			});
		
		//================= 1ere ligne =========================
		constraints.gridx = 0;
		constraints.gridy = ligne;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 0.5;
		constraints.weighty = 0.1;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.insets = new Insets(2, 2, 2, 2);
		panel.add(lblTabPrinc, constraints);

		constraints.gridx = 2;
		constraints.gridwidth = 5;
		constraints.weightx = 0.0;
		constraints.anchor = GridBagConstraints.NORTHEAST;
		panel.add(lblPouleName, constraints);

		//================= 2eme ligne =========================
		ligne++;
		constraints.gridx = 0;
		constraints.gridy = ligne;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.1;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		panel.add(lblListeMatch, constraints);

		constraints.gridx = 1;
		constraints.weightx = 0.0;
		panel.add(lblSet, constraints);

		constraints.gridx = 2;
		panel.add(lblUn, constraints);

		constraints.gridx = 3;
		panel.add(lblDeux, constraints);

		constraints.gridx = 4;
		panel.add(lblTrois, constraints);

		constraints.gridx = 5;
		panel.add(lblQuatre, constraints);

		constraints.gridx = 6;
		panel.add(lblCinq, constraints);

		//================= 3eme ligne =========================
		ligne++;
		constraints.gridx = 0;
		constraints.gridy = ligne;
		constraints.gridwidth = 1;
		constraints.gridheight = 7;
		constraints.weightx = 0.5;
		constraints.weighty = 1.0;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.BOTH;
		listeMatchs.setListData(poule.getMatchs().toArray());
	JScrollPane scrollPane = new JScrollPane(listeMatchs);
		listeMatchs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listeMatchs.addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent arg0)
			{
				listMatchsSelectionChanged(arg0);

			}
		});
		panel.add(scrollPane, constraints);

		constraints.gridx = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		panel.add(new JLabel("Joueur 1 : "), constraints);

		constraints.gridx = 2;
		panel.add(tfTabJoueur1[0], constraints);		

		constraints.gridx = 3;
		panel.add(tfTabJoueur1[1], constraints);		

		constraints.gridx = 4;
		panel.add(tfTabJoueur1[2], constraints);
	

		constraints.gridx = 5;
		panel.add(tfTabJoueur1[3], constraints);
	

		constraints.gridx = 6;
		panel.add(tfTabJoueur1[4], constraints);
		

		//=================== 4�me ligne =========================
		ligne++;
		constraints.gridx = 1;
		constraints.gridy = ligne;
		panel.add(new JLabel("Joueur 2 : "), constraints);

		constraints.gridx = 2;
		panel.add(tfTabJoueur2[0], constraints);
	

		constraints.gridx = 3;
		panel.add(tfTabJoueur2[1], constraints);
		
		constraints.gridx = 4;
		panel.add(tfTabJoueur2[2], constraints);
		
		constraints.gridx = 5;
		panel.add(tfTabJoueur2[3], constraints);
		
		constraints.gridx = 6;
		panel.add(tfTabJoueur2[4], constraints);		

		//=================== 5�me ligne =========================
		ligne++;
		constraints.gridx = 1;
		constraints.gridy = ligne;
		constraints.gridwidth = 6;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.insets = new Insets(2, 2, 2, 2);
		bSave.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				bSaveCliked(arg0);

			}
		});
		panel.add(bSave, constraints);
				
		//=================== 6�me ligne =========================
		ligne++;
		constraints.gridx = 1;
		constraints.gridy = ligne;
		constraints.gridwidth = 6;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.insets = new Insets(2, 2, 2, 2);		
		panel.add(new JLabel("<html><u>Classement final : </u></html>"), constraints);
		
		//=================== 7�me ligne =========================
		ligne++;
		constraints.gridx = 1;
		constraints.gridy = ligne;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.SOUTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.insets = new Insets(2, 2, 2, 2);		
		panel.add(bUp, constraints);

		constraints.gridx = 7;
		panel.add(bLeft, constraints);

		constraints.gridx = 2;
		constraints.gridy = ligne;
		constraints.gridwidth = 5;
		constraints.gridheight = 2;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.insets = new Insets(2, 2, 2, 2);		
		panel.add(listeJoueurs, constraints);

		//=================== 8�me ligne =========================
		ligne++;
		constraints.gridx = 1;
		constraints.gridy = ligne;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.insets = new Insets(2, 2, 2, 2);		
		panel.add(bDown, constraints);

		constraints.gridx = 7;
		panel.add(bRight, constraints);

		//=================== 9�me ligne =========================
		ligne++;
		constraints.gridx = 1;
		constraints.gridy = ligne;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.fill = GridBagConstraints.NONE;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.insets = new Insets(2, 2, 2, 2);
		panel.add(bValider, constraints);
		constraints.gridwidth = 4;
		constraints.gridx = 3;				
		panel.add(bImprimer, constraints);
		constraints.gridwidth = 1;
		constraints.gridx = 7;
		panel.add(bSupprimer, constraints);
		
		//================== derni�re ligne ====================
		ligne++;
		constraints.gridx = 1;
		constraints.gridy = ligne;
		constraints.gridwidth = 6;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 1.0;
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.insets = new Insets(2, 2, 2, 2);
		panel.add(Box.createGlue(), constraints);

		//getContentPane().add(BorderLayout.CENTER, panel);
		add(BorderLayout.CENTER, panel);
		
		// on enl�ve les focus inutiles
		bUp.setFocusable(false);
		bDown.setFocusable(false);
		bRight.setFocusable(false);
		bLeft.setFocusable(false);
		bValider.setFocusable(false);
		bImprimer.setFocusable(false);
		bSupprimer.setFocusable(false);
		listeJoueurs.setFocusable(false);

	}

	public void setPoule(Poule poule)
	{
		this.poule = poule;
		initialize();
	}

	/**
	 *  Constructor for the FormPoule object
	 *
	 * @param  poule  Description of the Parameter
	 */
	public PanelPoule(Poule poule, TableauPoules tableau)
	{
		super(true);
		this.poule = poule;
		this.tableau = tableau;
		initialize();
	}

	public void clearMatch()
	{
		for (int i = 0; i < tfTabJoueur1.length; i++)
		{
			tfTabJoueur1[i].setText("");
			tfTabJoueur2[i].setText("");
		}
	}

	public void listMatchsSelectionChanged(ListSelectionEvent arg0)
	{
		//Match match = (Match)listeMatchs.getModel().getElementAt(arg0.getLastIndex());
		Match match = (Match)listeMatchs.getSelectedValue(); 			
		for(int i=0; i<tfTabJoueur1.length; i++)
		{
			Manche manche = (Manche)match.getManches().get(i);
			if(manche.isTerminated())
			{
				tfTabJoueur1[i].setText(""+manche.getScore1().getScore());
				tfTabJoueur2[i].setText(""+manche.getScore2().getScore());
			}
			else
			{
				tfTabJoueur1[i].setText("");
				tfTabJoueur2[i].setText("");
			}
		}
	}
	
	public boolean isFocusable()
	{		
		return true;
	}

	
	public void bSaveCliked(ActionEvent actionEvent)
	{		
		Match match = (Match)listeMatchs.getSelectedValue();
		if(match==null) return;
		for (int i = 0; i < tfTabJoueur1.length; i++)
		{			
			if(!tfTabJoueur1[i].getText().equals("")&&!tfTabJoueur2[i].getText().equals(""))
			{
				int score1 = Integer.parseInt(tfTabJoueur1[i].getText());
				int score2 = Integer.parseInt(tfTabJoueur2[i].getText());			
				match.setManche(i, new Score(match.getJoueur1(),score1),new Score(match.getJoueur2(),score2));
			}
			else
			{
				match.setManche(i, new Manche());
			}
		} 
		listeMatchs.revalidate();
		listeMatchs.repaint();
		((DefaultListModel)listeJoueurs.getModel()).clear();
		ArrayList joueurs = poule.isTerminated()?poule.getJoueursParPosition():poule.getJoueurs();
		for (Iterator iter = joueurs.iterator(); iter.hasNext();)
		{
			((DefaultListModel)listeJoueurs.getModel()).addElement(iter.next());			
		}
		getParent().repaint();		
	}
		
	public void upJoueur(ActionEvent e)
	{
		int index = listeJoueurs.getSelectedIndex();
		if (index>0)
		{			
			DefaultListModel model = (DefaultListModel)listeJoueurs.getModel();
			Joueur joueur = (Joueur)model.remove(index);
			model.add(index-1, joueur);						
			listeJoueurs.setSelectedIndex(index-1);			
		}		
	}

	public void downJoueur(ActionEvent e)
	{
		int index = listeJoueurs.getSelectedIndex();
		DefaultListModel model = (DefaultListModel)listeJoueurs.getModel();
		if (index>=0 && index<(model.getSize()-1))
		{						
			Joueur joueur1 = (Joueur)(model.get(index));
			Joueur joueur2 = (Joueur)(model.get(index+1));
			model.set(index, joueur2);
			model.set(index+1, joueur1);
			listeJoueurs.setSelectedIndex(index+1);			
		}				
	}

	public void leftJoueur(ActionEvent e)
	{
		DefaultListModel model = (DefaultListModel)listeJoueurs.getModel();
		int index = listeJoueurs.getSelectedIndex();
		tableau.permuteToLeft(poule,index);
		listeMatchs.revalidate();
		listeMatchs.repaint();	
		listeJoueurs.revalidate();
		listeJoueurs.repaint();	
		repaint();
		getParent().repaint();
	}

	public void rightJoueur(ActionEvent e)
	{
		DefaultListModel model = (DefaultListModel)listeJoueurs.getModel();
		int index = listeJoueurs.getSelectedIndex();
		tableau.permuteToRight(poule,index);
		listeMatchs.revalidate();
		listeMatchs.repaint();			
		listeJoueurs.revalidate();
		listeJoueurs.repaint();				
		repaint();
		getParent().repaint();	
	}

	public void supprimerJoueur(ActionEvent e)
	{
		DefaultListModel model = (DefaultListModel)listeJoueurs.getModel();
		int index = listeJoueurs.getSelectedIndex();
		if(index!=-1)
		{
			int result = JOptionPane.showConfirmDialog(
				null,
				"Supprimer le joueur de la poule ?",
				"Attention",
				JOptionPane.YES_NO_OPTION);
			if(result==JOptionPane.YES_OPTION)
			{
				tableau.removeJoueur(poule,(Joueur)model.elementAt(index));
				listeMatchs.revalidate();
				listeMatchs.repaint();			
				listeJoueurs.revalidate();
				listeJoueurs.repaint();				
				repaint();
				getParent().repaint();				
			}			
		}
	}


	public void validerPosition(ActionEvent e)
	{
		ArrayList joueurs = new ArrayList();
		DefaultListModel model = (DefaultListModel)listeJoueurs.getModel();
		for(int i=0; i<model.getSize(); i++)
		{
			joueurs.add(model.get(i));
		}
		poule.savePosition(joueurs);
		repaint();
		getParent().repaint();
	}
	
	public void imprimerPoule(ActionEvent e)
	{
		PrintingPoule printingPoule = new PrintingPoule();
		printingPoule.addPrintedPoule(new PrintedPoule(poule,""));
		PrinterJob printer = FormDesktop.printer;
		printer.setPrintable(printingPoule, FormDesktop.pageFormat);
		try
		{
			printer.print();
		}
		catch(Exception exp)
		{
			exp.printStackTrace();
			logger.error(exp);
		}
	}
	
}

