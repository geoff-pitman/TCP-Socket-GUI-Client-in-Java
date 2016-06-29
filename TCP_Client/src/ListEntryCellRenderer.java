// Authored by: Joris Van den Bogaert
// Source URL - http://esus.com/how-do-i-create-a-jlist-with-icons-and-text/
// Used by: Geoffrey Pitman
// CSC464 - HCI
// 6/30/16
// Iteration 2
// ListEntryCellRenderer.java
// Purpose: special event handler class to provide functionality to
//          the listbox cells / the items in the list box

import javax.swing.*;
import java.awt.*;

	class ListEntryCellRenderer extends JLabel implements ListCellRenderer
	{
	   private JLabel label;
	  
	   public Component getListCellRendererComponent(JList list, Object value,
	                                                 int index, boolean isSelected,
	                                                 boolean cellHasFocus) {
	      ListEntry entry = (ListEntry) value;
	  
	      setText(value.toString());
	      setIcon(entry.getIcon());
	   
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