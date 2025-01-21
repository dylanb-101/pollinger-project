//Dylan Barrett
//Description
//11/17/24

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;


public class HomePanel extends CustomPanel {

    private BigDuty bigDuty;
    private final Border border;

    private String[] headings = {"TEACHER", "COURSES", "TOTAL", "PASCACKS", "HALLS", "COVERAGES", "FROSH PASCACKS"}; //tables

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

        //        make the sidebar
        JPanel sidebar = new JPanel(new GridLayout(20, 0));

        JButton saveButton = new JButton("Save Data");

        saveButton.addActionListener(e -> {

            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);

//            chooser.showSaveDialog(this);

            if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

                System.out.println(chooser.getSelectedFile().getAbsolutePath());
                FileUtility.downloadCurrentSchedule(bigDuty.getTeachers(), chooser.getSelectedFile());

            }

//           FileUtility.downloadCurrentSchedule(bigDuty.getTeachers());

        });

        sidebar.add(saveButton);

        JButton nextSemsesterButton = new JButton("Next Semester");

        nextSemsesterButton.addActionListener(e -> {

            if(bigDuty.getSemester().equals("S1")) {

//                ArrayList<Teacher> teachers = new ArrayList<>(bigDuty.getTeachers());
//                ArrayList<Teacher> deepCopyTeachers = new ArrayList<>();
//
//                for(Teacher t : teachers) {
//                    Teacher deepCopyTeacher = new Teacher(t);
//                    deepCopyTeacher.fillFreesInForSemester("S2");
//                    deepCopyTeachers.add(deepCopyTeacher);
//
//                }

                ArrayList<Teacher> teachersCopy = new ArrayList<>();

                teachersCopy.addAll(bigDuty.getTeachers());

                BigDuty bd = new BigDuty(teachersCopy, null, "S2");

                SemesterTab semesterTab = GUIMain.makeTeachersTabs(bd);


                SemesterTab.gui.getSemesterTabs().add(bd.getSemester(), semesterTab);
                SemesterTab.gui.semesters.add(semesterTab);



            }

        });

        sidebar.add(nextSemsesterButton);


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
                return false;
            }
        };

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
            data[r][1] = String.valueOf(t.totalCourses());
            data[r][2] = String.valueOf(t.totalDuties());
            data[r][3] = String.valueOf(t.getPascacks().size());
            System.out.println(t.getPascacks().size());
            data[r][4] = String.valueOf(t.getHalls().size());
            data[r][5] = String.valueOf(t.getCoverages().size());
            data[r][6] = String.valueOf(t.getFroshPascacks().size());




        }


    }

    @Override
    public void refreshPanel() {

        makePanel();

        super.refreshPanel();
    }
}
