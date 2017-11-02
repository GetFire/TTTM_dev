/*
 *
 */
package tournoi.forms;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

import tournoi.Competition;
import tournoi.DescriptionTableau;
import tournoi.SortiePoule;
import tournoi.TableauDoubleKO;
import tournoi.TableauIntegral;
import tournoi.TableauKO;
import tournoi.TableauPoules;
import tournoi.component.SortiePouleTableModel;

/**
 *
 */
public class FormFormulesAuto extends JInternalFrame implements TableModelListener
{
	/**
     * 
     */
    
    private static final long serialVersionUID = 1L;
    private Vector formulesList = new Vector();
	private JLabel lblFormules = new JLabel("Formules :");
	private JList jlFormules;
	private JLabel lblFormuleName = new JLabel("Nom :");
	private JTextField jtfFormuleName = new JTextField(5);	
	private JLabel lblNbPoules = new JLabel("Nombre de poules :");
	private JTextField jtfNbPoules = new JTextField(2);
	private JLabel lblSortiePoule = new JLabel("Sortie de poules :");
	private JButton bAddSortie = new JButton("+");
	private JButton bDeleteSortie = new JButton("-");
	private JButton bAddFormule = new JButton("Ajouter");
	private JButton bUpdateFormule = new JButton("Modifier");
	private JButton bDeleteFormule = new JButton("Supprimer");
	private JButton bSaveFormule = new JButton("Sauvegarder");
	private JTable jtSortiePoule = new JTable(new SortiePouleTableModel());
	private Logger logger = Logger.getLogger(FormFormulesAuto.class);
	
	
	private Competition competition = null;
	
	public FormFormulesAuto(Competition competition)
	{
		super("Liste des formules");
		this.competition = competition;
		setResizable(true);
		setIconifiable(true);
		init();
	}

	private void updateFormulesList()
	{
		((DefaultListModel)jlFormules.getModel()).clear();
		for (Iterator iter = formulesList.iterator(); iter.hasNext();)
		{
			((DefaultListModel)jlFormules.getModel()).addElement(iter.next());			
		}
		
	}

	private void updateSortiePouleList(DescriptionTableau desc)
	{
		((SortiePouleTableModel)jtSortiePoule.getModel()).clear();
		for (Iterator iter = desc.getSortiePouleList().iterator(); iter.hasNext();)
		{
			SortiePoule element = (SortiePoule) iter.next();
			((SortiePouleTableModel)jtSortiePoule.getModel()).addSortiePoule(element);
		}			
		jtSortiePoule.revalidate();
		jtSortiePoule.repaint();
		
	}

	public void formulesListChanged(ListSelectionEvent e)
	{
		int index = jlFormules.getSelectedIndex();
		if(index!=-1)
		{
			DescriptionTableau desc = (DescriptionTableau)formulesList.get(index);
			jtfFormuleName.setText(desc.getIdentifiant());
			jtfNbPoules.setText(""+desc.getNbPoules());
			((SortiePouleTableModel)jtSortiePoule.getModel()).clear();
			updateSortiePouleList(desc);			
		}
	}

	private void setConstraint( GridBagConstraints constraints, 
								int gridx, int gridy, int gridwidth, 
								int gridheight, double weightx, double weighty, 
								int anchor, int fill)
	{
		constraints.gridy = gridy;
		constraints.gridx = gridx;
		constraints.gridwidth = gridwidth;
		constraints.gridheight = gridheight;
		constraints.weightx = weightx;
		constraints.weighty = weighty;
		constraints.anchor = anchor;
		constraints.fill = fill;		
	}

