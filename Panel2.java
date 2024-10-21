import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
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
   JScrollPane pane;
   private int numRows;
   private String[] colHeadings;
   private Object[][] data;
   private String hover;
   
   public Panel2(String panelName, Dimension d, BorderLayout b, BigDuty bigduty, String[] colheadings, int numRows, Object[][] data, String s)
   {
      super(panelName, d, b);
      bigDuty = bigduty;
      colHeadings = colheadings;
      this.data = data;
      hover = s;
      
      //buttons
      JButton p2b1 = new JButton("--"); //demo
      p2b1.addActionListener(new ActionListener() {
       @Override
       public void actionPerformed(ActionEvent e)
       {
          System.out.println("Top Pressed");
       }
      });
      JButton p2b2 = new JButton("Footer"); //demo
      p2b2.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            System.out.println("Bottom Pressed");
         }
        });
      JButton p2b3 = new JButton("East"); //demo
      p2b3.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            System.out.println("Right Pressed");
         }
        });
      JButton p2b4 = new JButton("West"); //demo
      p2b4.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            System.out.println("Left Pressed");
         }
        });
      
      //adding buttons
      this.add(p2b1, BorderLayout.NORTH); 
      this.add(p2b2, BorderLayout.SOUTH);
      this.add(p2b3, BorderLayout.EAST);
      this.add(p2b4, BorderLayout.WEST);
      
      //adding tab to pane tabs
    
      DefaultTableModel p2model = new DefaultTableModel(numRows, colHeadings.length) ;
      p2model.setColumnIdentifiers(colHeadings);
      JTable p2table = new JTable(p2model); 
      
      //p2 table
      this.add(p2table, BorderLayout.CENTER);        
      JTableHeader p2header = p2table.getTableHeader();
      p2header.setBackground(Color.yellow);
      JScrollPane p2pane = new JScrollPane(p2table);
      p2table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

      this.add(p2pane);
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
