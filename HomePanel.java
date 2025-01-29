//Dylan Barrett
//Description
//11/17/24

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;


public class HomePanel extends CustomPanel {

    private BigDuty bigDuty;
    private final Border border;

    private String[] headings = {"TEACHER", "CREDIT",  "COURSES", "MAX DUTIES", "TOTAL", "PASCACKS", "HALLS", "COVERAGES", "FROSH PASCACKS"}; //tables

    private String[][] data;


    public HomePanel(String panelName, Dimension d, BigDuty bigDuty) {
        super(panelName, d);

        this.bigDuty = bigDuty;
        this.data = new String[bigDuty.getTeachers().size()][headings.length];

        BorderLayout layout = new BorderLayout(1, 1);
        this.setLayout(layout);

        this.border = BorderFactory.createLineBorder(Color.BLACK);


        makePanel();


    }

    public void makePanel() {

        GridLayout layout = new GridLayout(20, 0);
        layout.setVgap(2);

        //        make the sidebar
        JPanel sidebar = new JPanel(layout);


        JButton saveButton = new JButton("Save Data");

        saveButton.addActionListener(e -> {

            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);

//            chooser.showSaveDialog(this);

            System.out.println(bigDuty.dutySlotsToSaveString());

            if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

                System.out.println(chooser.getSelectedFile().getAbsolutePath());
                FileUtility.downloadCurrentSchedule(SemesterTab.gui, chooser.getSelectedFile());

            }

//           FileUtility.downloadCurrentSchedule(bigDuty.getTeachers());

        });

        sidebar.add(saveButton);

        JButton nextSemsesterButton = new JButton("Next Semester");

        nextSemsesterButton.addActionListener(e -> {

            int n = JOptionPane.showConfirmDialog(this, "Are you sure you want to start the next semester? This will lock Semester 1 and no more changes will be able to be made.", "Start New Semester?", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);



            if(bigDuty.getSemester().equals("S1") && n == 0) {

                ArrayList<Teacher> teachersCopy = new ArrayList<>();

                teachersCopy.addAll(bigDuty.getTeachers());

                BigDuty bd = new BigDuty(teachersCopy, null, "S2");

                SemesterTab semesterTab = GUIMain.makeTeachersTabs(bd);


                SemesterTab.gui.getSemesterTabs().add(bd.getSemester(), semesterTab);
                SemesterTab.gui.semesters.add(semesterTab);



            }

        });

        if(!bigDuty.getSemester().equals("S2")) {
            sidebar.add(nextSemsesterButton);
        }


        JButton reassignDutiesButton = new JButton("Reassign Duties");
        reassignDutiesButton.addActionListener(e -> {
            int n = JOptionPane.showConfirmDialog(this, "Are you sure you want to assign duties? This will clear any previously assigned duties. Make sure you have configured everything!", "Assign Duties?", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

            if(n == 0) {

                if(bigDuty.getNumberOfDutySlots() > bigDuty.getUnlockedTeachers().size()*3) {

                    n = JOptionPane.showConfirmDialog(this, "You have more duties that need to be filled than there are available teachers! If you continue all teachers WILL have 3 duties but not all available slots will be filled! You can go back and configure the duty slots if this is not OK.", "Assign Duties?", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

                }

                if(n == 0) {
                    bigDuty.reassignDuties();

                }

            }

        });

        sidebar.add(reassignDutiesButton);

        JButton assignDutiesButton = new JButton("Assign Duties");

        assignDutiesButton.addActionListener(e -> {

            bigDuty.assignDuties();
            bigDuty.refreshPanels();

        });

        sidebar.add(assignDutiesButton);

        JButton clearDutiesButton = new JButton("Clear Duties");

        clearDutiesButton.addActionListener(e -> {

            int n = JOptionPane.showConfirmDialog(this, "Are you SURE you want to clear all duties? This will remove all duties, even ones assigned manually!", "Clear Duties?", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

            if(n == 0) {
                System.out.println("Clearing duties");
                bigDuty.clearDuties();
                System.out.println("Done Clearing duties");
                bigDuty.refreshPanels();
            }

        });
        sidebar.add(clearDutiesButton);






        // make the center area
        JPanel center = new JPanel();


        populateData();

        DefaultTableModel model = new DefaultTableModel(data, headings) {
            private static final long serialVersionUID = 1L;

            @Override
            public Class<?> getColumnClass(int column){
                if(column > 0 || column < 9)
                    return Integer.class;
                return super.getColumnClass(column);
            }

            public boolean isCellEditable(int row, int column) {

                if(column == 3 || column == 1) return true;

                return false;
            }

        };

        model.addTableModelListener(e -> {

            int val = (int) model.getValueAt(e.getFirstRow(), e.getColumn());
            Teacher t = bigDuty.getTeachers().get(e.getFirstRow());


            if(e.getFirstRow() == 1) { // changing the teachers social credit
                t.setSocialCredit(val);
            } else if(e.getFirstRow() == 3) { // changing the teachers max duties

                if(t.getDuties().size() > val) {
                    int n = JOptionPane.showConfirmDialog(this, "<html>Your about to change this teachers max duties but the teacher already has  <b>" + t.getDuties().size() + "</b> duties assigned! </html>", "Duties already assigned!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

                    if(n == 0) {
                        t.setMaxDuties(val);
                    } else {
                        model.setValueAt(t.getMaxDuties(), e.getFirstRow(), e.getColumn());
                    }
                } else {
                    t.setMaxDuties(val);

                }

            }

        });

        JTable table = new JTable(model);
        table.setVisible(true);
        table.setAutoCreateRowSorter(true);
        table.setFont(new Font("Tratatello", Font.PLAIN, 14));

        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.yellow);
        JScrollPane pane = new JScrollPane(table);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setGridColor(Color.LIGHT_GRAY);

        table.setComponentPopupMenu(new HomePopUp(bigDuty, table, this));


        table.addMouseListener(new MouseAdapter() {

            public void MousePressed(MouseEvent e) {

                int r = table.rowAtPoint(e.getPoint());
                int c = table.columnAtPoint(e.getPoint());

                System.out.println(r + ", " + c);

            }

        });





        this.removeAll();



        this.add(sidebar, BorderLayout.WEST);
        this.add(pane, BorderLayout.CENTER);

    }

    public void populateData() {

        for(int r = 0; r < data.length; r++) {

            Teacher t = bigDuty.getTeacher(r);

            data[r][0] = t.getLastName();
            data[r][1] = String.valueOf(t.getSocialCredit());
            data[r][2] = String.valueOf(t.totalCourses());
            data[r][3] = String.valueOf(t.getMaxDuties());
            data[r][4] = String.valueOf(t.totalDuties());
            data[r][5] = String.valueOf(t.getPascacks().size());
            data[r][6] = String.valueOf(t.getHalls().size());
            data[r][7] = String.valueOf(t.getCoverages().size());
            data[r][8] = String.valueOf(t.getFroshPascacks().size());




        }


    }

    @Override
    public void refreshPanel() {

        makePanel();
        System.out.println("updateing home panel");

        super.refreshPanel();
    }
}
