//Dylan Barrett
//Description
//11/17/24

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;


public class HomePanel extends CustomPanel {

    private BigDuty bigDuty;
    private final Border border;

    private String[] headings = {"TEACHER", "COURSES", "TOTAL", "PASCACKS", "HALLS", "COVERAGES", "FROSH PASCACKS"}; //tables

    private Object[][] data;
    
    private ArrayList<JButton> tableButtons;

    int r, c;
    
    public HomePanel(String panelName, Dimension d, BigDuty bigDuty) {
        super(panelName, d);

        this.bigDuty = bigDuty;
        this.data = new Object[bigDuty.getTeachers().size()][headings.length];

        BorderLayout layout = new BorderLayout(1, 1);
        this.setLayout(layout);

        this.border = BorderFactory.createLineBorder(Color.BLACK);
        tableButtons = new ArrayList<JButton>();

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

                BigDuty bd = new BigDuty(bigDuty.getTeachers(), null, "S2");

                SemesterTab semesterTab = new SemesterTab(bd);

//                semesterTab.addTab("S2", GUIMain.makeTeachersTabs(bd));

                GUIMain.semesters.add(semesterTab);

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
                return true;
            }
        };

        JTable table = new JTable(model);
        table.setVisible(true);
        table.setAutoCreateRowSorter(true);
        table.setFont(new Font("Tratatello", Font.PLAIN, 14));
        
        //Instantiate an actionlistener
        
        table.addMouseListener(new MouseAdapter() {

           public void MousePressed(MouseEvent e) {

                r = table.rowAtPoint(e.getPoint());
                c = table.columnAtPoint(e.getPoint());

               System.out.println(r + ", " + c);

           }

       });

        Action doClicked = new AbstractAction()
        {
            public void actionPerformed(ActionEvent e)
            {
               
               bigDuty.getSemesterPane().changeTabTo(2);
               Panel2 panel = (Panel2) bigDuty.getSemesterPane().getSelectedComponent();
               panel.setTeacherIndex(table.getSelectedRow());
               panel.doClicked();
            }
        };
        
        table.getColumn("TEACHER").setCellRenderer(new ButtonRenderer());
        table.getColumn("TEACHER").setCellEditor(new ButtonEditor(new JCheckBox(), doClicked));

        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.yellow);
        JScrollPane pane = new JScrollPane(table);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setGridColor(Color.LIGHT_GRAY);

        table.setComponentPopupMenu(new HomePopUp(bigDuty, table, this));


        




        this.removeAll();



        this.add(sidebar, BorderLayout.WEST);
        this.add(pane, BorderLayout.CENTER);
        
        
        //TESTING FOR GETTING TO TEACHER VIEW jan17 jy
        
        JButton j = new JButton("TEST");
        j.addActionListener(doClicked);
        this.add(j, BorderLayout.NORTH);
        

    }
    
  
    public void populateData() {
       
       
          
        
        for(int r = 0; r < data.length; r++) {

            Teacher t = bigDuty.getTeacher(r);
           

            data[r][0] = String.valueOf(t.getName());
               
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
