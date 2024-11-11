import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

//Justin Yoo
//Program Description:
//Oct 30, 2024

public class helpPanel extends CustomPanel
{
   private JPanel wrapper, helpDesk;
   private JComboBox<String> cb;
   private String[] choices;
   
   private String hover;
   public helpPanel(String panelName, Dimension d, BorderLayout bl, String hover)
   {
      super(panelName, d, bl);
      this.hover = hover;
      
      makeWrapper();
      makeHelpDesk();
      this.add(wrapper);
   }
   private void makeWrapper()
   {
      wrapper = new JPanel();
      JPanel panel = new JPanel();
      panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // added code
      JLabel lbl = new JLabel("Help Desk Directory");
      lbl.setFont(new Font("Tratatello", Font. BOLD, 18));
      lbl.setAlignmentX(Component.CENTER_ALIGNMENT);

      choices = new String[] {"Toolbar Help", "Menu Help", "Teacher View Help", "Schedule Edit Help"};
      cb = new JComboBox<String>(choices);
      cb.setMaximumSize(cb.getPreferredSize()); 
      cb.setAlignmentX(Component.CENTER_ALIGNMENT);
      
      
      JButton btn = new JButton("OK");
      btn.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            doClicked();
            revalidate();
            repaint();
         }


        });
      btn.setAlignmentX(Component.CENTER_ALIGNMENT); // added code
      btn.setFont(new Font("Tratatello", Font. BOLD, 18));
      cb.setFont(new Font("Tratatello", Font. BOLD, 14));
      
      panel.add(lbl);
      panel.add(cb);
      panel.add(btn);
      wrapper.add(panel);
   }
   private void makeHelpDesk() {
      helpDesk = new JPanel();
      JLabel lbl = new JLabel(); //exchangable
      lbl.setText("<html>Menu Bar Tutorial <br> Go to the File Bar and press desired download type!</html>");
      lbl.setFont(new Font("Tratatello", Font.PLAIN, 18));
      
      
      
      JButton btn = new JButton("Return");
      btn.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            returnMenu();
            revalidate();
            repaint();
         }


        });
      btn.setAlignmentX(Component.CENTER_ALIGNMENT); // added code
      btn.setFont(new Font("Tratatello", Font. BOLD, 18));
      helpDesk.add(lbl);
      helpDesk.add(btn, BorderLayout.SOUTH);
   }
   
   public void doClicked()
   {

    //justin was here 2024 november 4th i think Trump wins
      
      this.remove(wrapper);
      this.remove(helpDesk);
      this.makeHelpDesk();
      this.add(helpDesk);
      revalidate();
      repaint();
   }
   public void returnMenu()
   {
      this.remove(helpDesk);
      this.add(wrapper);

//      System.out.println(teacherIndex);
      revalidate();
      repaint();
   }
   public JPanel getWrapper()
   {
      return wrapper;
   }
   public void setWrapper(JPanel wrapper)
   {
      this.wrapper = wrapper;
   }
   public JComboBox<String> getCb()
   {
      return cb;
   }
   public void setCb(JComboBox<String> cb)
   {
      this.cb = cb;
   }
   public String[] getChoices()
   {
      return choices;
   }
   public void setChoices(String[] choices)
   {
      this.choices = choices;
   }
   public String getHover()
   {
      return hover;
   }
   public void setHover(String hover)
   {
      this.hover = hover;
   }

}
