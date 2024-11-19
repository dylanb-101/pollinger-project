//Dylan Barrett
//Description
//11/11/24

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class Panel4 extends CustomPanel {

    private BigDuty bigDuty;
    private Border border;

    private String[] column;
    private int teacherIndex;

    public Panel4(String panelName, Dimension d, BigDuty bigDuty) {
        super(panelName, d);

        this.bigDuty = bigDuty;
        this.teacherIndex = 4;


        this.border = BorderFactory.createLineBorder(Color.BLACK);

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
            weekPanels[x].setBorder(border);
        }
        column = new String[1];
        column[0] = "Duties";



        for(int x = 0; x < dayNames.length; x++) {

            int offset = x == 0 ? 1 : 0;

            for (int y = 1; y <= ((GridLayout) (weekPanels[x].getLayout())).getRows() - offset; y++) {

                JTable table = null;

                if(x != 0 && y == 4) {
                    table = createDylanTable(bigDuty.getTeacher(teacherIndex).getDayLunch(dayNames[x]));
                } else table = createDylanTable(bigDuty.getTeacher(teacherIndex).getAssignment(dayNames[x], new Period(y)));




                weekPanels[x].add(table);

                if (x == 0 && y == 4) {
                    weekPanels[x].add(createDylanTable(bigDuty.getTeacher(teacherIndex).getDayLunch(dayNames[x])));
                }


            }
        }



    }

    public JTable createDylanTable(Assignment assignment) {

        String[][] data = new String[1][1];

        for(int i = 0; i < 1; i++) {

            if(assignment != null) data[i][0] = String.valueOf(assignment.getTeacher().scoreAssignment(assignment));
            else data[i][0] = "null";

            if(assignment != null && assignment instanceof Lunch) data[i][0] = "Lunch";

        }

        DefaultTableModel tm = new DefaultTableModel(data, column); //eventual data implementation, make data have things
        JTable t = new JTable(tm);
        t.setBorder(border);
        t.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        return t;
    }

}
