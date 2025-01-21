import javax.swing.*;
import java.awt.*;
import java.io.Serial;

// Ben Smith
// Sep 25, 2024

public class CustomPanel extends JPanel {


    @Serial
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

    public void refreshPanel() {

        repaint();
        revalidate();

    }


//   public JComponent returnPanel(){
//      return panel;
//   }


//   public void addLabel(JLabel l) {
//      panel.add(l);
//   }


}
