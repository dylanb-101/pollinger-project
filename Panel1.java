import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

//Justin Yoo
//Program Description:
//Oct 18, 2024

public class Panel1 extends CustomPanel
{
   private BigDuty bigDuty;
   JScrollPane pane;
   private int numRows;
   private String[] colHeadings;
   private Object[][] data;
   private String hover;

   public Panel1(String panelName, Dimension d, BorderLayout b, BigDuty bigduty, String[] colheadings, int numRows, Object[][] data, String hover)
   {
      super(panelName, d, b);
      bigDuty = bigduty;
      colHeadings = colheadings;
      this.data = data;
      this.hover = hover;
      
      //buttons
      JButton b1 = new JButton("SmartSort"); //demo
      b1.addActionListener(new ActionListener() {

       @Override
       public void actionPerformed(ActionEvent e)
       {
          System.out.println("Smartly Sorted!");
         
       }
      });
      JButton b2 = new JButton("Footer"); //demo
      JButton b3 = new JButton("East"); //demo
      JButton b4 = new JButton("West"); //demo
      
      //Add Buttons
//      this.add(b1, BorderLayout.NORTH);
//      this.add(b2, BorderLayout.SOUTH);
//      this.add(b3, BorderLayout.EAST);
//      this.add(b4, BorderLayout.WEST);

     //testing data
      
      
      //test filler
      for(int i = 0; i < data.length; i++)
      { //basically import data kinda like teacher.getCourse(0).getName(); or .getCourseID() or something like that

          Teacher t = bigDuty.getTeacher(i);

          data[i][0] = t.getLastName();
          data[i][1] = t.getId();
          data[i][2] = t.getRoom();
          data[i][3] = t.getDepartment();
  


      }
     
      DefaultTableModel model = new DefaultTableModel(data, colHeadings) {
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
      
      JTable table = new JTable(model);
      table.setVisible(true);
      table.setAutoCreateRowSorter(true);
      table.setFont(new Font("Tratatello", Font.PLAIN, 14));
      
      
      
      //*********************************************************************************************************//
      
      this.add(table, BorderLayout.CENTER); //add table to the center  
      JTableHeader header = table.getTableHeader();
      header.setBackground(Color.yellow);
      pane = new JScrollPane(table);
      table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
      table.setGridColor(Color.LIGHT_GRAY);
      this.add(pane);
   }
   

   public JScrollPane getPane()
   {
      return pane;
   }

   public void setPane(JScrollPane pane)
   {
      this.pane = pane;
   }

   public BigDuty getBigDuty()
   {
      return bigDuty;
   }

   public void setBigDuty(BigDuty bigDuty)
   {
      this.bigDuty = bigDuty;
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

   public Object[][] getData()
   {
      return data;
   }

   public void setData(Object[][] data)
   {
      this.data = data;
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
