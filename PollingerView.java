import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

//Benjamin Smith
//Program Description:
//Oct 21, 2024

public class PollingerView extends CustomPanel
{
   JScrollPane pane;
   private int numRows;
   private final Border b;
   String[] column;
   protected BigDuty bd;

   public PollingerView(String panelName, Dimension d, BigDuty bd)
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
      column[0] = "";

      //real input attempted
         for(int x = 0; x < dayNames.length; x++) {

             int offset = x == 0 ? 1 : 0;

             for (int y = 1; y <= ((GridLayout) (weekPanels[x].getLayout())).getRows() - offset; y++) {

                 JTable table = createDylanTable(bd.getTeachersWithDutyInPeriod(new Period(y), dayNames[x]), new Period(y), dayNames[x]);

                 JScrollPane scroll = new JScrollPane(table);

                 scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);


                 weekPanels[x].add(scroll);

                 if (x == 0 && y == 4) {
                     weekPanels[x].add(createDylanTable(bd.getTeachersWithDutyInPeriod(new Period("L"), dayNames[x]), new Period("L"), dayNames[x]));
                 }


             }
         }

   }

    @Override
    public void refreshPanel() {

       this.removeAll();

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

        super.refreshPanel();
    }

    //next class
   public JTable createDylanTable(ArrayList<Teacher> teachers, Period p, String day) {

       String[][] data = new String[teachers.size()][1];

       for(int i = 0; i < teachers.size(); i++) {

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