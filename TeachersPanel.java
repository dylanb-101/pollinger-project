//Dylan Barrett
//Description
//11/14/24

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TeachersPanel extends CustomPanel {

    private final BigDuty bigDuty;

    private final Border border;

    private Teacher activeTeacher;

    private CustomPanel schedule;

    public TeachersPanel(String panelName, Dimension d, BigDuty bigDuty) {
        super(panelName, d);

        this.bigDuty = bigDuty;
        this.border = BorderFactory.createLineBorder(Color.BLACK);
        this.activeTeacher = bigDuty.getTeacher(0);

        initView();



    }

    public void initView() {

        BorderLayout layout = new BorderLayout(1, 0);
        this.setLayout(layout);

//        teacher sidebar

        JPanel sidebarContainer = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;

        gbc.fill = GridBagConstraints.HORIZONTAL;

        for(Teacher t : bigDuty.getTeachers()) {

            JButton button = new JButton();

            button.setText(t.getName());
            button.addActionListener(e -> updateScheduleDisplay(t));

            button.setComponentPopupMenu(new TeacherPopUp(t, bigDuty, this));

            sidebarContainer.add(button, gbc);
            gbc.gridy++;

        }

        JScrollPane sideBar = new JScrollPane(sidebarContainer);

        this.add(sideBar, BorderLayout.WEST);

//        schedule display

        this.schedule = new CustomPanel("Schedule Display", new Dimension((int) (GUIMain.WIDTH*(0.75)), this.getHeight()));

        GridLayout scheduleLayout = new GridLayout(1, 5);

        schedule.setLayout(scheduleLayout);

        updateScheduleDisplay(activeTeacher);


        this.add(schedule, BorderLayout.CENTER);

    }

    public JComponent createMenu(String day, int period) {

        Assignment assignment = activeTeacher.getAssignment(day, new Period(period));

        JComponent menu = new JLabel(assignment instanceof Free ? String.valueOf(activeTeacher.scoreAssignment(assignment)) : assignment.getName());

        if(assignment instanceof Duty) menu.setForeground(Color.RED);

        AssignmentPopUp popUp = new AssignmentPopUp(assignment, bigDuty, this);

        menu.setComponentPopupMenu(popUp);

        menu.setBorder(border);

        return menu;

    }

    public void updateScheduleDisplay(Teacher teacher) {

        activeTeacher = teacher;

//        schedule = new CustomPanel("Schedule Display", new Dimension((int) (GUIMain.WIDTH*(0.75)), this.getHeight()));

        schedule.removeAll();


        CustomPanel[] dayPanels = new CustomPanel[5];
        String[] days = {"M", "T", "W", "R", "F"};

//        set up panel

        for(int i = 0; i < dayPanels.length; i++) {

            dayPanels[i] = new CustomPanel(days[i], new Dimension(50, 100));

            GridLayout mondayLayout = new GridLayout(9, 1);
            GridLayout otherLayout = new GridLayout(7, 1);

            if(i == 0) dayPanels[i].setLayout(mondayLayout);
            else dayPanels[i].setLayout(otherLayout);

            schedule.add(dayPanels[i]);
            dayPanels[i].setBorder(border);

        }

//        fill in panel

        for(int d = 0; d < days.length; d++) {

            int offset = d == 0 ? 1 : 0;

            for(int p = 1; p <= ((GridLayout) (dayPanels[d].getLayout())).getRows() - offset; p++) {

                JComponent table = null;

                if(d != 0 && p == 4) table = createMenu(days[d], -2);
                else table = createMenu(days[d], p);

                dayPanels[d].add(table);

                if(d == 0 && p == 4) dayPanels[d].add(createMenu(days[d], -2));

            }
        }

        repaint();
        revalidate();

    }

    public Teacher getActiveTeacher() {
        return activeTeacher;
    }
}
