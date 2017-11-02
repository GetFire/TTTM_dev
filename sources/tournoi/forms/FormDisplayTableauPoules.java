
package tournoi.forms;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.apache.log4j.Logger;
import tournoi.*;
import tournoi.component.*;
import tournoi.exception.TournoiException;

/**
 * La fen�tre qui affiche les tableau avec poules.
 */
public class FormDisplayTableauPoules extends JInternalFrame implements Observer, MouseListener
{
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Logger logger = Logger.getLogger(FormDisplayTableauPoules.class);
	/** le tableau a afficher */
	private TableauPoules tableau = null;	
	private JTabbedPane tab = new JTabbedPane();
	private boolean tableauxIsStarted = false;
	private boolean tableauxIsCompleted = false;
	private JButton bPrintPoules = new JButton("Imprimer les poules");
	private JButton bGenererTableau = new JButton("См. Таблицу (x)");
    private JButton bUpdateFormule = new JButton("Измените формулу");
	private JPanel panelPoules = null;
	private PanelPoule panelPoule = null; 
	private JPanel containPoule = null;
    private Competition competition = null;
	
	private int nbMaxJoueursInPoule = 0;
	
	private TableauxAndFrame tableauxAndResultat = new TableauxAndFrame();
		
	public void updateJTableau()
	{
		for (int i = 0; i < tableau.getTableaux().size(); i++)
		{		
			boolean result = ((TableauSimple)tableau.getTableaux().get(i)).creerPremierTour();			
			showTableau(((TableauSimple)tableau.getTableaux().get(i)));						
		}
	}	
	
