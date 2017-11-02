/*
 *
 */
package tournoi.component;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import tournoi.Joueur;

/**
 *  Permet la repr�sentation des r�sultats d'un tournoi
 *
 *@author     captainpaf
 *@created    26 avril 2004
 */
public class ResultatTableModel extends AbstractTableModel
{
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String[] columnNames = {"Место",
			"Имя",
			"Фамилия",
			"Классификация",
			"CКатегория",
			"Пол"};
	private Object[][] data = null;

	public ResultatTableModel(ArrayList joueurs)
	{
		super();
		setResultats(joueurs);
	}

	/**
	 * permet de modifier les donn�e de la table
	 * @param joueurs la liste des joueurs du vainqueur au dernier
	 */
	public void setResultats(ArrayList joueurs)
	{
		data = new Object[joueurs.size()][columnNames.length];
		int place = 1;
		for (int row=0; row<joueurs.size(); row++)
		{			
			Joueur joueur = (Joueur)joueurs.get(row);
			if(!Joueur.EMPTY_JOUEUR.equals(joueur)&&joueur!=null)
			{
				setValueAt(new Integer(place),row,0);
				setValueAt(joueur.getNom(),row,1);
				setValueAt(joueur.getPrenom(),row,2);
				if(joueur.getClassement()==null)
				{
					setValueAt("",row,3);
				}
				else
				{
					setValueAt(joueur.getClassement().toString(),row,3);
				}
				setValueAt(joueur.getCategorie().toString(),row,4);
				if(joueur.isMasculin())
				{
					setValueAt("мужчина",row,5);
				}
				else
				{
					setValueAt("женщина",row,5);
				}				
				place++;
			}
		}
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
		return data.length;
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
		return data[row][col];
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
		return false;
	}


	/*
	 *  Don't need to implement this method unless your table's
	 *  data can change.
	 */
	/**
	 *  Sets the valueAt attribute of the MyTableModel object
	 *
	 *@param  value  The new valueAt value
	 *@param  row    The new valueAt value
	 *@param  col    The new valueAt value
	 */
	public void setValueAt(Object value, int row, int col)
	{
		data[row][col] = value;
		fireTableCellUpdated(row, col);
	}
}