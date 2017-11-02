/*
 *
 */
package tournoi.component;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import java.awt.Dimension;
import java.awt.GridLayout;

import myUtils.*;


/**
 *  TableSorterDemo is like TableDemo, except that it inserts a custom model --
 *  a sorter -- between the table and its data model. It also has column tool
 *  tips.
 *
 *@author     captainpaf
 *@created    26 avril 2004
 */
public class JTableSorted extends JPanel
{
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    JTable table;
	TableSorter sorter;
	/**
	 *  Constructor for the TableSorterDemo object
	 */
	public JTableSorted(AbstractTableModel model)
	{
		super(new GridLayout(1, 0));
		sorter = new TableSorter(model);
		table = new JTable(sorter);
		sorter.setTableHeader(table.getTableHeader());
		table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		table.getTableHeader().setToolTipText(
				"Click to specify sorting; Control-Click to specify secondary sorting");

		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane);
	}
	
	public TableSorter getSorter()
	{
		return sorter;
	}
	
	public JTable getJTable()
	{
		return table;
	}
}
