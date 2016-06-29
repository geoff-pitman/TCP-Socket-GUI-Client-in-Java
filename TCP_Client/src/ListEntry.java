// Authored by: Joris Van den Bogaert
// Source URL - http://esus.com/how-do-i-create-a-jlist-with-icons-and-text/
// Used by: Geoffrey Pitman
// CSC464 - HCI
// 6/30/16
// Iteration 2
// ListEntry.java
// Purpose: specialized list object class to hold/display string value AND image

import javax.swing.ImageIcon;

class ListEntry
	{
	   private String value;
	   private ImageIcon icon;
	  
	   public ListEntry(String value, ImageIcon icon) {
	      this.value = value;
	      this.icon = icon;
	   }
	   
	   public ListEntry (ListEntry copy)
	   {
		   value = copy.getValue();
		   icon = copy.getIcon();
	   }
	  
	   public String getValue() {
	      return value;
	   }
	  
	   public ImageIcon getIcon() {
	      return icon;
	   }
	  
	   public String toString() {
	      return value;
	   }
	}