	private void init()
	{
		formulesList = new Vector(competition.getFormulesAuto()); 		
		jlFormules = new JList(formulesList);
		jlFormules.setModel(new DefaultListModel());
		jlFormules.addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent e)
			{
				formulesListChanged(e);

			}
		});		
		TableColumn sportColumn = jtSortiePoule.getColumnModel().getColumn(1);
		jlFormules.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);	
		updateFormulesList();			
		JComboBox comboBox = new JComboBox();
		comboBox.addItem(TableauKO.TYPE);
		comboBox.addItem(TableauDoubleKO.TYPE);
		comboBox.addItem(TableauIntegral.TYPE);
		sportColumn.setCellEditor(new DefaultCellEditor(comboBox));
		((SortiePouleTableModel)jtSortiePoule.getModel()).addTableModelListener(this);		
		jtSortiePoule.setVisible(true);
		jtSortiePoule.setOpaque(true);	
		
		GridBagConstraints constraints = new GridBagConstraints();
		JPanel panel = new JPanel(new BorderLayout());
		panel.setLayout(new GridBagLayout());		
		int ligne = 0;
		bSaveFormule.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				saveFormules();

			}
		});
		
		bAddFormule.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				addFormule();

			}
		});
		
		bAddSortie.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				SortiePoule sortie = new SortiePoule();
				sortie.setNbJoueurs(0);
				sortie.setRefTableau("");
				sortie.setType(TableauIntegral.TYPE);				
				int index = jlFormules.getSelectedIndex();
				if(index!=-1)
				{
					DescriptionTableau desc = (DescriptionTableau)(formulesList.get(index)); 
					desc.getSortiePouleList().add(sortie);
					updateSortiePouleList(desc);					
				}
			}
		});

		bDeleteSortie.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				DescriptionTableau desc = (DescriptionTableau)(formulesList.get(jlFormules.getSelectedIndex())); 
				desc.getSortiePouleList().remove(desc.getSortiePouleList().size()-1);
				updateSortiePouleList(desc);
			}
		});
	
		bUpdateFormule.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				updateFormule();

			}
		});

		bDeleteFormule.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				int index = jlFormules.getSelectedIndex();
				if(index!=-1)
				{
					formulesList.remove(index);
					updateFormulesList();			
				}	
			}
		});

		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.insets = new Insets(2, 2, 2, 2);
		
		//================= 1ere ligne =========================
		setConstraint(constraints,0,++ligne,4,1,1.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL);
		panel.add(lblFormules, constraints);	
		//================= 2eme ligne =========================
		setConstraint(constraints,0,++ligne,4,1,1.0,1.0,GridBagConstraints.NORTH,GridBagConstraints.BOTH);
		JScrollPane sp = new JScrollPane(jlFormules);
		jlFormules.setVisibleRowCount(10);
		panel.add(sp, constraints);	
		//================= 3eme ligne =========================
		setConstraint(constraints,0,++ligne,1,1,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE);
		JPanel panelFormule = new JPanel(new GridLayout(1,4,0,0));
		panelFormule.add(lblFormuleName);
		panelFormule.add(jtfFormuleName);
		panelFormule.add(lblNbPoules);
		panelFormule.add(jtfNbPoules);
		panel.add(panelFormule, constraints);		
		//================= 4eme ligne =========================
		setConstraint(constraints,0,++ligne,4,1,1.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL);
		panel.add(lblSortiePoule, constraints);			
		//================= 5eme ligne =========================
		setConstraint(constraints,0,++ligne,3,1,1.0,1.0,GridBagConstraints.NORTH,GridBagConstraints.BOTH);
		JScrollPane scroll = new JScrollPane(jtSortiePoule);
		panel.add(scroll, constraints);					
		JPanel panelButtonsSortiePoule = new JPanel(new GridLayout(2,1,0,5));
		panelButtonsSortiePoule.add(bAddSortie);
		panelButtonsSortiePoule.add(bDeleteSortie);
		constraints.gridx = 3;
		constraints.gridwidth = 1;
		constraints.fill = GridBagConstraints.NONE;
		panel.add(panelButtonsSortiePoule, constraints);
		//================= 6eme ligne =========================
		setConstraint(constraints,0,++ligne,3,1,0.0,0.0,GridBagConstraints.NORTH,GridBagConstraints.NONE);
		JPanel panelButtonsFormule = new JPanel(new GridLayout(1,4,5,0));
		panelButtonsFormule.add(bAddFormule);
		panelButtonsFormule.add(bUpdateFormule);
		panelButtonsFormule.add(bDeleteFormule);
		panelButtonsFormule.add(bSaveFormule);
		panel.add(panelButtonsFormule, constraints);		
				
		setContentPane(panel);
		pack();	
	}

	private DescriptionTableau getFormule()
	{
		DescriptionTableau description = new DescriptionTableau();
		int nbPoules = 0;
		try
		{
			nbPoules = Integer.parseInt(jtfNbPoules.getText());
		}
		catch(Exception e)
		{
			return null;
		}
		description.setType(TableauPoules.TYPE);		
		description.setNbPoules(nbPoules);
		description.setOrdreMatchAuto(true);
		description.setVisible(true);
		description.setIdentifiant(jtfFormuleName.getText());
		description.setSortiePouleList(((SortiePouleTableModel)jtSortiePoule.getModel()).getSortiePouleList());
		if(description.getSortiePouleList()==null) description.setSortiePouleList(new ArrayList());
		return description;
	}
	
	private void updateFormule()
	{
		int index = jlFormules.getSelectedIndex();
		if(index!=-1)
		{
			DescriptionTableau formule = getFormule();
			int nbPoules = formule.getNbPoules();
			ArrayList sortiePouleList = formule.getSortiePouleList();
			for (Iterator iter = sortiePouleList.iterator(); iter.hasNext();) 
			{
				SortiePoule element = (SortiePoule) iter.next();
				if(element.getNbJoueurs()!=0&&element.getNbJoueurs()%nbPoules!=0)
				{
					JOptionPane.showMessageDialog(
							getContentPane(),
							"Le nombre de joueur d'un tableau doit, pour cette formule,<br> être un multiple de "+nbPoules,
							"Erreur !",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			formulesList.set(index, getFormule());
			updateFormulesList();			
		}
	}

	private boolean saveFormules()
	{
		System.out.println(formulesList.size());
		competition.updateDescriptionAuto(formulesList);
		return competition.saveFormulesAuto();
	}


	private void clearFields()
	{
		jtfFormuleName.setText("Nouvelle Formule");
		jtfNbPoules.setText("0");	
		((SortiePouleTableModel)jtSortiePoule.getModel()).clear();	
	}

	private void addFormule()
	{
		clearFields();
		updateFormulesList();	
		updateSortiePouleList(getFormule());					
		DescriptionTableau formule = getFormule();
		if(formule!=null)
		{
			formulesList.add(formule);
			updateFormulesList();
			jlFormules.setSelectedIndex(formulesList.size()-1);
		}								
	}

	public void tableChanged(TableModelEvent arg0) 
	{
		if(arg0.getType()==TableModelEvent.UPDATE)
		{
			if(arg0.getColumn()==0)
			{
				DescriptionTableau desc = (DescriptionTableau)formulesList.get(formulesList.size()-1);
				int row = arg0.getLastRow();
				SortiePoule s = (SortiePoule)desc.getSortiePouleList().get(row);
				s.setNbJoueurs(((Integer)((SortiePouleTableModel)jtSortiePoule.getModel()).getValueAt(row,0)).intValue());
			}
		}
	}

}
