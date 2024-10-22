import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

//Benjamin Smith
//Program Description:
//Oct 21, 2024

public class Panel3 extends CustomPanel
{
   JScrollPane pane;
   private int numRows;
   private Border b;

   public Panel3(String panelName, Dimension d)
   {
      super(panelName, d);
      
     b = BorderFactory.createLineBorder(Color.black);

     // Week creation, panels + layout
      GridLayout week = new GridLayout(1, 5);
      CustomPanel[] weekPanels = new CustomPanel[5];
      this.setLayout(week);
      String[] dayNames = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
      
      for(int x = 0; x < 5; x++) {
         weekPanels[x] = new CustomPanel(dayNames[x], new Dimension(100, 100));
            GridLayout monday = new GridLayout(9, 1);
            GridLayout other = new GridLayout(7, 1);
         if(x == 0)
            weekPanels[x].setLayout(monday);
         else
            weekPanels[x].setLayout(other);
         this.add(weekPanels[x]);
         weekPanels[x].setBorder(b);
      }
      
      
      //Filling this with JLabels for now, real data input will go here
      
 
         for(int x = 0; x < 5; x++)
            for(int y = 0; y < 7; y++) {
               JLabel j = new JLabel();
               j.setBorder(b);
               j.setText(dayNames[x] + " " + (y+1));
               weekPanels[x].add(dayNames[x], j);
            }
         
         //extra 2 for monday
         JLabel j2 = new JLabel();
         j2.setBorder(b);
         j2.setText(dayNames[0] + " " + 8);
         weekPanels[0].add(dayNames[0], j2);
         
         JLabel j3 = new JLabel();
         j3.setBorder(b);
         j3.setText(dayNames[0] + " " + 9);
         weekPanels[0].add(dayNames[0], j3);
         
            
      
      
      
      
      
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
//   public JTable createTable(Period p) {
//      
//   }
   
   
   public JScrollPane getPane()
   {
      return pane;
   }

   public void setPane(JScrollPane pane)
   {
      this.pane = pane;
   }

}