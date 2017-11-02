/*
 *
 */
package tournoi.component;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import tournoi.Joueur;
import tournoi.OrdreMatch;

/**
 *  Permet la repr�sentation des r�sultats d'un tournoi
 *
 *@author     captainpaf
 *@created    26 avril 2004
 */
public class OrdreMatchTableModel extends AbstractTableModel
{
	/**
     * 
     */
    private static final long serialVersionUID = 1L;


    private String[] columnNames = {
			"Партия №",
			"Соперник 1",
			"Соперник 2"};

	
	private ArrayList datas = null;

	public OrdreMatchTableModel()
	{
		
		super();
		datas = new ArrayList();
		
	}
	
	public void clear()
	{
		datas.clear();
	}
	
	public ArrayList getOrdreMatch()
	{
		return datas;
	}
	
	
	public Joueur getOrdreMatch(int index)
	{
		return (Joueur)datas.get(index);
	}
	
	
	public boolean addOrdreMatch(Joueur joueur)
	{
		if(!datas.contains(joueur))
		{
			datas.add(joueur);
			return true;
		}
		return false;
	}


	public void setOrdreMatch(int row, OrdreMatch ordreMatch)
	{
		ordreMatch.setNumMatch(row+1);
		datas.set(row,ordreMatch);
	}

	public void removeOrdreMatch(int row)
	{
		datas.remove(row);
	}


	/**
	 *  Gets the columnCount attribute of the MyTableModel object
	 *
	 *@return    The columnCount value
	 */
	public int getColumnCount()
	{
		return columnNames.length;
	}


	/**
	 *  Gets the rowCount attribute of the MyTableModel object
	 *
	 *@return    The rowCount value
	 */
	public int getRowCount()
	{
		return datas.size();
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


	/**
	 *  Gets the valueAt attribute of the MyTableModel object
	 *
	 *@param  row  Description of the Parameter
	 *@param  col  Description of the Parameter
	 *@return      The valueAt value
	 */
	public Object getValueAt(int row, int col)
	{
		OrdreMatch ordreMatch = (OrdreMatch)datas.get(row);
		switch(col)
		{			
			case 0 : // match n�			
				return ""+(ordreMatch.getNumMatch());
			case 1 : // adversaire 1
				return ""+ordreMatch.getAdversaire1();
			case 2 : // adversaire 2			
				return ""+ordreMatch.getAdversaire2();
			default :
				return "";			
		}
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
		if(col>0) return true;
		return false;
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
		datas.set(row,value);
		fireTableCellUpdated(row, col);
	}
}
