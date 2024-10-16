import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

// Ben Smith
// Sep 25, 2024

public class CustomPanel extends JPanel
{
   private static final long serialVersionUID = 1L;
   

   public CustomPanel(String panelName, Dimension d) {
      this.setPreferredSize(d);
      this.setName(panelName);
   }
   public CustomPanel(String panelName, Dimension d, BorderLayout bl) {
      this.setPreferredSize(d);
      this.setName(panelName);
      this.setLayout(bl);
      
      
   }
   
   
//   public JComponent returnPanel(){
//      return panel;
//   }
   
   
//   public void addLabel(JLabel l) {
//      panel.add(l);
//   }

   
   
}
