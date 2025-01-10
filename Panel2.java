import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

//Justin Yoo
//Program Description:
//Oct 18, 2024

public class Panel2 extends CustomPanel
{
   private BigDuty bigDuty;
   private JScrollPane pane;
   private int numRows;
   private String[] colHeadings;
   private String hover, selected;
   private static JPanel tPanel, wrapper;
   private boolean isClicked;
   private final JComboBox<String> cb;
   private final String[] choices;
   private final ArrayList<Teacher> teachers;
   private String selectedChoiceBox;
   private static int teacherIndex;
   private DefaultTableModel model;
   private JTable table;
   
   @SuppressWarnings("unchecked")
   public Panel2(String panelName, Dimension d, BorderLayout b, BigDuty bigduty, String[] colheadings, int numRows, String s)
   {
      super(panelName, d, b);
      bigDuty = bigduty;
      colHeadings = colheadings;
      this.numRows = numRows;
      hover = s;
      teacherIndex = 0;
      this.setLayout(new BorderLayout());

      //************************************************************//
      teachers = bigDuty.getTeachers();
      Collections.sort(teachers);
      choices = new String[bigDuty.getTeachers().size()];
      
      for(int i = 0; i < bigDuty.getTeachers().size(); i++)
         choices[i] = teachers.get(i).getLastName() + ", " + teachers.get(i).getFirstName();
      Arrays.sort(choices);
      cb = new JComboBox<String>(choices);
      cb.setMaximumSize(cb.getPreferredSize()); 
      cb.setAlignmentX(Component.CENTER_ALIGNMENT);
      
      
      makeWrapper(); 
      makeTPanel();
      this.add(wrapper);
      //*********************************************************************************************************//
   }
   public void makeWrapper()
   {
      wrapper = new JPanel();
      JPanel panel = new JPanel();
      panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // added code
      JLabel lbl = new JLabel("Select one of the possible choices and click OK");
      lbl.setAlignmentX(Component.CENTER_ALIGNMENT);

    
      
      
      JButton btn = new JButton("OK");
      btn.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e)
         {
//            GUIMain.setWarningMsg("Selected " + (String) cb.getSelectedItem() + "!");
            selectedChoiceBox = (String) cb.getSelectedItem();
            teacherIndex = cb.getSelectedIndex();
            doClicked();
            revalidate();
            repaint();
         }
        });
      btn.setAlignmentX(Component.CENTER_ALIGNMENT); // added code
      
      
      panel.add(lbl);
      panel.add(cb);
      panel.add(btn);
      wrapper.add(panel);
   }
   public void makeTPanel()
   {
      tPanel = new JPanel();
      tPanel.setLayout(new BorderLayout());
      
      

      //////////////
     
      ///////////////

      Teacher t = teachers.get(teacherIndex);

      Object[][] data = new Object[t.getAssignments().size()][colHeadings.length];

      for(int i = 0; i < t.getAssignments().size(); i++) {

         data[i][0] = t.getName();
         data[i][1] = t.getRoom();
         data[i][2] = t.getDepartment();
         data[i][3] = t.getAssignments().get(i).getName();
         data[i][4] = t.getAssignments().get(i).getPeriod();
         data[i][5] = t.getAssignments().get(i).getDay();
         data[i][6] = t.getAssignments().get(i).getSemester();

      }

      model = new DefaultTableModel(data, colHeadings) {
      private static final long serialVersionUID = 1L;
      @Override
      public Class<?> getColumnClass(int column){
         if(column > 0 || column < 9)
            return Integer.class;
         return super.getColumnClass(column);
      }
      public boolean isCellEditable(int row, int column) {
         return false;
      }
      };  
      table = new JTable(model);
      table.setVisible(true);
      table.setAutoCreateRowSorter(true);
      table.setFont(new Font("Tratatello", Font.PLAIN, 14));

      JTableHeader header = table.getTableHeader();
      header.setBackground(Color.yellow);
      pane = new JScrollPane(table);
      table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
      table.setGridColor(Color.LIGHT_GRAY);
    
      
      tPanel.add(pane, BorderLayout.CENTER);
      JButton b1 = new JButton("Return to Menu"); //demo
      b1.addActionListener(new ActionListener() {

       @Override
       public void actionPerformed(ActionEvent e)
       {

          returnMenu();
          repaint();
       }
      });
      tPanel.add(b1, BorderLayout.NORTH);
      
      JButton b2 = new JButton("-->"); //demo
      b2.addActionListener(new ActionListener() {

       @Override
       public void actionPerformed(ActionEvent e)
       {
          doNext();
          repaint();
       }
      });
      tPanel.add(b2, BorderLayout.EAST);
      JButton b3 = new JButton("<--"); //demo
      b3.addActionListener(new ActionListener() {

       @Override
       public void actionPerformed(ActionEvent e)
       {
          doLast();
          repaint();
       }
      });
      tPanel.add(b3, BorderLayout.WEST);
      repaint();
   }
   
   public void doNext()
   {
      this.remove(tPanel);
      
     
      makeTPanel();


      teacherIndex++;

      if(teacherIndex >= teachers.size()) {
         teacherIndex = 0;
      }

      this.add(tPanel);
      this.remove(wrapper);
      revalidate();
      repaint();
   }
   public void doLast() 
   {
      this.remove(tPanel);
      
   
      
      
      makeTPanel();

      teacherIndex--;


      if(teacherIndex < 0) {
         teacherIndex = teachers.size()-1;
      }


      this.add(tPanel);
      this.remove(wrapper);
      revalidate();
      repaint();
   }
   
   public void doClicked()
   {

    
      
      this.remove(wrapper);
      this.remove(tPanel);
      this.makeTPanel();
      this.add(tPanel);
      revalidate();
      repaint();
   }

   public void returnMenu()
   {
      this.remove(tPanel);
      this.add(wrapper);
      teacherIndex = 0;
      revalidate();
      repaint();
   }
   

   public BigDuty getBigDuty()
   {
      return bigDuty;
   }

   public void setBigDuty(BigDuty bigDuty)
   {
      this.bigDuty = bigDuty;
   }

   public JScrollPane getPane()
   {
      return pane;
   }

   public void setPane(JScrollPane pane)
   {
      this.pane = pane;
   }

   public int getNumRows()
   {
      return numRows;
   }

   public void setNumRows(int numRows)
   {
      this.numRows = numRows;
   }

   public String[] getColHeadings()
   {
      return colHeadings;
   }

   public void setColHeadings(String[] colHeadings)
   {
      this.colHeadings = colHeadings;
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
