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
   private JComboBox<String> cb;
   private String[] choices;
   private ArrayList<Teacher> teachers;
   private String selectedChoiceBox;
   private int teacherIndex;
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
            System.out.println("Selected " + (String) cb.getSelectedItem() + "!");
            GUIMain.setWarningMsg("Selected " + (String) cb.getSelectedItem() + "!");
            selectedChoiceBox = (String) cb.getSelectedItem();
            System.out.println("selectedChoiceBox is " +  selectedChoiceBox);
            System.out.println("ChoiceBoxIndex is " +  cb.getSelectedIndex());
            teacherIndex = cb.getSelectedIndex();
            System.out.println("teacherIndex is " + teacherIndex);
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
      
      
      Object[][] data = new Object[numRows][colHeadings.length];
      
      System.out.println("Drawing teacher object " + teachers.get(teacherIndex).getName());


      Teacher t = teachers.get(teacherIndex);

      for(int i = 0; i < t.getAssignments().size(); i++) {

         data[i][0] = t.getName();
         data[i][1] = t.getRoom();
         data[i][2] = t.getDepartment();
         data[i][3] = t.getAssignments().get(i).getName();
         data[i][4] = t.getAssignments().get(i).getPeriod();
         data[i][5] = t.getAssignments().get(i).getDay();
         data[i][6] = t.getAssignments().get(i).getSemester();

      }
      
      //FROM HERE THE CORRELATED TEACHER CAN BE ADDED NOW
      data[0][0] = teachers.get(teacherIndex).getLastName();
      data[0][1] = teachers.get(teacherIndex).getRoom();
      data[0][2] = teachers.get(teacherIndex).getDepartment();
      data[0][3] = teachers.get(teacherIndex).getAssignments().size();
      
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

      tPanel.add(table, BorderLayout.NORTH); //add table to the center  
      JTableHeader header = table.getTableHeader();
      header.setBackground(Color.yellow);
      pane = new JScrollPane(table);
      table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
      table.setGridColor(Color.LIGHT_GRAY);
      
      JButton b1 = new JButton("Return to Menu"); //demo
      b1.addActionListener(new ActionListener() {

       @Override
       public void actionPerformed(ActionEvent e)
       {
          System.out.println("Returned!");
          
          returnMenu();
          repaint();
       }
      });
      tPanel.add(b1, BorderLayout.SOUTH);
      
      tPanel.add(pane, BorderLayout.CENTER);
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

      System.out.println(teacherIndex);
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
