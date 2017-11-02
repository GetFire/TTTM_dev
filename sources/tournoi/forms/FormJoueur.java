package tournoi.forms;

import javax.swing.*;
import javax.swing.event.*;

import org.apache.log4j.Logger;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.io.*;
import java.util.*;

import tournoi.*;
import tournoi.component.JoueursTableModel;
import tournoi.component.PlaceTableCellRenderer;
import myUtils.*;

/**
 *  La fen�tre qui permet de saisir des joueurs.
 */
public class FormJoueur extends JInternalFrame implements Observer, TableModelListener
{
	/**
     * 
     */
    
    private static final long serialVersionUID = 1L;
    private static Logger logger = Logger.getLogger(FormJoueur.class);
	// private Vector lJoueurs;
	private JLabel lblNom = new JLabel("Имя");
	private JLabel lblPrenom = new JLabel("Фамилия");
	private JLabel lblMasculin = new JLabel("Мужчина");
	private JLabel lblFeminin = new JLabel("Женщина");
	private JLabel lblClt = new JLabel("Класс");
	private JLabel lblListeJoueurs = new JLabel("Список участников соревнований");

	private JTextField tfNom = new JTextField(new ControledDocument(50, true, false), "", 13);
	private JTextField tfPrenom = new JTextField(new ControledDocument(50, false, false), "", 13);
	private JTextField tfCltNew = new JTextField(new ControledDocument(5, false, true), "", 4);
	private JTextField tfClub = new JTextField(new ControledDocument(35, true, false), "", 10);
	private JTextField tfNumero = new JTextField(new ControledDocument(5, false, true), "", 4);
	

	private ButtonGroup bgSexe = new ButtonGroup();
	private JRadioButton rbMasculin = new JRadioButton();
	private JRadioButton rbFeminin = new JRadioButton();
	private ButtonGroup bgLicencie = new ButtonGroup();
	private JRadioButton rbLincencie = new JRadioButton();
	private JRadioButton rbNonLicencie = new JRadioButton();
	private Club club = new Club();
	private JComboBox cbCltOld = new JComboBox(Classement.getOldClassementList());
	private JComboBox cbCategorie = new JComboBox(Categorie.getCategorieList());
	private JComboBox cbClub = new JComboBox(club.getClubList());

	private JButton bAjouter = new JButton("Добавить");
	private JButton bModifier = new JButton("Изменить");
	private JButton bSupprimer = new JButton("Удалить");
	private JButton bSauverListe = new JButton("Сохранить все");
	private JButton bSauverSelection = new JButton("Сохранить выбранные");
	private JButton bImprimerPresents = new JButton("Присутствующие игроки");
	private JButton bImprimerJoueurs = new JButton("Печать игроков");
	private JButton bChargerListe = new JButton("Загрузка");
	private JButton bAddClub = new JButton("+");
    private JButton bRAZPart = new JButton("RAZ participation");
    
	JTable tableJoueurs = null;
	TableSorter sorter = null;
	
	private Competition competition;