	private void showTableau(TableauSimple tableau)
	{			
		JTableau jTournoi;
		if(tableau.getType().equals(TableauKO.TYPE))
		{
			jTournoi = new JTableauKO((TableauKO)tableau);			
		}
		else if(tableau.getType().equals(TableauIntegral.TYPE))
		{
			jTournoi = new JTableauIntegral((TableauIntegral)tableau);			
		}			
		else if(tableau.getType().equals(TableauDoubleKO.TYPE))
		{
			jTournoi = new JTableauDoubleKO((TableauDoubleKO)tableau);
		}
		else
		{
			jTournoi = null;
		}
		tableauxAndResultat.addTableauAndFrame(tableau,null);
		tableau.addObserver(this);
		jTournoi.setLocation(0,0);										
		JScrollPane scrollPane = new JScrollPane(jTournoi);
		JPanel panelTab = new JPanel(new BorderLayout());		
		JPanel panelGrid = new JPanel(new GridLayout(1,2));
			
		JButton print = new JButton("Печатные матчи");
		JButton printAll = new JButton("Печать таблицы");
		panelGrid.add(print);
		panelGrid.add(printAll);
			
		print.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				printClicked(arg0);

			}
		});		
		printAll.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				printAllClicked(arg0);

			}
		});							
			
		panelTab.add(panelGrid, BorderLayout.SOUTH);
		panelTab.add(scrollPane, BorderLayout.CENTER);		        
		tab.add(panelTab, jTournoi.getName());
		jTournoi.addJNodesListener(new JNodesListener()
		{
			public void nodeClicked(JNode node)
			{
				tableauMatchCliked(node);					
			}
		});		
	}

	public void tableauMatchCliked(JNode node)
	{			
		FormMatch fMatch = new FormMatch(node.getMatch());
		tab.add("Матч", fMatch);
		fMatch.show();
	}
	
	private JPanel getPanelPoules()
	{				
		int poulesSize = tableau.getPoules().size(); 
		int row = ((poulesSize%6)==0)?(poulesSize/6):((poulesSize/6)+1);
		int column = 6;
		
		JPanel panel = new JPanel(new GridLayout(row,column,2,2));
		panel.removeAll();		
		for (int i = 0; i < poulesSize; i++)
		{
			if(nbMaxJoueursInPoule<((Poule)tableau.getPoules().get(i)).getJoueurs().size())
			{
				nbMaxJoueursInPoule = ((Poule)tableau.getPoules().get(i)).getJoueurs().size();
			}
			JPoule jPoule = new JPoule((Poule)tableau.getPoules().get(i));					
			jPoule.addMouseListener(this); 
			panel.add(jPoule);
		}
		return panel;		
	}
	
	private JPanel getPanelButtons()
	{
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.EAST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.insets = new Insets(2, 2, 2, 2);
		panel.add(bGenererTableau, constraints);
		constraints.gridx = 1;
		constraints.anchor = GridBagConstraints.WEST;
		panel.add(bPrintPoules, constraints);        
        constraints.gridx = 2;
        constraints.anchor = GridBagConstraints.WEST;
        panel.add(bUpdateFormule, constraints);
        bPrintPoules.setToolTipText("Imprime toutes les poules du tableau.");
        bUpdateFormule.setToolTipText("Измените формулу в этой таблице.");
        bGenererTableau.setToolTipText("Покажите таблицы в конце куры.");
		return panel;
	}
	
	private JPanel getMainPoulePanel()
	{
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.insets = new Insets(2, 2, 2, 2);
		panelPoules = getPanelPoules();
		panel.add(panelPoules, constraints);
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.gridy = 1;
		constraints.weighty = 0.0;	
		constraints.weightx = 0.0;	
		panel.add(getPanelButtons(), constraints);
		constraints.gridy = 2;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		panelPoule = new PanelPoule((Poule)tableau.getPoules().get(0), tableau);
		panelPoule.setVisible(true);
		panel.add(panelPoule, constraints);		
		return panel;
	}
	
	/**
	 * constructeur par d�faut
	 * @param tableau le tableau a afficher
	 */
	public FormDisplayTableauPoules(Competition competition, TableauPoules tableau)
	{		
		super(tableau.getName(), true, true);
		this.tableau = tableau;		
        this.competition=competition;
		tab = new JTabbedPane();
		containPoule = getMainPoulePanel();	
		containPoule.setSize(200,200);		
		JScrollPane scrollPane = new JScrollPane(containPoule);
		tab.add("poules", scrollPane);				
		getContentPane().add(tab, BorderLayout.CENTER);												
		bGenererTableau.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				genererTableau();

			}
		});		

        bUpdateFormule.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent arg0)
                    {
                        updateFormule(arg0);

                    }
                });     

		tab.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent arg0)
			{
				tabStateChanged(arg0);

			}
		});
		bGenererTableau.setFocusable(false);
		bPrintPoules.setFocusable(false);
		bPrintPoules.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				bPrintPoulesClicked(arg0);

			}
		});		
		if(tableau.isStarted()&&allPoulesAreTerminated())
		{
			for (int i = 0; i < tableau.getTableaux().size(); i++)
			{		
				for (Iterator iter = ((TableauSimple)tableau.getTableaux().get(i)).joueurs.iterator(); iter.hasNext();)
				{				
					Joueur element = (Joueur) iter.next();
				}			
				showTableau(((TableauSimple)tableau.getTableaux().get(i)));						
			}
			tableauxIsCompleted=true;
		}
		tableau.setStarted(true);
		setResizable(true);	
		setIconifiable(true);
		setMaximizable(true);
						
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(dim.width/2,dim.height/2);
		//=============================			
		//getContentPane().add(BorderLayout.CENTER, panel);
		try
		{
			setSelected(true);
		}
		catch(Exception e)
		{
		}
		setVisible(true);
		
	}
	
	private boolean allPoulesAreTerminated()
	{
		//== toutes les poules sont termin�es ? ==		
		for (Iterator iter = tableau.getPoules().iterator(); iter.hasNext();)
		{			
			Poule poule = (Poule) iter.next();
			if(!poule.isTerminated())
			{
				return false;		
			}
		}	
		return true;	
	}
	
	public void genererTableau()
	{
		//== toutes les poules sont termin�es ? ==
		if(!allPoulesAreTerminated())		
		{
			if(tableauxIsCompleted)
			{
				for (int i=0; i<tableau.getTableaux().size();i++)
				{						
					//tab.remove(tab.getTabCount()-1);
					tableauxAndResultat.hideResultat((TableauSimple)tableau.getTableaux().get(i));	
				}
				tableauxIsCompleted = false;				
				tableau.initTableaux();
			}				
			return;		
		}
		if(!tableauxIsCompleted)
		{	
			tableau.clearTableaux();	
			tableau.completeTableaux();	
			tableauxIsCompleted=true;
			updateJTableau();
		}			
	}
	
	public void tabStateChanged(ChangeEvent changeEvent)
	{
		//== toutes les poules sont termin�es ? ==
		if(!allPoulesAreTerminated())		
		{
			if(tableauxIsCompleted)
			{
				for (int i=0; i<tableau.getTableaux().size();i++)
				{						
					//tab.remove(tab.getTabCount()-1);
					tableauxAndResultat.hideResultat((TableauSimple)tableau.getTableaux().get(i));	
				}
				tableauxIsCompleted = false;
				tableau.initTableaux();
			}				
			return;		
		}		
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent mouseEvent)
	{	
		containPoule.remove(panelPoule);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.insets = new Insets(2, 2, 2, 2);
				
		JPoule jPoule = (JPoule)mouseEvent.getSource();
		panelPoule = new PanelPoule(jPoule.getPoule(),tableau);
		containPoule.add(panelPoule, constraints);		
		repaint();						
	}

	private JTableau getSelectedJTableau()
	{
		JScrollPane scroll = (JScrollPane)tab.getSelectedComponent().getComponentAt(0,0);
		JTableau tableau = (JTableau)scroll.getViewport().getView();		
		return tableau;
	}

	/**
	 * impression des matchs s�lectionn�s
	 * @param arg0
	 */
	public void printClicked(ActionEvent arg0)
	{
		ArrayList temp = getSelectedJTableau().getSelectedJNodes();
		PrinterJob printer = FormDesktop.printer;
		PrintingMatch printingMatch = new PrintingMatch();		
		for (Iterator iter = temp.iterator(); iter.hasNext();)
		{
			JNode element = (JNode) iter.next();
			if(element.isSelected()&&element.getNode()!=null)
			{
				String tabName = tableau.getName()+" - "+getSelectedJTableau().tableau.getName();
				Match match = element.getNode().getMatch();				
				printingMatch.addPrintedMatch(new PrintedMatch(match, tabName));
			}					
		}		
		printer.setPrintable(printingMatch,FormDesktop.pageFormat);	
		try
		{
			printer.print();						
		}
		catch(Exception e)
		{
			logger.error(e);
			JOptionPane.showMessageDialog(this,"Ошибка при печати: "+e.getMessage(),"Ошибка",JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * impression du tableau
	 * @param arg0
	 */
	public void printAllClicked(ActionEvent arg0)
	{
		PrinterJob pj = FormDesktop.printer;	
		pj.setPrintable(getSelectedJTableau(),FormDesktop.pageFormat);
		if(Competition.isPrintDialogue())
		{
			pj.printDialog();
		}
		
		try
		{
			pj.print();
		}
		catch (Exception exp)
		{
			logger.error(exp);
		}
	}

    public void updateFormule(ActionEvent arg0)
    {
        Object[] formules = competition.getFormulesAuto().toArray();
        DescriptionTableau desc = (DescriptionTableau)JOptionPane.showInputDialog(getContentPane(),"Новая формула : ","Изменение формулы",JOptionPane.QUESTION_MESSAGE,null,formules,null);
        if(desc!=null)
        {
            try
            {
                competition.updateDescriptionInTableau(desc,tableau);
                if(tab.getComponentCount()>1)
                {
                    for(int x=tab.getComponentCount()-1; x>0; x--)
                    {
                       tab.remove(x);
                    }
                    tableau.clearTableaux();    
                    tableau.completeTableaux(); 
                    tableauxIsCompleted=true;
                    updateJTableau();
                }
            }
            catch(TournoiException exp)
            {
                JOptionPane.showMessageDialog(getContentPane(),exp.getMessage(),"Ошибка",JOptionPane.ERROR_MESSAGE);
                return;
            }         
        }
        
    }
    
	public void bPrintPoulesClicked(ActionEvent arg0)
	{
		PrinterJob printer = FormDesktop.printer;
		PrintingPoule printingPoule = new PrintingPoule();
		for (Iterator iter = tableau.getPoules().iterator(); iter.hasNext();)
		{
			Poule poule = (Poule) iter.next();
			printingPoule.addPrintedPoule(new PrintedPoule(poule, tableau.getName()));			
		}					
		printer.setPrintable(printingPoule, FormDesktop.pageFormat);
		try
		{
			printer.print();						
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error(e);
			JOptionPane.showMessageDialog(this,"Ошибка при печати: "+e.getMessage(),"Ошибка",JOptionPane.ERROR_MESSAGE);
		}
	}
		
	public void update(Observable arg0, Object arg1)
	{
		if(((TableauSimple)arg0).isTerminated())
		{
			tableauxAndResultat.hideResultat((TableauSimple)arg0);
			tableauxAndResultat.showResultat((TableauSimple)arg0);
			tab.add(tableauxAndResultat.getFrameResultat((TableauSimple)arg0),tab.getTabCount());
			tab.setTitleAt(tab.getTabCount()-1,"Результат "+((TableauSimple)arg0).getName());
			
		}
		else
		{
			tableauxAndResultat.hideResultat((TableauSimple)arg0);
		}				
	}
	
	public void mousePressed(MouseEvent mouseEvent)
	{
		int onmask = MouseEvent.BUTTON1_DOWN_MASK;
		if ((mouseEvent.getModifiersEx() & (onmask)) == onmask) 
		{		
			containPoule.remove(panelPoule);
			GridBagConstraints constraints = new GridBagConstraints();
			constraints.gridx = 0;
			constraints.gridy = 2;
			constraints.gridwidth = 1;
			constraints.gridheight = 1;
			constraints.weightx = 1.0;
			constraints.weighty = 1.0;
			constraints.anchor = GridBagConstraints.CENTER;
			constraints.fill = GridBagConstraints.BOTH;
			constraints.ipadx = 0;
			constraints.ipady = 0;
			constraints.insets = new Insets(2, 2, 2, 2);				
			JPoule jPoule = (JPoule)mouseEvent.getSource();
			panelPoule = new PanelPoule(jPoule.getPoule(),tableau);
			containPoule.add(panelPoule, constraints);		
			repaint();
		}								
	}
	
	public void repaint()
	{		
		super.repaint();
		boolean repaint = false;
		if(panelPoule!=null)
		{			
			int poulesSize = tableau.getPoules().size(); 
			for (int i = 0; i < poulesSize; i++)
			{
				if(nbMaxJoueursInPoule<((Poule)tableau.getPoules().get(i)).getJoueurs().size())
				{
					repaint = true;
					nbMaxJoueursInPoule = ((Poule)tableau.getPoules().get(i)).getJoueurs().size();
				}				
			}
			if(repaint)
			{
				panelPoules.removeAll();
				poulesSize = tableau.getPoules().size(); 
				int row = ((poulesSize%6)==0)?(poulesSize/6):((poulesSize/6)+1);
				int column = 6;							
				for (int i = 0; i < poulesSize; i++)
				{
					JPoule jPoule = new JPoule((Poule)tableau.getPoules().get(i));					
					jPoule.addMouseListener(this); 
					panelPoules.add(jPoule);
				}									
			}									
		}		
	}



	public void mouseReleased(MouseEvent arg0){
	}


	public void mouseEntered(MouseEvent arg0){		
	}


	public void mouseExited(MouseEvent arg0){		
	}	
	
   
    
	/**
	 * permet de retrouver la frame servant � afficher un tableau
	 *
	 */
	private class TableauxAndFrame
	{
		ArrayList tableaux = new ArrayList();
		ArrayList frames = new ArrayList();
		
		public void addTableauAndFrame(TableauSimple tableau, FormDisplayResultat form)
		{
			tableaux.add(tableau);
			frames.add(form);
			try{form.setClosed(true);}catch (Exception e){
			}
		}
		
		public boolean showResultat(TableauSimple tableau)
		{
			int index = tableaux.indexOf(tableau);
			if(index!=-1)
			{
				frames.set(index, new FormDisplayResultat(tableau.getResultats(), tableau.getName()));
				return true;
			}
			return false;
		}
		
		public FormDisplayResultat getFrameResultat(TableauSimple tableau)
		{
			int index = tableaux.indexOf(tableau);
			if(index!=-1)
			{				
				return (FormDisplayResultat)frames.get(index);
			}
			return null;
			
		}
		
		public boolean hideResultat(TableauSimple tableau)
		{
			int index = tableaux.indexOf(tableau);
			if(index!=-1)
			{
				try
				{
					((FormDisplayResultat)frames.get(index)).setClosed(true);
					return true;
				} catch (Exception e){return false;}
			}
			return false;
		}
	}
}
