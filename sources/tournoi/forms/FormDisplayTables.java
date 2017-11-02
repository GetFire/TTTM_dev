/*
 *
 */
package tournoi.forms;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import myUtils.ControledDocument;
import org.apache.log4j.Logger;

import tournoi.Competition;


/**
 *
 */
public class FormDisplayTables extends JInternalFrame implements Observer
{

	/**
     * 
     */
    private static final long serialVersionUID = 1L;

    private javax.swing.JPanel jContentPane = null;

	private javax.swing.JList jListFreeTables = null;
	private javax.swing.JButton down = null;
	private javax.swing.JButton up = null;
	private javax.swing.JLabel jLabel = null;
    private JLabel lblNbTables = new JLabel("Количество таблиц : ");
    private JTextField tfNbTables = new JTextField(new ControledDocument(5, false, true), "16", 4);
    
	private Competition competition = null; 
	
	private Logger logger = Logger.getLogger(FormDisplayTables.class);
	
	/**
	 * This is the default constructor
	 */
	public FormDisplayTables(Competition competition)
	{
		super("управление таблицей");
		this.competition = competition;
		competition.addObserver(this);
		initialize();
		displayFreeTable();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize()
	{
		this.setSize(250, 350);
		this.setContentPane(getJContentPane());	
		this.setResizable(true);
		this.setMaximizable(true);	
		this.setIconifiable(true);
        tfNbTables.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                changeNbTablesDisponibles(e);
            }
        });
	}
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane()
	{
		if (jContentPane == null)
		{
			jContentPane = new javax.swing.JPanel();
			java.awt.GridBagConstraints consGridBagConstraints1 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints3 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints4 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints7 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints8 = new java.awt.GridBagConstraints();
            java.awt.GridBagConstraints consGridBagConstraints2 = new java.awt.GridBagConstraints();
            java.awt.GridBagConstraints consGridBagConstraints5 = new java.awt.GridBagConstraints();
            
            consGridBagConstraints2.gridx = 0;
            consGridBagConstraints2.gridy = 0;
            consGridBagConstraints2.insets = new java.awt.Insets(2,2,2,2);
            consGridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTHEAST;
            consGridBagConstraints2.gridwidth=2;
            
            consGridBagConstraints5.gridx = 2;
            consGridBagConstraints5.gridy = 0;
            consGridBagConstraints5.insets = new java.awt.Insets(2,2,2,2);
            consGridBagConstraints5.anchor = java.awt.GridBagConstraints.NORTHEAST;
            
            
            
			consGridBagConstraints3.gridx = 0;
			consGridBagConstraints3.gridy = 3;
			consGridBagConstraints3.insets = new java.awt.Insets(2,2,2,2);
			consGridBagConstraints3.anchor = java.awt.GridBagConstraints.NORTH;
			consGridBagConstraints3.weighty = 0.3D;
			consGridBagConstraints4.gridx = 0;
			consGridBagConstraints4.gridy = 2;
			consGridBagConstraints4.insets = new java.awt.Insets(2,2,2,2);
			consGridBagConstraints4.anchor = java.awt.GridBagConstraints.SOUTH;
			consGridBagConstraints4.weighty = 0.3D;									
			consGridBagConstraints7.gridx = 1;
			consGridBagConstraints7.gridy = 1;
			consGridBagConstraints7.insets = new java.awt.Insets(2,2,2,2);
			consGridBagConstraints1.gridx = 1;
			consGridBagConstraints1.gridy = 2;
			consGridBagConstraints1.gridwidth = 1;
			consGridBagConstraints1.gridheight = 2;
			consGridBagConstraints1.weightx = 1.0;
			consGridBagConstraints1.weighty = 1.0;
			consGridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
			consGridBagConstraints1.ipady = 0;
			consGridBagConstraints1.gridwidth=3;
			
			jContentPane.setLayout(new java.awt.GridBagLayout());
			JScrollPane scroll = new JScrollPane(getJList());
			jContentPane.add(scroll, consGridBagConstraints1);
			jContentPane.add(getJButton1(), consGridBagConstraints3);
			jContentPane.add(getJButton3(), consGridBagConstraints4);
			jContentPane.add(getJLabel(), consGridBagConstraints7);
            jContentPane.add(lblNbTables, consGridBagConstraints2);
            jContentPane.add(tfNbTables, consGridBagConstraints5);
		}
		return jContentPane;
	}
	/**
	 * This method initializes jList
	 * 
	 * @return javax.swing.JList
	 */
	private javax.swing.JList getJList() {
		if(jListFreeTables == null) {
			jListFreeTables = new javax.swing.JList(new DefaultListModel());
		}
		return jListFreeTables;
	}
	/**
	 * This method initializes jButton1
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJButton1() {
		if(down == null) {
			down = new javax.swing.JButton();
			URL location;
			location = java.lang.ClassLoader.getSystemResource("fleche_bas.gif");
			down.setIcon(new ImageIcon(location));
			
			down.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					downPerformed(e);

				}
			});		
		}
		return down;
	}
	/**
	 * This method initializes jButton3
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJButton3() {
		if(up == null) {
			up = new javax.swing.JButton();
			URL location;
			location = java.lang.ClassLoader.getSystemResource("fleche_haut.gif");
			up.setIcon(new ImageIcon(location));
			
			up.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					upPerformed(e);

				}
			});
		}
		return up;
	}
	/**
	 * This method initializes jLabel
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel() {
		if(jLabel == null) {
			jLabel = new javax.swing.JLabel();
			jLabel.setText("Бесплатные таблицы :");
		}
		return jLabel;
	}
	
	private void displayFreeTable()
	{
		if(jListFreeTables != null)
		{
			((DefaultListModel)jListFreeTables.getModel()).removeAllElements();
			for (Iterator iter = Competition.getFreeTablesList().iterator(); iter.hasNext();)
			{
				Integer element = (Integer) iter.next();
				((DefaultListModel)jListFreeTables.getModel()).insertElementAt(element,0);
			}
		}
	}
	
    private void changeNbTablesDisponibles(ActionEvent e)
    {
        Competition.changeNbFreeTableList(new Integer(tfNbTables.getText()).intValue());
        displayFreeTable();
    }
    
    
	public void upPerformed(ActionEvent e)
	{		
			
		if(jListFreeTables.getSelectedIndex()!=-1)
		{
			if(jListFreeTables.getSelectionModel().getAnchorSelectionIndex()==0) return;
			int indices[] = jListFreeTables.getSelectedIndices();
			java.util.List list = Arrays.asList(jListFreeTables.getSelectedValues());
			for (Iterator iter = list.iterator(); iter.hasNext();)
			{
				Integer element = (Integer) iter.next();
				int index = Competition.getFreeTablesList().indexOf(element);
				if((index+1)<Competition.getFreeTablesList().size())
				{
					Competition.getFreeTablesList().remove(index);
					Competition.getFreeTablesList().add(index+1,element);
					jListFreeTables.setSelectedIndex(index+1);					
				}
				
			}
			displayFreeTable();
			if(indices!=null && indices.length>0)
			{
				int start = indices[0]-1;
				if(start<0) start=0;
				int end = indices[indices.length-1]-1;
				jListFreeTables.setSelectionInterval(start,end);
			}
				
		}
		
	}
	
	public void downPerformed(ActionEvent e)
	{
		if(jListFreeTables.getSelectedIndex()!=-1)
		{
			int start = 0;
			int end = 0;
			int[] indices = jListFreeTables.getSelectedIndices();
			if(jListFreeTables.getSelectionModel().getLeadSelectionIndex()==Competition.getFreeTablesList().size()-1) return;
			java.util.List list = Arrays.asList(jListFreeTables.getSelectedValues());			
			if(indices!=null && indices.length>0)
			{
				start = jListFreeTables.getSelectionModel().getAnchorSelectionIndex()+1;				
				end = jListFreeTables.getSelectionModel().getLeadSelectionIndex()+1;
				if(end>=Competition.getFreeTablesList().size()) end=(Competition.getFreeTablesList().size()-1);
				if(start>=Competition.getFreeTablesList().size()) start=(Competition.getFreeTablesList().size()-1);						
			}
			
			Collections.sort(list, Collections.reverseOrder());
			for (Iterator iter = list.iterator(); iter.hasNext();)
			{
				Integer element = (Integer) iter.next();
				int index = Competition.getFreeTablesList().indexOf(element);
				if((index-1)>=0)
				{
					Competition.getFreeTablesList().remove(index);
					Competition.getFreeTablesList().add(index-1,element);										
				}
				
			}
			displayFreeTable();
			jListFreeTables.setSelectionInterval(start,end);
		}
		
	}
	public void update(Observable o, Object arg)
	{
		displayFreeTable();
	}
	
	
	public void repaint() 
	{	
		displayFreeTable();
		super.repaint();		
	}
}
