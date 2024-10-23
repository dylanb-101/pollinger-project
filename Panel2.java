import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

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
   private Object[][] data;
   private String hover, selected;
   private static JPanel tPanel, wrapper;
   private boolean isClicked;
   
   public Panel2(String panelName, Dimension d, BorderLayout b, BigDuty bigduty, String[] colheadings, int numRows, String s)
   {
      super(panelName, d, b);
      bigDuty = bigduty;
      colHeadings = colheadings;
      hover = s;
 
      
      //************************************************************//
      
      wrapper = new JPanel();   
      JPanel panel = new JPanel();
      panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // added code
      JLabel lbl = new JLabel("Select one of the possible choices and click OK");
      lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
      String[] choices = new String[bigDuty.getTeachers().size()];
      for(int i = 0; i < bigDuty.getTeachers().size(); i++)
         choices[i] = bigDuty.getTeacher(i).getLastName() + ", " + bigDuty.getTeacher(i).getFirstName();
      Arrays.sort(choices);
      
      
      final JComboBox<String> cb = new JComboBox<String>(choices);
      cb.setMaximumSize(cb.getPreferredSize()); // added code
      cb.setAlignmentX(Component.CENTER_ALIGNMENT);// added code
      JButton btn = new JButton("OK");
      btn.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            System.out.println("Selected " + (String) cb.getSelectedItem() + "!");
            GUIMain.setWarningMsg("Selected " + (String) cb.getSelectedItem() + "!");
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
      this.add(wrapper);

      
     

      //************************************************************// 
      
      tPanel = new JPanel();
      JButton b1 = new JButton("Return to Menu"); //demo
      b1.addActionListener(new ActionListener() {

       @Override
       public void actionPerformed(ActionEvent e)
       {
          System.out.println("Returned!");
          returnMenu();
       }
      });
      tPanel.add(b1, BorderLayout.SOUTH);
      
      selected = (String)cb.getSelectedItem();
      int teacherIndex = 0;
      for(int i = 0; i < bigDuty.getTeachers().size(); i++)
         if(selected.equals(bigDuty.getTeacher(i).getName()))
            teacherIndex = i;
      
         data = new Object[numRows][colHeadings.length];
          Teacher t = bigDuty.getTeacher(teacherIndex);
          data[teacherIndex][0] = t.getLastName();
          data[teacherIndex][1] = t.getId();
          data[teacherIndex][2] = t.getRoom();
          data[teacherIndex][3] = t.getDepartment();
          
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

      tPanel.add(table, BorderLayout.NORTH); //add table to the center  
      JTableHeader header = table.getTableHeader();
      header.setBackground(Color.yellow);
      pane = new JScrollPane(table);
      table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
      table.setGridColor(Color.LIGHT_GRAY);
      tPanel.add(pane);
      //*********************************************************************************************************//
   }
   
   
   
   public void doClicked()
   {
      this.remove(wrapper);
      this.add(tPanel);
   }
   
   public void returnMenu()
   {
      this.add(wrapper);
      this.remove(tPanel);
      selected = "";
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
