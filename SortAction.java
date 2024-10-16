import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

//Justin Yoo
//Program Description:
//Oct 10, 2024

public class SortAction extends AbstractAction {
   
    protected String typeSort;
   
    public SortAction(String name, String type) {
        super(name);
        typeSort = type;
    }

    public void actionPerformed(ActionEvent e) {
        // Button pressed logic goes here
       System.out.println("Sorting " + typeSort + " ...");
       GUIMain.setWarningMsg("Sorted " + typeSort + "!");
      
    }
    
    
    
    
}
