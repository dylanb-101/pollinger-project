import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

//Justin Yoo
//Program Description:
//Jan 21, 2025

class JTableButtonRenderer implements TableCellRenderer {        
   @Override public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
       JButton button = (JButton)value;
       return button;  
   }
}
