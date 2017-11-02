package tournoi.component;

import java.awt.Component;
import java.awt.Color;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import myUtils.TableSorter;
import tournoi.Joueur;


public class PlaceTableCellRenderer extends DefaultTableCellRenderer 
{
    /**
     * 
     */
    
    private static final long serialVersionUID = 1L;

    
    public Component getTableCellRendererComponent
       (JTable table, Object value, boolean isSelected,
       boolean hasFocus, int row, int column) 
    {
        Component cell = super.getTableCellRendererComponent
           (table, value, isSelected, hasFocus, row, column);

		
		TableSorter model = (TableSorter)table.getModel();
		JoueursTableModel modelJoueur = (JoueursTableModel)model.getTableModel();
		Joueur joueur = modelJoueur.getJoueur(model.modelIndex(row));		
		boolean isFree = (joueur.getNbParticipation()>0
			&&joueur.isHere()
			&&joueur.getNbParticipation()%4==0);
			
						
        	if(isFree)
        	{
        		// Font font = new Font("Helvetica",Font.BOLD,12);
				cell.setBackground( Color.green ); 
        	}
            else
            {
				if (isSelected) {
					  setBackground(table.getSelectionBackground());
					setForeground(table.getSelectionForeground());
				}
				  else {
					setBackground(table.getBackground());
					setForeground(table.getForeground());
				}
            }

        return cell;

    }
}



