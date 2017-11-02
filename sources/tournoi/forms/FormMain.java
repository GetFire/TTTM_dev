
package tournoi.forms;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.apache.log4j.Logger;
import tournoi.*;
import tournoi.component.JoueursCellRenderer;
import tournoi.component.TableauxCellRenderer;
import tournoi.exception.TournoiException;
import tournoi.xml.XMLAnalyzer;

import java.net.URL;
import java.util.*;

/**
 * la fen�tre principale.
 */
public class FormMain extends JInternalFrame implements Observer
{	
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    private JButton bCreer = new JButton("Cоздать");
	private JButton bReload = new JButton("Загрузка");
	private JButton bLancer = new JButton("Запуск");
	private JButton bSupprimer = new JButton("Удаление");
	private JButton bUpJoueur = new JButton();
	private JButton bDownJoueur = new JButton();
	private JButton bAddJoueur = new JButton();    
	private JComboBox cbTournois = new JComboBox(); 	
	private JList listeJoueurs;
	private JList listeTournoisCreer;
	private Competition competition = null;

	private JDesktopPane desktop;

	private static Logger logger = Logger.getLogger(FormMain.class);

	
	public Competition getCompetition()
	{
		return competition;
	}
	
	/**  Constructor for the FormMain object */
	public FormMain(Competition competition, JDesktopPane desktop)
	{
		super("Ecran principal", true, true);
		//setClosable(false);
		this.desktop = desktop;	
		this.competition = competition;	
		this.competition.addObserver(this);
		cbTournois.setModel(new DefaultComboBoxModel());
		//this.lTournois = tournois;
		setBounds(10, 10, 500, 300);        
		URL location;
		location = java.lang.ClassLoader.getSystemResource("fleche_haut.gif");
		bUpJoueur.setIcon(new ImageIcon(location));
		location = java.lang.ClassLoader.getSystemResource("fleche_bas.gif");
		bDownJoueur.setIcon(new ImageIcon(location));

		location = java.lang.ClassLoader.getSystemResource("fleche_droite.gif");
		bAddJoueur.setIcon(new ImageIcon(location));

		bUpJoueur.setToolTipText("Ранг игрока более высокого уровня в списке.");
		bDownJoueur.setToolTipText("Устанавливает игрока на более низком уровне в списке.");
		bAddJoueur.setToolTipText("Добавляет игрока последним в выбранную таблицу.");
        cbTournois.setToolTipText("Список формул.");
        bCreer.setToolTipText("Создайте новую таблицу.");
        bLancer.setToolTipText("Запустите выбранную таблицу.");
        bReload.setToolTipText("Перезагрузите список формул, чтобы учесть новые формулы.");
		bSupprimer.setToolTipText("Удаляет выбранный массив.");
        
        
	GridBagConstraints constraints = new GridBagConstraints();
	JPanel panel = new JPanel(new BorderLayout());
		panel.setLayout(new GridBagLayout());
		listeJoueurs = new JList();
		listeJoueurs.setModel(new DefaultListModel());
		listeTournoisCreer = new JList();
		listeTournoisCreer.setModel(new DefaultListModel());		        
		listeJoueurs.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		listeTournoisCreer.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);		
		listeJoueurs.setCellRenderer(new JoueursCellRenderer());
		listeTournoisCreer.setCellRenderer(new TableauxCellRenderer());
		bCreer.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				buttonCreerClick();

			}
		});
		
		bUpJoueur.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				upJoueur(arg0);
			}
		});
		
		bAddJoueur.addActionListener(new ActionListener()
				{
			public void actionPerformed(ActionEvent arg0)
			{
				addJoueur(arg0);
			}
		});        
        
		bDownJoueur.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				downJoueur(arg0);
			}
		});

		bReload.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				buttonReloadClick();

			}
		});

		bLancer.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				buttonLancerClick();

			}
		});
		
		bSupprimer.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				buttonSupprimerClick();

			}
		});
		
		
	int ligne = 0;
		//================= 1ere ligne =========================
		constraints.gridx = 1;
		constraints.gridy = ligne;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.insets = new Insets(2, 2, 2, 2);	
		panel.add(new JLabel("Список игроков"), constraints);

		constraints.gridx = 4;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.insets = new Insets(2, 2, 2, 2);
		panel.add(new JLabel("Список созданных таблиц"), constraints);
		
		//==================== 2�me ligne ============================
		ligne++;
		constraints.gridx = 0;
		constraints.gridy = ligne;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.fill = GridBagConstraints.NONE;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.insets = new Insets(2, 2, 2, 2);		
		panel.add(bUpJoueur, constraints);										

		constraints.gridx = 1;
		constraints.gridy = ligne;
		constraints.gridwidth = 1;
		constraints.gridheight = 3;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.insets = new Insets(2, 2, 2, 2);		
		JScrollPane scroll = new JScrollPane(listeJoueurs, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		panel.add(scroll, constraints);		
		
		constraints.gridx = 2;
		constraints.gridy = ligne;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.SOUTH;
		constraints.fill = GridBagConstraints.NONE;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.insets = new Insets(2, 2, 2, 2);
		cbTournois = new JComboBox(new Vector(competition.getVisibleTableauxList()));		
		panel.add(cbTournois, constraints);		

		constraints.gridx = 4;
		constraints.gridy = ligne;
		constraints.gridwidth = 1;
		constraints.gridheight = 3;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.insets = new Insets(2, 2, 2, 2);
		JScrollPane scroll2 = new JScrollPane(listeTournoisCreer, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		panel.add(scroll2, constraints);		
		
		constraints.gridx = 5;
		constraints.gridy = ligne;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.SOUTH;
		constraints.fill = GridBagConstraints.NONE;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.insets = new Insets(2, 2, 2, 2);		
		panel.add(bLancer, constraints);		
		
		
		//=========== 3eme ligne ===================
		ligne++;
		constraints.gridx = 0;
		constraints.gridy = ligne;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.fill = GridBagConstraints.NONE;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.insets = new Insets(2, 2, 2, 2);		
		panel.add(bDownJoueur, constraints);		
		
		
		constraints.gridx = 2;
		constraints.gridy = ligne;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTHEAST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.insets = new Insets(2, 2, 2, 2);		
		panel.add(bCreer, constraints);		

		constraints.gridx = 3;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		panel.add(bReload, constraints);

		constraints.gridx = 5;
		constraints.gridy = ligne;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.fill = GridBagConstraints.NONE;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.insets = new Insets(2, 2, 2, 2);		
		panel.add(bSupprimer, constraints);		
				
		//=========== 4eme ligne ===================
		ligne++;
		constraints.gridx = 2;
		constraints.gridy = ligne;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.fill = GridBagConstraints.NONE;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.insets = new Insets(2, 2, 2, 2);		
		panel.add(bAddJoueur, constraints);				
		getContentPane().add(BorderLayout.CENTER, panel);
		setResizable(true);
		setMaximizable(true);
		setIconifiable(true);		
		pack();
	}
	
	public void buttonSupprimerClick()
	{
		int index = listeTournoisCreer.getSelectedIndex();
		if(index!=-1)
		{
			competition.removeTableau((Tableau)((DefaultListModel)listeTournoisCreer.getModel()).getElementAt(index));
		}
	}
	
	public void buttonReloadClick()
	{
		XMLAnalyzer analyser = new XMLAnalyzer();		
		cbTournois.removeAllItems();		
		ArrayList tableaux = analyser.getCompetition().getDescriptionTableauList();
		competition.clearAllDescriptions();
		for (Iterator iter = tableaux.iterator(); iter.hasNext();)
		{
			DescriptionTableau description = (DescriptionTableau)iter.next();
			if(description.isVisible())
			{
				cbTournois.addItem(description);
			}
			competition.addDescriptionTableau(description);	
		}				
	}
	
	public void buttonCreerClick()
	{
		//== on r�cup�re les joueurs s�lectionn�s ==
		java.util.List list = Arrays.asList(listeJoueurs.getSelectedValues());
		DescriptionTableau description = (DescriptionTableau)cbTournois.getSelectedItem();
		
		try
		{	
			String tabName = JOptionPane.showInputDialog(
					getContentPane(),
					"Как называется ваша таблица?");
			if(tabName!=null && !tabName.equals(""))
			{
				Tableau tableau = competition.addTableau(description.getIdentifiant(), tabName);	
                if(tableau==null)
                {
                    JOptionPane.showMessageDialog(
                            getContentPane(),
                            "Имя этой таблицы уже существует! Пожалуйста, выберите другое имя!",
                            "Ошибка!",
                            JOptionPane.ERROR_MESSAGE);                      
                    return;
                }
				tableau.setJoueurs(new ArrayList(list));
				//tableau.setJoueurs(list.);									
			}
		}
		catch(TournoiException e)
		{
			JOptionPane.showMessageDialog(
					getContentPane(),
					e.getMessage(),
					"Ошибка!",
					JOptionPane.ERROR_MESSAGE);			
		}				
	}
	
	public void buttonLancerClick()
	{
		Tableau tableau = (Tableau)listeTournoisCreer.getSelectedValue();
		if(tableau==null) return;
		if(tableau.getType().equals(TableauKO.TYPE))
		{
			((TableauKO)tableau).creerPremierTour();
			FormDisplayTableauSimple form = new FormDisplayTableauSimple((TableauKO)tableau);
			desktop.add(form);
			form.setVisible(true);	
			form.setFocusable(true);
			form.grabFocus();		
			try
			{
				form.setSelected(true);		
			}
			catch(Exception e)
			{
				logger.error(e);
			}
				
		}
		else if(tableau.getType().equals(TableauDoubleKO.TYPE))
		{
			((TableauDoubleKO)tableau).creerPremierTour();
			FormDisplayTableauSimple form = new FormDisplayTableauSimple((TableauDoubleKO)tableau);
			desktop.add(form);
			form.setVisible(true);
			form.setFocusable(true);
			form.grabFocus();			
			try
			{
				form.setSelected(true);		
			}
			catch(Exception e)
			{
				logger.error(e);
			}
						
		}
		else if(tableau.getType().equals(TableauIntegral.TYPE))
		{
			((TableauIntegral)tableau).creerPremierTour();
			FormDisplayTableauSimple form = new FormDisplayTableauSimple((TableauIntegral)tableau);
			desktop.add(form);
			form.setVisible(true);
			form.setFocusable(true);
			form.grabFocus();			
			try
			{
				form.setSelected(true);		
			}
			catch(Exception e)
			{
				logger.error(e);
			}

		}						
		else if(tableau.getType().equals(TableauPoules.TYPE))
		{
			FormDisplayTableauPoules form = new FormDisplayTableauPoules(competition, (TableauPoules)tableau);
			desktop.add(form);
			form.setVisible(true);
			form.setFocusable(true);
			form.grabFocus();	
			try
			{
				form.setSelected(true);		
			}
			catch(Exception e)
			{
				logger.error(e);
			}
			
		}
	}

	public void update(Observable arg0, Object arg1)
	{
		DefaultListModel modelListJoueurs = (DefaultListModel)listeJoueurs.getModel();
		modelListJoueurs.clear();
		for (Iterator iter = competition.getJoueurs().iterator(); iter.hasNext();)
		{
			modelListJoueurs.addElement(iter.next());
		}				
		DefaultListModel modelListTableau = (DefaultListModel)listeTournoisCreer.getModel();
		modelListTableau.clear();
		int i=0;
		for (Iterator iter = competition.getTableaux().iterator(); iter.hasNext();)
		{
			modelListTableau.addElement(iter.next());			
		}	
		//listeJoueurs.ensureIndexIsVisible(0);
		//listeTournoisCreer.ensureIndexIsVisible(0);			
	}
	
	public void upJoueur(ActionEvent e)
	{
		int index = listeJoueurs.getSelectedIndex();
		if (index>0)
		{
			Joueur joueur = (Joueur)competition.getJoueurs().get(index);
			competition.getJoueurs().remove(index);
			competition.getJoueurs().add(index-1, joueur);
			competition.notifyJoueursChanged();
			listeJoueurs.setSelectedIndex(index-1);
		}		
	}

	public void addJoueur(ActionEvent e)
	{
		java.util.List list = Arrays.asList(listeJoueurs.getSelectedValues());
		Tableau tableau = (Tableau)listeTournoisCreer.getSelectedValue();
		if(tableau==null)
		{
			JOptionPane.showMessageDialog(
					getContentPane(),
					"Вы должны выбрать таблицу, в которую будут добавлены игроки",
					"Внимание!",
					JOptionPane.INFORMATION_MESSAGE);
			return;
			
		}
		if(list==null||list.size()<1)
		{
			JOptionPane.showMessageDialog(
					getContentPane(),
					"Вы должны выбрать игроков для добавления в таблицу.",
					"Внимание!",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		if(tableau.getMax_joueurs()!=0 && (tableau.getJoueurs().size()+list.size())>tableau.getMax_joueurs())
		{
			JOptionPane.showMessageDialog(
					getContentPane(),
					"Вы достигли максимального количества игроков на эту таблицу.",
					"Внимание!",
					JOptionPane.INFORMATION_MESSAGE);
			return;
			
		}
		if(tableau.getType().equals(TableauPoules.TYPE))
		{
			for (Iterator iter = list.iterator(); iter.hasNext();) {
				Joueur joueur = (Joueur) iter.next();
				tableau.addJoueur(joueur);
			}
		}
		
	}	
	
	public void downJoueur(ActionEvent e)
	{
		int index = listeJoueurs.getSelectedIndex();
		if (index!=-1 && index<competition.getJoueurs().size()-1)
		{
			Joueur joueur = (Joueur)competition.getJoueurs().get(index);
			Joueur joueur2 = (Joueur)competition.getJoueurs().get(index+1);
			competition.getJoueurs().set(index,joueur2);
			competition.getJoueurs().set(index+1,joueur);			
			competition.notifyJoueursChanged();
			listeJoueurs.setSelectedIndex(index+1);
		}		
	}	
}
