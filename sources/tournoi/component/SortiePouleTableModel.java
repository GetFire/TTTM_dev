/*
 *
 */
package tournoi.component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import tournoi.SortiePoule;

/**
 *
 */
public class SortiePouleTableModel extends AbstractTableModel
{
	/**
     * 
     */
    
    private static final long serialVersionUID = 1L;
    private String[] columnNames = {"Nombre de joueurs","Type de tableau"};
	private List datas = null;
			
	public SortiePouleTableModel()
	{
		super();
		datas = new ArrayList();
	}

	public int getRowCount()
	{
		return datas.size();
	}

	public int getColumnCount()
	{
		return columnNames.length;
	}
	
	/**
	 *  Gets the columnName attribute of the MyTableModel object
	 *
	 *@param  col  Description of the Parameter
	 *@return      The columnName value
	 */
	public String getColumnName(int col)
	{
		return columnNames[col];
	}
	
	public Object getValueAt(int row, int col)
	{	
		Object[] tab = (Object[])datas.get(row);	
		return tab[col];
	}
	/*
	 *  JTable uses this method to determine the default renderer/
	 *  editor for each cell.  If we didn't implement this method,
	 *  then the last column would contain text ("true"/"false"),
	 *  rather than a check box.
	 */
	/**
	 *  Gets the columnClass attribute of the MyTableModel object
	 *
	 *@param  c  Description of the Parameter
	 *@return    The columnClass value
	 */
	public Class getColumnClass(int c)
	{
		return getValueAt(0, c).getClass();
	}


	/**
	 *  Gets the cellEditable attribute of the MyTableModel object
	 *
	 *@param  row  line
	 *@param  col  column
	 *@return      The cellEditable value
	 */
	public boolean isCellEditable(int row, int col)
	{
		return true;		
	}


	/**
	 *  Sets the valueAt attribute of the MyTableModel object
	 *
	 *@param  value  The new valueAt value
	 *@param  row    The new valueAt value
	 *@param  col    The new valueAt value
	 */
	public void setValueAt(Object value, int row, int col)
	{
		((Object[])datas.get(row))[col]=value;
		fireTableCellUpdated(row, col);
	}
	public void addSortiePoule(SortiePoule sortiePoule)
	{		
		datas.add(new Object[]{new Integer(sortiePoule.getNbJoueurs()),sortiePoule.getType()});
	}
	
	public ArrayList getSortiePouleList()
	{
		ArrayList temp = new ArrayList();
		int index=1;
		for (Iterator iter = datas.iterator(); iter.hasNext();)
		{
			SortiePoule sortie = new SortiePoule();
			Object[] element = (Object[]) iter.next();
			sortie.setNbJoueurs(((Integer)element[0]).intValue());
			sortie.setType(((String)element[1]));
			sortie.setRefTableau("Tableau "+index);
			temp.add(sortie);
			index++;
		}
		return temp;
	}

	public void clear()
	{
		datas.clear();
	}

}
