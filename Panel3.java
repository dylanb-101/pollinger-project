import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

//Benjamin Smith
//Program Description:
//Oct 21, 2024

public class Panel3 extends CustomPanel
{
   JScrollPane pane;
   private int numRows;
   private final Border b;
   String[] column;
   protected BigDuty bd;

   public Panel3(String panelName, Dimension d, BigDuty bd)
   {
      super(panelName, d);
      
     b = BorderFactory.createLineBorder(Color.black);

     this.bd = bd;
     // Week creation, panels + layout
      GridLayout week = new GridLayout(1, 5);
      CustomPanel[] weekPanels = new CustomPanel[5];
      this.setLayout(week);
      String[] dayNames = {"M", "T", "W", "R", "F"};
      
      for(int x = 0; x < 5; x++) {
         weekPanels[x] = new CustomPanel(dayNames[x], new Dimension(100, 100));
         GridLayout monday = new GridLayout(9, 1);
         GridLayout other = new GridLayout(7, 1);

         if(x == 0) weekPanels[x].setLayout(monday);
         else weekPanels[x].setLayout(other);

         this.add(weekPanels[x]);
         weekPanels[x].setBorder(b);
      }
      column = new String[1];
      column[0] = "Duties";
      
      //Filling this with JLabels for now, real data input will go here
      
// 
//         for(int x = 0; x < 5; x++)
//            for(int y = 0; y < 7; y++) {
//               JLabel j = new JLabel();
//               j.setBorder(b);
//               j.setText(dayNames[x] + " " + (y+1));
//               weekPanels[x].add(dayNames[x], j);
//            }
//         
//         //extra 2 for monday
//         JLabel j2 = new JLabel();
//         j2.setBorder(b);
//         j2.setText(dayNames[0] + " " + 8);
//         weekPanels[0].add(dayNames[0], j2);
//         
//         JLabel j3 = new JLabel();
//         j3.setBorder(b);
//         j3.setText(dayNames[0] + " " + 9);
//         weekPanels[0].add(dayNames[0], j3);
      
      //real input attempted
         for(int x = 0; x < dayNames.length; x++) {

             int offset = x == 0 ? 1 : 0;

             for (int y = 1; y <= ((GridLayout) (weekPanels[x].getLayout())).getRows() - offset; y++) {

                 JTable table = createDylanTable(bd.getTeachersWithDutyInPeriod(new Period(y), dayNames[x]), new Period(y), dayNames[x]);


                 weekPanels[x].add(table);

                 if (x == 0 && y == 4) {
                     weekPanels[x].add(createDylanTable(bd.getTeachersWithDutyInPeriod(new Period("L"), dayNames[x]), new Period("L"), dayNames[x]));
                 }


             }
         }
         //extra 2 for monday
//         weekPanels[0].add(createDylanTable(null));
//         weekPanels[0].add(createDylanTable(null));
//         weekPanels[0].add(createDylanTable(null));
         
         
         
            
      
      
      
      
      
      //*********************************************************************************************************//
      
//      this.add(table, BorderLayout.CENTER); //add table to the center  
//      JTableHeader header = table.getTableHeader();
//      header.setBackground(Color.yellow);
//      pane = new JScrollPane(table);
//      table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
//      table.setGridColor(Color.LIGHT_GRAY);
//      this.add(pane);
   }

   
   //next class
   public JTable createDylanTable(ArrayList<Teacher> teachers, Period p, String day) {

       String[][] data = new String[teachers.size()][1];

       for(int i = 0; i < teachers.size(); i++) {

           System.out.println(day + ", " + p + ", " + teachers.get(i).getName());
           data[i][0] = teachers.get(i).getName() + ", " + teachers.get(i).getAssignment(day, p).getName();
       }

      DefaultTableModel tm = new DefaultTableModel(data, column); //eventual data implementation, make data have things
      JTable t = new JTable(tm);
      t.setBorder(b);
      t.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
      return t;
   }

    public JTable createTable(Object[][] data) {
        DefaultTableModel tm = new DefaultTableModel(data, column); //eventual data implementation, make data have things
        JTable t = new JTable(tm);
        t.setBorder(b);
        t.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        return t;
    }
   
   
   public JScrollPane getPane()
   {
      return pane;
   }

   public void setPane(JScrollPane pane)
   {
      this.pane = pane;
   }

}