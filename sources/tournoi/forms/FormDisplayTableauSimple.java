
package tournoi.forms;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;
import tournoi.*;
import tournoi.component.*;

/**
 * La fen�tre qui affiche les tableau sans poule.
 */

public class FormDisplayTableauSimple extends JInternalFrame implements Observer
{
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(FormDisplayTableauSimple.class);
	/** le tableau a afficher */
	private TableauSimple tableau = null;	
	private JTabbedPane tab = new JTabbedPane();
	private JTableau jTournoi = null;
	private JButton print = new JButton("Печатные матчи");
	private JButton printAll = new JButton("Печать таблицы");
	
	private FormDisplayResultat fResultat = null;
	
	private boolean resultatIsShowing = false;
	
	/**
	 * constructeur par d�faut
	 * @param tableau le tableau a afficher
	 */
	public FormDisplayTableauSimple(TableauSimple tableau)
	{
		super(tableau.getName(), true, true);
		this.tableau = tableau;		
		this.tableau.addObserver(this);
		
		if(tableau.getType().equals(TableauKO.TYPE))
		{
			jTournoi = new JTableauKO((TableauKO)tableau);
		}
		else if(tableau.getType().equals(TableauDoubleKO.TYPE))
		{
			jTournoi = new JTableauDoubleKO((TableauDoubleKO)tableau);
		}
		else if(tableau.getType().equals(TableauIntegral.TYPE))
		{
			jTournoi = new JTableauIntegral((TableauIntegral)tableau);
		}
		
		getContentPane().setLayout(new BorderLayout());		
		jTournoi.setLocation(0,0);
		JScrollPane scrollPane = new JScrollPane(jTournoi);				
		jTournoi.addJNodesListener(new JNodesListener()
		{
			public void nodeClicked(JNode node)
			{
				tableauMatchCliked(node);

			}
		});
		
		tab.add(jTournoi.getName(), scrollPane);
		
		//tab.add(jTournoi);			
		getContentPane().add(tab, BorderLayout.CENTER);
		JPanel panelGrid = new JPanel(new GridLayout(1,2));
		panelGrid.add(print);
		panelGrid.add(printAll);
		getContentPane().add(panelGrid, BorderLayout.SOUTH);
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
	
	/**
	 * impression des matchs s�lectionn�s
	 * @param arg0
	 */
	public void printClicked(ActionEvent arg0)
	{
		PrinterJob printer = FormDesktop.printer;
		PrintingMatch printingMatch = new PrintingMatch();		

		ArrayList temp = jTournoi.getSelectedJNodes();		
		for (Iterator iter = temp.iterator(); iter.hasNext();)
		{
			JNode element = (JNode) iter.next();
			if(element.isSelected()&&element.getNode()!=null)
			{
				printingMatch.addPrintedMatch(new PrintedMatch(element.getNode().getMatch(),tableau.getName()));										
			}
		}
		printer.setPrintable(printingMatch,FormDesktop.pageFormat);
		try
		{
			printer.print();
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(this,"Ошибка при печати : "+e.getMessage(),"Ошибка",JOptionPane.ERROR_MESSAGE);
			logger.error(e);
		}
	}
	
	/**
	 * impression du tableau
	 * @param arg0
	 */
	public void printAllClicked(ActionEvent arg0)
	{
		PrinterJob pj = FormDesktop.printer;	
		pj.setPrintable(jTournoi);
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
	
	
	public void tableauMatchCliked(JNode node)
	{			
		if(node.getMatch()!=null)
		{	
			FormMatch fMatch = new FormMatch(node.getMatch());
			tab.add("Match", fMatch);
			fMatch.show();
		}
	}

	public void update(Observable arg0, Object arg1)
	{
		if(tableau.isTerminated())
		{
			if(!resultatIsShowing)
			{
				resultatIsShowing=true;
				fResultat = new FormDisplayResultat(tableau.getResultats(),"");
				tab.add("Результат", fResultat);
				fResultat.show();

			}			
		}		
		else if(resultatIsShowing)
		{
			//fResultat.setVisible(false);
			try
			{
			fResultat.setClosed(true);
			}catch(Exception e){
			}
			resultatIsShowing = false;
		}
	}	
}
