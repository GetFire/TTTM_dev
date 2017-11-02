/*
 *
 */
package tournoi.component;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import tournoi.Joueur;

/**
 *  Permet la représentation des résultats d'un tournoi
 *
 *@author     captainpaf
 *@created    26 avril 2004
 */
public class JoueursTableModel extends AbstractTableModel
{
	/**
     * 
     */
    private static final long serialVersionUID = 1L;



    private String[] columnNames = {
			"Nom",
			"Prénom",
			"Classement",
			"Catégorie",
			"Sexe",
			"Club",
			"Présent",
			"Participation"};
	
	
			
	//private Object[][] data = null;
	
	private ArrayList datas = null;
	//private ArrayList joueurs = null;

	public JoueursTableModel()
	{
		
		super();
		datas = new ArrayList();
		
	}
	
	public void clear()
	{
		datas.clear();
	}
	
	public ArrayList getJoueurs()
	{
		return datas;
	}
	
	public int indexOf(Joueur joueur)
	{
		return datas.indexOf(joueur);
	}
	
	public Joueur getJoueur(int index)
	{
		return (Joueur)datas.get(index);
	}
	
	
	public boolean addJoueur(Joueur joueur)
	{
		if(!datas.contains(joueur))
		{
			datas.add(joueur);
			return true;
		}
		return false;
	}

	public boolean updateJoueur(Joueur j)
	{
		int index = datas.indexOf(j);
		if(index==-1) return false;
		datas.set(index,j);
		return true;
	}

	public void setJoueur(int row, Joueur joueur)
	{
		datas.set(row,joueur);
	}

	public void removeJoueur(int row)
	{
		datas.remove(row);
	}

	/**
	 * permet de modifier les donnée de la table
	 * @param joueurs la liste des joueurs du vainqueur au dernier
	 */
	public void setJoueurs(ArrayList joueurs)
	{
		datas = joueurs;
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
		Joueur joueur = (Joueur)datas.get(row);
		boolean isFree = (joueur.getNbParticipation()>0
			&&joueur.isHere()
			&&joueur.getNbParticipation()%4==0);
		switch(col)
		{
			
			case 0 : // Nom
				
				return joueur.getNom();
			case 1 : // Prénom
				return joueur.getPrenom();
			case 2 : // Classement			
				return joueur.getStrOldClassement();
			case 3 : // Catégorie
				return joueur.getCategorie().toString().substring(0, 1).toUpperCase();
			case 4 : // Sex
				String sex = joueur.isMasculin()?"M":"F";
				return sex;
			case 5 : // Club				
				return joueur.getClub();				
			case 6 : // Présent				
				return new Boolean(joueur.isHere());
			case 7 : // Partitipation				
				return new Integer(joueur.getNbParticipation());				
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
		if(col==6||col==7) return true;
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
		Joueur joueur = getJoueur(row);
        if(col==6)
        {
            joueur.setHere(((Boolean)value).booleanValue());
        }
        else if(col==7)
        {
            joueur.setNbParticipation(((Integer)value).intValue());
        }
		datas.set(row,joueur);
		fireTableCellUpdated(row, col);
	}
}