	/**  Constructor for the FormPoule object */
	public FormJoueur(Competition competition)
	{
		super("Список участников соревнований", true, true);
		//setClosable(false);
		this.competition = competition;
		competition.addObserver(this);		
		JoueursTableModel modelJoueur = new JoueursTableModel();				
		sorter = new TableSorter(modelJoueur);
		tableJoueurs = new JTable(sorter);
		sorter.setTableHeader(tableJoueurs.getTableHeader());
		tableJoueurs.setPreferredScrollableViewportSize(new Dimension(500, 70));
		tableJoueurs.setAutoscrolls(true);		
		tableJoueurs.getTableHeader().setToolTipText(
				"Click to specify sorting; Control-Click to specify secondary sorting");
				
		modelJoueur.addTableModelListener(this);
		PlaceTableCellRenderer renderer = new PlaceTableCellRenderer();
		PlaceTableCellRenderer renderer2 = new PlaceTableCellRenderer();
		tableJoueurs.setDefaultRenderer( Number.class, renderer );
		tableJoueurs.setDefaultRenderer( String.class, renderer2 );		
		renderer.setHorizontalAlignment(PlaceTableCellRenderer.CENTER);

		
		JScrollPane scrollPane = new JScrollPane(tableJoueurs);
		//add(scrollPane);			
		setBounds(10, 10, 500, 300);
	GridBagConstraints constraints = new GridBagConstraints();
	JPanel panel = new JPanel(new BorderLayout());
		panel.setLayout(new GridBagLayout());
		tableJoueurs.getSelectionModel().addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent arg0)
			{
				selectionJoueurChanged(arg0);

			}
		});		
		tableJoueurs.addKeyListener(new KeyListener()
		{
			public void keyTyped(KeyEvent e)
			{
			}

			public void keyPressed(KeyEvent e)
			{
				actionKeyPressed(e);

			}

			public void keyReleased(KeyEvent e)
			{
			}
		});
		
		rbLincencie.addChangeListener(
			new ChangeListener()
			{
				public void stateChanged(ChangeEvent e)
				{
					rbLincencieStateChanged(e);
				}
			});
		bAjouter.addActionListener(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					ajouterJoueur(e);
				}
			});

		bAddClub.addActionListener(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					ajouterClub(e);
				}
			});

		bImprimerPresents.addActionListener(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					imprimerPresents(e);
				}
			});

			
		bImprimerJoueurs.addActionListener(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					imprimerJoueurs(e);
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

		bModifier.addActionListener(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					modifierJoueur(e);
				}
			});

		bSauverListe.addActionListener(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					sauverListe(e);
				}
			});

		bSauverSelection.addActionListener(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					sauverSelection(e);
				}
			});
			
			
		bChargerListe.addActionListener(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					chargerListe(e);
				}
			});

        bRAZPart.addActionListener(
                new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        clearParticipation(e);
                    }
                });
		
        bAjouter.setToolTipText("Добавьте этого игрока в список игроков.");
        bAddClub.setToolTipText("Добавьте клуб в список клубов.");
        bChargerListe.setToolTipText("Загрузите список игроков из файла.");
        bImprimerJoueurs.setToolTipText("Распечатайте список игроков.");
        bImprimerPresents.setToolTipText("Распечатывает список игроков, присутствующих на турнире.");
        bModifier.setToolTipText("Измените выбранного игрока.");
        bRAZPart.setToolTipText("Измените количество участников всех игроков.");
        bSauverListe.setToolTipText("Сохраняет список игроков в файле.");
        bSauverSelection.setToolTipText("Сохраняет список игроков, присутствующих в файле.");
        bSupprimer.setToolTipText("Очищает выбранного игрока.");
        tfCltNew.setToolTipText("Новая классификация (EX: 1685)");
        cbCltOld.setToolTipText("Старый рейтинг");
        tfClub.setToolTipText("Позволяет сообщать о новом клубе");
        
		int ligne = 0;
		//================= 1ere ligne =========================
		ligne++;
		constraints.gridy = ligne;
		constraints.gridx = 3;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTHEAST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.insets = new Insets(2, 2, 2, 2);
		panel.add(lblNom, constraints);
		
		constraints.gridy = ligne;
		constraints.gridx = 4;
		constraints.gridwidth = 3;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.insets = new Insets(2, 2, 2, 2);
		panel.add(tfNom, constraints);

		constraints.gridx = 0;
		constraints.gridy = ligne;
		constraints.gridwidth = 3;
		constraints.gridheight = 9;
		constraints.weightx = 1.0;
		constraints.weighty = 0.8;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.insets = new Insets(2, 2, 2, 2);
		panel.add(scrollPane, constraints);


		constraints.gridx = 3;
		constraints.gridy = ++ligne;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTHEAST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.insets = new Insets(2, 2, 2, 2);
		panel.add(lblPrenom, constraints);

		constraints.gridx = 4;
		constraints.gridwidth = 3;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.insets = new Insets(2, 2, 2, 2);
		panel.add(tfPrenom, constraints);

		//================= 3eme ligne =========================
		bgSexe.add(rbFeminin);
		bgSexe.add(rbMasculin);
		constraints.gridx = 3;
		constraints.gridy = ++ligne;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.insets = new Insets(2, 2, 2, 2);
		panel.add(lblMasculin, constraints);

		constraints.gridx = 4;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.insets = new Insets(2, 2, 2, 2);
		panel.add(rbMasculin, constraints);

		constraints.gridx = 5;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.insets = new Insets(2, 2, 2, 2);
		panel.add(lblFeminin, constraints);

		constraints.gridx = 6;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.insets = new Insets(2, 2, 2, 2);
		panel.add(rbFeminin, constraints);
		//================= 3eme ligne =========================
		bgLicencie.add(rbLincencie);
		bgLicencie.add(rbNonLicencie);
		constraints.gridx = 3;
		constraints.gridy = ++ligne;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.insets = new Insets(2, 2, 2, 2);
		panel.add(new JLabel("Лицензирован"), constraints);

		constraints.gridx = 4;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.insets = new Insets(2, 2, 2, 2);
		panel.add(rbLincencie, constraints);

		constraints.gridx = 5;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.insets = new Insets(2, 2, 2, 2);
		panel.add(new JLabel("Не лицензирован"), constraints);

		constraints.gridx = 6;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.insets = new Insets(2, 2, 2, 2);
		panel.add(rbNonLicencie, constraints);

		//=======================================================
		constraints.gridx = 3;
		constraints.gridy = ++ligne;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.insets = new Insets(2, 2, 2, 2);
		panel.add(new JLabel("Клуб"), constraints);

		constraints.gridx = 4;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.insets = new Insets(2, 2, 2, 2);
		panel.add(cbClub, constraints);
		
		constraints.gridx = 5;
		panel.add(tfClub, constraints);
		
		constraints.gridx = 6;
		panel.add(bAddClub, constraints);
		

		//=================== 4�me ligne =========================
		constraints.gridx = 3;
		constraints.gridy = ++ligne;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.insets = new Insets(2, 2, 2, 2);
		panel.add(lblClt, constraints);

		constraints.gridx = 4;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.insets = new Insets(2, 2, 2, 2);
		panel.add(tfCltNew, constraints);

		constraints.gridx = 5;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.insets = new Insets(2, 2, 2, 2);
		panel.add(cbCltOld, constraints);
		
		constraints.gridx = 6;
		tfNumero.setToolTipText("пронумерованные игроки");
		panel.add(tfNumero, constraints);
		
		//=================== 4�me ligne =========================
		constraints.gridx = 3;
		constraints.gridy = ++ligne;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.insets = new Insets(2, 2, 2, 2);
		panel.add(new JLabel("Категория"), constraints);

		constraints.gridx = 4;
		constraints.gridwidth = 3;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.insets = new Insets(2, 2, 2, 2);
		panel.add(cbCategorie, constraints);

		//================== 5 ligne ====================
		
		JPanel panelButtonsJoueurs = new JPanel(new GridLayout(1,3));
		constraints.gridx = 3;
		constraints.gridy = ++ligne;
		constraints.gridwidth = 5;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.fill = GridBagConstraints.NONE;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.insets = new Insets(2, 2, 2, 2);
		panel.add(panelButtonsJoueurs, constraints);
		
		panelButtonsJoueurs.add(bAjouter);
		panelButtonsJoueurs.add(bModifier);
		panelButtonsJoueurs.add(bSupprimer);
		
		
		JPanel panelButtonsListe = new JPanel(new GridLayout(3,2));
		constraints.gridx = 3;
		constraints.gridy = ++ligne;
		constraints.gridwidth = 5;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.SOUTH;
		constraints.fill = GridBagConstraints.NONE;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.insets = new Insets(2, 2, 2, 2);
		panel.add(panelButtonsListe, constraints);
		panelButtonsListe.add(bSauverListe);
		panelButtonsListe.add(bSauverSelection);
		panelButtonsListe.add(bChargerListe);
		panelButtonsListe.add(bImprimerPresents);
		panelButtonsListe.add(bImprimerJoueurs);
        panelButtonsListe.add(bRAZPart);
		
		
		bSauverListe.setFocusable(false);
		bSauverSelection.setFocusable(false);
		bChargerListe.setFocusable(false);
		bImprimerPresents.setFocusable(false);
		bImprimerJoueurs.setFocusable(false);	
		
		
		selectDefaultValue();
		getContentPane().add(BorderLayout.CENTER, panel);		
		setResizable(true);
		setMaximizable(true);
		setIconifiable(true);
		//pack();
	}

	/**  Description of the Method */
	public void selectDefaultValue()
	{
		cbCategorie.setSelectedIndex(1);
		cbCltOld.setSelectedIndex(0);
		cbClub.setSelectedIndex(0);
		rbLincencie.setSelected(true);
		rbMasculin.setSelected(true);

	}

	/**
	 *  �coute les chagenement du radio button licencie
	 *
	 * @param  e  l'�v�nement d�clench�
	 */
	public void rbLincencieStateChanged(ChangeEvent e)
	{
	boolean isSelected = rbLincencie.isSelected();
		cbClub.setEnabled(isSelected);
		tfCltNew.setEnabled(isSelected);
		cbCltOld.setEnabled(isSelected);
		tfNumero.setEnabled(isSelected);
	}

	public void selectRowOff(Joueur joueur)
	{
		int index = 0;
		for(int x=0; x<tableJoueurs.getRowCount(); x++)
		{			
			index = x;
			index = sorter.modelIndex(index);
			Joueur j = ((JoueursTableModel)sorter.getTableModel()).getJoueur(index);
			if(j.equals(joueur))
			{	
							
				tableJoueurs.scrollRectToVisible(tableJoueurs.getCellRect(tableJoueurs.getRowCount()-1,1,true));
				tableJoueurs.scrollRectToVisible(tableJoueurs.getCellRect(x,1,true));
				tableJoueurs.getSelectionModel().setSelectionInterval(x,x);
				break;
			}			
		}			
		
	}

	/**
	 *  Description of the Method
	 *
	 * @param  e  Description of the Parameter
	 */
	public void ajouterJoueur(ActionEvent e)
	{	
		tableJoueurs.scrollRectToVisible(tableJoueurs.getCellRect(tableJoueurs.getRowCount()-1,1,true));	
		Joueur j = getJoueur();	
		if (j == null)
		{
			// afficher un message d'erreur
			return;
		}
		boolean ajout = ((JoueursTableModel)sorter.getTableModel()).addJoueur(j);
		if(ajout)
		{
			((JoueursTableModel)sorter.getTableModel()).fireTableDataChanged();
			selectRowOff(j);		
		}
		else
		{
			effacerSaisie();
		}
						
	}

	public boolean ajouterJoueur(Joueur joueur)
	{				
		if (joueur == null)
		{
			// afficher un message d'erreur
			return false;
		}
		return ((JoueursTableModel)sorter.getTableModel()).addJoueur(joueur);
	}



	public void ajouterClub(ActionEvent e)
	{		
		String strClub = tfClub.getText();
		if(strClub!=null && !strClub.equals(""))
		{
			club.addClub(strClub);
			cbClub.setModel(new DefaultComboBoxModel(club.getClubList()));
			tfClub.setText("");
			cbClub.setSelectedItem(strClub);
		}		
	}

	
	/**
	 *  Modifie les caract�ristique d'un joueurs
	 *
	 * @param  e  Description of the Parameter
	 */
	public void modifierJoueur(ActionEvent e)
	{
	Joueur j = getOldJoueur();
	int index = tableJoueurs.getSelectedRow();
	if(index==-1) return;
	index = sorter.modelIndex(index);

		if (j == null || index == -1)
		{
			// Pas de modification : la nouvelle saisie est invalide
			return;
		}
		competition.modifierJoueur(((JoueursTableModel)sorter.getTableModel()).getJoueur(index),j);
		((JoueursTableModel)sorter.getTableModel()).setJoueur(index,j);
		((JoueursTableModel)sorter.getTableModel()).fireTableDataChanged();
		selectRowOff(j);				
	}

	/**  Description of the Method */
	public void effacerSaisie()
	{
		tfCltNew.setText("");
		tfNumero.setText("");
		tfNom.setText("");
		tfPrenom.setText("");
		selectDefaultValue();
	}

	/**
	 *  fabrique un joueur en fonction des champs saisies et de l'ancien joueur de la liste
	 *
	 * @return    le joueur fabrique ou null si les champs �taient invalid
	 */
	public Joueur getOldJoueur()
	{
	Joueur j;
	int index = tableJoueurs.getSelectedRow();
	if(index==-1) return null;
	index = sorter.modelIndex(index);	
	Joueur oldJoueur = ((JoueursTableModel)sorter.getTableModel()).getJoueur(index);
	if(oldJoueur==null) return null;
	
	String nom = tfNom.getText();
	String prenom = tfPrenom.getText();
	boolean isMasculin = rbMasculin.isSelected();
	Categorie categorie = new Categorie((String)cbCategorie.getSelectedItem());
	
		if (nom.equals("") || prenom.equals(""))
		{
			// afficher un message d'erreur
			return null;
		}
		// le joueur n'est pas licenci�
		if (!rbLincencie.isSelected())
		{
			j = new Joueur(nom, prenom, isMasculin, categorie);
		}
		else
		{
			int newClassement = (tfCltNew.getText().equals("")) ? 0 : Integer.parseInt(tfCltNew.getText());
			int oldClassement = ((Integer)cbCltOld.getSelectedItem()).intValue();			
			int numero = (tfNumero.getText().equals("")) ? 0 : Integer.parseInt(tfNumero.getText());
			Classement clt = null;
			Classement cltOld = oldJoueur.getClassement();
			if(numero!=0)
			{
				clt = new Classement(numero, isMasculin);
			}
			else
			{
				if(cltOld!=null && oldClassement!=cltOld.getOldClassement())
				{
					clt = new Classement(oldClassement, -1, isMasculin);
				}
				else
				{
					clt = new Classement(oldClassement, newClassement, isMasculin);
				}
				
			}			
			j = new Joueur(nom, prenom, isMasculin, categorie, clt, true);			
		}
		j.setClub((String)cbClub.getSelectedItem());
		j.setHere(oldJoueur.isHere());
		j.setNbParticipation(oldJoueur.getNbParticipation());
		
		return j;
	}


	/**
	 *  fabrique un joueur en fonction des champs saisies
	 *
	 * @return    le joueur fabrique ou null si les champs �taient invalid
	 */
	public Joueur getJoueur()
	{
	Joueur j;
	String nom = tfNom.getText();
	String prenom = tfPrenom.getText();
	boolean isMasculin = rbMasculin.isSelected();
	Categorie categorie = new Categorie((String)cbCategorie.getSelectedItem());
		if (nom.equals("") || prenom.equals(""))
		{
			// afficher un message d'erreur
			return null;
		}
		// le joueur n'est pas licenci�
		if (!rbLincencie.isSelected())
		{
			j = new Joueur(nom, prenom, isMasculin, categorie);
		}
		else
		{
			int newClassement = (tfCltNew.getText().equals("")) ? 0 : Integer.parseInt(tfCltNew.getText());
			int oldClassement = ((Integer)cbCltOld.getSelectedItem()).intValue();			
			int numero = (tfNumero.getText().equals("")) ? 0 : Integer.parseInt(tfNumero.getText());
			Classement clt = null;
			if(numero!=0)
			{
				clt = new Classement(numero, isMasculin);
			}
			else //if (newClassement != 0 || oldClassement != 0)
			{
				clt = new Classement(oldClassement, newClassement, isMasculin);
			}			
			j = new Joueur(nom, prenom, isMasculin, categorie, clt, true);			
		}
		j.setClub((String)cbClub.getSelectedItem());
		return j;
	}
	
	/**
	 *  Description of the Method
	 *
	 * @param  e  Description of the Parameter
	 */
	public void chargerListe(ActionEvent evt)
	{
		JFileChooser fileChooser = new JFileChooser();
		MyFileFilter filter = new MyFileFilter();
		filter.addExtension("xml");
		fileChooser.setFileFilter(filter);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);		
		int returnVal = fileChooser.showOpenDialog(FormJoueur.this);
		
		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			//=== suppression des joueurs ===
			((JoueursTableModel)sorter.getTableModel()).clear();			
			File file = fileChooser.getSelectedFile();		
			try
			{
				XMLDecoder d = new XMLDecoder(
								   new BufferedInputStream(
									   new FileInputStream(file)));
				while(true)
				{						   
	
					try
					{
						Object result = d.readObject();
						((JoueursTableModel)sorter.getTableModel()).addJoueur((Joueur)result);
					}
					catch(ArrayIndexOutOfBoundsException exp)
					{
						break;
					}							
				};
				d.close();
			}
			catch(Exception exp)
			{				
				logger.error(exp);
			}
		}	
		((JoueursTableModel)sorter.getTableModel()).fireTableDataChanged();	
		//competition.notifyJoueursChanged();
	}

	/**
	 *  Description of the Method
	 *
	 * @param  e  Description of the Parameter
	 */
	public void sauverListe(ActionEvent evt)
	{
		JFileChooser fileChooser = new JFileChooser();
		MyFileFilter filter = new MyFileFilter();
		filter.addExtension("xml");
		fileChooser.setFileFilter(filter);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);		
		int returnVal = fileChooser.showOpenDialog(FormJoueur.this);
		
		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			File file = fileChooser.getSelectedFile();
			try
			{
				// si le fichier n'existe pas on le cr�er
				if(!file.exists())
				{
					if(!file.getName().endsWith(".xml")&&!file.getName().endsWith(".XML"))
					{
						file = new File(file.getAbsolutePath()+".xml");
						if(!file.createNewFile())
						{
							JOptionPane.showMessageDialog(this,"Ошибка при регистрации!","Ошибка",JOptionPane.ERROR_MESSAGE);
						}
					}
				}
				XMLEncoder e = new XMLEncoder(
								   new BufferedOutputStream(
									   new FileOutputStream(file)));
				Iterator joueurs = ((JoueursTableModel)sorter.getTableModel()).getJoueurs().iterator();
				while(joueurs.hasNext())
				{
					Joueur joueur = (Joueur)joueurs.next();
					joueur.setHere(false);
					e.writeObject(joueur);
				}
				e.close();
			}
			catch(Exception exp)
			{
				logger.error(exp);
			}
						
		}		
	}

	
	/**
	 *  Sauvegarde les joueurs pr�sents dans le tournois
	 *
	 * @param  e  Description of the Parameter
	 */
	public void sauverSelection(ActionEvent evt)
	{
		JFileChooser fileChooser = new JFileChooser();
		MyFileFilter filter = new MyFileFilter();
		filter.addExtension("xml");
		fileChooser.setFileFilter(filter);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);		
		int returnVal = fileChooser.showOpenDialog(FormJoueur.this);
		
		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			try
			{
				File file = fileChooser.getSelectedFile();
				// si le fichier n'existe pas on le cr�er
				if(!file.exists())
				{
					if(!file.getName().endsWith(".xml")&&!file.getName().endsWith(".XML"))
					{
						file = new File(file.getAbsolutePath()+".xml");
						if(!file.createNewFile())
						{
							JOptionPane.showMessageDialog(this,"Ошибка при регистрации!","Ошибка",JOptionPane.ERROR_MESSAGE);
						}
					}
				}							
				XMLEncoder e = new XMLEncoder(
								   new BufferedOutputStream(
									   new FileOutputStream(file)));
				Iterator joueurs = ((JoueursTableModel)sorter.getTableModel()).getJoueurs().iterator();
				while(joueurs.hasNext())
				{
					Joueur joueur = (Joueur)joueurs.next();
					if(joueur.isHere())
					{
						joueur.setHere(false);
						e.writeObject(joueur);						
					}
				}
				e.close();
			}
			catch(Exception exp)
			{
				logger.error(exp);
			}
						
		}		
	}
	
	/**
	 *  Description of the Method
	 *
	 * @param  e  Description of the Parameter
	 */
	public void supprimerJoueur(ActionEvent e)
	{
	int index = tableJoueurs.getSelectedRow();
	index = sorter.modelIndex(index);
		if (index != -1)
		{
			((JoueursTableModel)sorter.getTableModel()).removeJoueur(index);
			((JoueursTableModel)sorter.getTableModel()).fireTableDataChanged();
		}
	}

    /**
     *  Description of the Method
     *
     * @param  e  Description of the Parameter
     */
    public void clearParticipation(ActionEvent e)
    {
        ArrayList joueurs = ((JoueursTableModel)sorter.getTableModel()).getJoueurs();
        for (Iterator iter = joueurs.iterator(); iter.hasNext();)
        {
            Joueur j = (Joueur) iter.next();
            j.setNbParticipation(0);
        }
        ((JoueursTableModel)sorter.getTableModel()).fireTableDataChanged();
    }

    
    
	private int imprimerJoueur(Joueur j, Graphics pg, int position)
	{
		pg.setFont(new Font("Helvetica",Font.PLAIN,12));
		pg.drawString(j.getNom()+" "+j.getPrenom()+" ("+j.getStrOldClassement()+") "+j.getClub(),50,position);
		return 15;
	}
	
	private void imprimerPresents(ActionEvent e)
	{
		PrintJob pJob = getToolkit().getPrintJob(new Frame(),
					 "Printers_Available", null);
		if(pJob==null) return;	
		Dimension page = pJob.getPageDimension();	
		int position = 50;
		Graphics pg = pJob.getGraphics();			 
		Iterator joueurs = ((JoueursTableModel)sorter.getTableModel()).getJoueurs().iterator();
		while(joueurs.hasNext())
		{
			Joueur joueur = (Joueur)joueurs.next();						
			if(joueur.isHere())
			{
				position += imprimerJoueur(joueur,pg,position);					

			}
			if((position+50)>(page.height-10))
			{				
				pg.dispose();
				pg = pJob.getGraphics();
				position = 50;
			}						
		}
						
		pg.dispose();
		pJob.end();	
		
	}

	private void imprimerJoueurs(ActionEvent e)
	{
		PrintJob pJob = getToolkit().getPrintJob(new Frame(),
					 "Printers", null);
		if(pJob==null) return;	
		Dimension page = pJob.getPageDimension();	
		int position = 50;
		Graphics pg = pJob.getGraphics();			 
		Iterator joueurs = ((JoueursTableModel)sorter.getTableModel()).getJoueurs().iterator();
		while(joueurs.hasNext())
		{
			Joueur joueur = (Joueur)joueurs.next();						
			if(joueur.isHere())
			{
				position += imprimerJoueur(joueur,pg,position);					

			}
			if((position+50)>(page.height-10))
			{				
				pg.dispose();
				pg = pJob.getGraphics();
				position = 50;
			}						
		}
						
		pg.dispose();
		pJob.end();	
		
	}

	/**
	 *  Description of the Method
	 *
	 * @param  e  Description of the Parameter
	 */
	public void selectionJoueurChanged(ListSelectionEvent e)
	{
		if(tableJoueurs.getRowCount()<=0) return;
		int index = tableJoueurs.getSelectedRow();		
			if (index == -1)
			{
				effacerSaisie();
				return;				
			}			
			index = sorter.modelIndex(index);
	Joueur j = ((JoueursTableModel)sorter.getTableModel()).getJoueur(index);		
	if(j==null)
	{
		effacerSaisie();
	}
	else
	{
	Classement clt = j.getClassement();
		tfNom.setText(j.getNom());
		tfPrenom.setText(j.getPrenom());
		rbFeminin.setSelected(!j.isMasculin());
		rbMasculin.setSelected(j.isMasculin());
		cbCategorie.setSelectedItem(j.getCategorie().getCategorie());
		cbClub.setSelectedItem(j.getClub());
		if (!j.isLicencie())
		{
			rbNonLicencie.setSelected(true);
		}
		else if (!j.isClasse())
		{
			rbLincencie.setSelected(true);
			if(j.getClassement()!=null)
			{
				tfCltNew.setText(""+j.getClassement().getNewClassement());
			}			
			cbCltOld.setSelectedIndex(0);
		}
		else
		{
			rbLincencie.setSelected(true);
			tfCltNew.setText(Integer.toString(clt.getNewClassement()));
			cbCltOld.setSelectedItem(new Integer(clt.getOldClassement()));
			tfNumero.setText(Integer.toString(clt.getNumero()));
		}
	}
	}

	public void actionKeyPressed(KeyEvent e)
	{
		if(Character.isLetter(e.getKeyChar()))
		{
			// rechercher l'index du premier joueur dont le nom commence par la lettre tap�e
			int index = 0;
			for(int x=0; x<tableJoueurs.getRowCount(); x++)
			{
			
				index = x;
				index = sorter.modelIndex(index);
				Joueur j = ((JoueursTableModel)sorter.getTableModel()).getJoueur(index);
				if(j.getNom().startsWith(""+Character.toUpperCase(e.getKeyChar())))
				{	
							
					tableJoueurs.scrollRectToVisible(tableJoueurs.getCellRect(tableJoueurs.getRowCount()-1,1,true));
					tableJoueurs.scrollRectToVisible(tableJoueurs.getCellRect(x,1,true));
					tableJoueurs.getSelectionModel().setSelectionInterval(x,x);
					break;
				}			
			}			
		}		
	}


	/**
	 * modification de la comp�tition
	 * maj des donn�es
	 */
	public void update(Observable arg0, Object arg1)
	{
		// maj des joueurs
		if(competition.isRestauration())
		{
			competition.setRestauration(false);
			for (Iterator iter = competition.getJoueurs().iterator(); iter.hasNext();)
			{
				Joueur joueur = (Joueur) iter.next();
				joueur.setHere(true);				
				if(!ajouterJoueur(joueur))
				{
					((JoueursTableModel)sorter.getTableModel()).updateJoueur(joueur);
				}
			}			
			((JoueursTableModel)sorter.getTableModel()).fireTableDataChanged();
		}
	}


	public void tableChanged(TableModelEvent arg0)
	{
		
		if(arg0.getType()==TableModelEvent.UPDATE)
		{
			if(arg0.getColumn()==6)
			{
				int index = arg0.getLastRow();
				Joueur j = ((JoueursTableModel)sorter.getTableModel()).getJoueur(index);
				if(j.isHere()&&!competition.getJoueurs().contains(j))
				{
					ArrayList joueurs2 = (ArrayList)competition.getJoueurs().clone();
					joueurs2.add(j);
					Collections.sort(joueurs2,Collections.reverseOrder());
					int joueurIndex = joueurs2.indexOf(j);
					competition.getJoueurs().add(joueurIndex,j);					
				}
				else if(!j.isHere()&&competition.getJoueurs().contains(j))
				{
					competition.getJoueurs().remove(j);
				}
				int nbPart = j.getNbParticipation();
				if(j.isHere())
				{
					j.incParticipation();
				}
				else
				{
					j.decParticipation();
				}
				competition.notifyJoueursChanged();
				tableJoueurs.revalidate();
				tableJoueurs.repaint();
			}						
		}
		
	}
}

