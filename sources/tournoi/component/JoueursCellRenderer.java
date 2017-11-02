/*
 *
 */
package tournoi.component;

import java.awt.Component;

import javax.swing.*;

 public class JoueursCellRenderer extends JLabel implements ListCellRenderer {



    private static final long serialVersionUID = 1L;

    public Component getListCellRendererComponent(
       JList list,
       Object value,            // value to display
       int index,               // cell index
       boolean isSelected,      // is the cell selected
       boolean cellHasFocus)    // the list and the cell have the focus
     {
         String s = value.toString();
         setText((index+1)+" - "+s);
   	   if (isSelected) {
             setBackground(list.getSelectionBackground());
	       setForeground(list.getSelectionForeground());
	   }
         else {
	       setBackground(list.getBackground());
	       setForeground(list.getForeground());
	   }
	   setEnabled(list.isEnabled());
	   setFont(list.getFont());
         setOpaque(true);
         return this;
     }
 }
