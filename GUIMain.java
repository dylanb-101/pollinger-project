import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

//Justin Yoo
//Program Description:
//Sep 20, 2024

public class GUIMain extends JPanel implements KeyListener, MouseListener
{
   
   //Frame
   private int flexWidth;
   private int flexHeight;
   private final int WIDTH = 800; //temp
   private final int HEIGHT = 720; //temp
   
   //Data
   private int periodNum;
   private String demoSchedule;
   private Object[][] data;
   private Teacher teacher;
   private Course course;
//   private FileReadIn dataset;
   private BigDuty bigDuty;
   //Panels
   private CustomPanel panel1, panel2, panel1Right;

   public GUIMain() //default constructor
   {
      
      //Initial Init
      this.addKeyListener(this);
      this.addMouseListener(this);
      this.setFocusable(true);

//      init big duty and data stuff
       this.bigDuty = new BigDuty("src/PollingerProject-DutyData.csv");
      
      String[] colHeadings = {"TEACHER","COURSE", "PERIOD", "SEM", "SEC", "ROOM", "DEPT", "YEAR"}; //tables
      String[] demoHeading = {"Demo"}; //tables
      int numRows = 50; //number of rows
      this.setLayout(new BorderLayout());
      
      //JTabbedPane
        JTabbedPane panedTabs = new JTabbedPane();
        panedTabs.setVisible(true);

        //buttons
        JButton b1 = new JButton("Header"); //demo
        b1.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent e)
         {
            System.out.println("Top Pressed");
         }
        });
        JButton b2 = new JButton("Footer"); //demo
        JButton b3 = new JButton("East"); //demo
        JButton b4 = new JButton("West"); //demo

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
 
        //Panel1 #homepage#
        panel1 = new CustomPanel("Panel 1", getPreferredSize(), new BorderLayout());
        panel1.add(b1, BorderLayout.NORTH);
        panel1.add(b2, BorderLayout.SOUTH);
        panel1.add(b3, BorderLayout.EAST);
        panel1.add(b4, BorderLayout.WEST);
        panedTabs.addTab("Panel 1", null, panel1, "Hovering...");
      
      //****************************************************PANEL 1 TABLE FILLING IT IN***********************************//

       //testing data
        data = new Object[bigDuty.getTeachers().size()][colHeadings.length]; //size of the dataframe
        
        for(int i = 0; i < data.length; i++)
        { //basically import data kinda like teacher.getCourse(0).getName(); or .getCourseID() or something like that
//           data[i][0] = (int)(Math.random()*20);
           data[i][1] = (int)(Math.random()*20);
           data[i][2] = (int)(Math.random()*20);
           data[i][3] = (int)(Math.random()*20);
           data[i][4] = (int)(Math.random()*20);
           data[i][5] = (int)(Math.random()*20);
           data[i][6] = (int)(Math.random()*20);
           data[i][7] = (int)(Math.random()*20);

            Teacher t = bigDuty.getTeacher(i);

            data[i][0] = t.getLastName();


        }
       
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
        
        
        
        //*********************************************************************************************************//
        
        panel1.add(table, BorderLayout.CENTER); //add table to the center  
        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.yellow);
        JScrollPane pane = new JScrollPane(table);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setGridColor(Color.LIGHT_GRAY);

        //right side p1 Table model
//        DefaultTableModel rightModel = new DefaultTableModel(1, 1) ;
//        rightModel.setColumnIdentifiers(demoHeading);
//        JTable rightTable = new JTable(rightModel);        
//        JTableHeader demoRightheader = rightTable.getTableHeader();
//        demoRightheader.setBackground(Color.YELLOW);
//        JScrollPane demoRightpane = new JScrollPane(rightTable);
//        rightTable.setAutoCreateColumnsFromModel(true);
//        rightTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        //panel2 #no function yet#
        panel2 = new CustomPanel("Panel 2", getPreferredSize(), new BorderLayout());
        panel2.add(p2b1, BorderLayout.NORTH); //adding buttons 
        panel2.add(p2b2, BorderLayout.SOUTH);
        panel2.add(p2b3, BorderLayout.EAST);
        panel2.add(p2b4, BorderLayout.WEST);
        panedTabs.addTab("Panel 2", null, panel2, "Hovering...");      //adding tab to pane tabs
        DefaultTableModel p2model = new DefaultTableModel(numRows, colHeadings.length) ;
        model.setColumnIdentifiers(colHeadings);
        JTable p2table = new JTable(p2model); 
        
        //p2 table
        panel2.add(p2table, BorderLayout.CENTER);        
        JTableHeader p2header = p2table.getTableHeader();
        p2header.setBackground(Color.yellow);
        JScrollPane p2pane = new JScrollPane(p2table);
        p2table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        //Initialization
        panel1.add(pane);   
//        panel1.add(demoRightpane, BorderLayout.EAST);  
        panel2.add(p2pane);     
        add(panedTabs);
   }
   public static void setWarningMsg(String text){
      Toolkit.getDefaultToolkit().beep();
      JOptionPane optionPane = new JOptionPane(text, JOptionPane.OK_OPTION);
      JDialog dialog = optionPane.createDialog(text);
      dialog.setAlwaysOnTop(true);
      dialog.setVisible(true);
  }
    
   public void paintComponent(Graphics g)
   {
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D) g; //casting the g into g2
      
      //Formatting:
      g2.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON));
      
      //Testing
    
   }
   public static void main(String[] args)
   {
//      data = readData("src/TeacherSchedules_24-25.csv");
//      System.out.println(data.get(1));
      
      
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
             //Turn off metal's use of bold fonts
     UIManager.put("swing.boldMetal", Boolean.FALSE);
     createAndShowGUI(); //initializes all GUI stuff
         }
     });
      
      
   }
   public static ArrayList<String> readData(String fileName)   { //file reading
      ArrayList<String> textData = new ArrayList<String>();
      File filePath = new File(fileName);
      try  {
         System.out.println("Reading data...");
         Scanner dataGetter = new Scanner(filePath);
         while(dataGetter.hasNext())
              textData.add(dataGetter.nextLine());
         dataGetter.close();
      }
      catch (FileNotFoundException e){
         System.out.println("Cannot find data file.");
      }
      return textData;
   }
   private static void createAndShowGUI() { 
      //Create and set up the window.
      JFrame frame = new JFrame("GUIMain");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       
      //Add content to the window.
      frame.add(new GUIMain(), BorderLayout.CENTER);
       
      //Display the window.
      frame.pack();
      frame.setVisible(true);
      frame.setLocationRelativeTo(null); //centers
      
      
      //JMENUS NOW:
      JMenuBar menuBar;
      JMenu menu, sortmenu, submenu, file, edit, view, tools;
      JMenuItem menuItem;
      JRadioButtonMenuItem rbMenuItem;
      JCheckBoxMenuItem cbMenuItem;
      
      menuBar = new JMenuBar();


    //file
    file = new JMenu("FILE");
    file.setMnemonic(KeyEvent.VK_A);
    file.getAccessibleContext().setAccessibleDescription("File");
    menuBar.add(file);
    //edit
    edit = new JMenu("EDIT");
    edit.setMnemonic(KeyEvent.VK_A);
    edit.getAccessibleContext().setAccessibleDescription("Edit");
    menuBar.add(edit);
    //view
    view = new JMenu("VIEW");
    view.setMnemonic(KeyEvent.VK_A);
    view.getAccessibleContext().setAccessibleDescription("File");
    menuBar.add(view);
    //tools
    tools = new JMenu("TOOLS");
    tools.setMnemonic(KeyEvent.VK_A);
    tools.getAccessibleContext().setAccessibleDescription("TOOLS");
    menuBar.add(tools);
    
    //file item
    menuItem = new JMenuItem("Print",
                             KeyEvent.VK_T);
    menuItem.getAccessibleContext().setAccessibleDescription(
            "No Function Yet");
    file.add(menuItem);
    
    //view
    sortmenu = new JMenu("Sort");
    
    menuItem = new JMenuItem(new SortAction("by Course ID", "by Course ID"));
    sortmenu.add(menuItem);
    
    menuItem = new JMenuItem(new SortAction("by Name", "by Name"));
    sortmenu.add(menuItem);
    
    menuItem = new JMenuItem(new SortAction("by Number of Classes", "by Number of Classes"));
    sortmenu.add(menuItem);
    
    menuItem = new JMenuItem(new SortAction("by Semester Availability", "by Semester Availability"));
    sortmenu.add(menuItem);
    view.add(sortmenu);

    //Dropdown
    submenu = new JMenu("Download");


    menuItem = new JMenuItem(".docx");
    submenu.add(menuItem);

    menuItem = new JMenuItem(".pdf");
    submenu.add(menuItem);
    
    menuItem = new JMenuItem(".jpeg");
    submenu.add(menuItem);
    
    file.add(submenu);

   
    frame.setJMenuBar(menuBar);
    
    
    
  }
   public Dimension getPreferredSize()
   {
      return new Dimension(WIDTH, HEIGHT);
   }

   @Override
   public void mouseClicked(MouseEvent e)
   {
   }

   @Override
   public void mousePressed(MouseEvent e)
   {
   }

   @Override
   public void mouseReleased(MouseEvent e)
   {
   }

   @Override
   public void mouseEntered(MouseEvent e)
   {
   }

   @Override
   public void mouseExited(MouseEvent e)
   {
   }

   @Override
   public void keyTyped(KeyEvent e)
   {
   }

   @Override
   public void keyPressed(KeyEvent e)
   {
   }

   @Override
   public void keyReleased(KeyEvent e)
   {
   }
   
   public int getGetWidth()
   {
      return getWidth();
   }
   
   public int getGetHeight()
   {
      return getHeight();
   }

   public int getFlexWidth()
   {
      return flexWidth;
   }

   public void setFlexWidth(int flexWidth)
   {
      this.flexWidth = flexWidth;
   }

   public int getFlexHeight()
   {
      return flexHeight;
   }

   public void setFlexHeight(int flexHeight)
   {
      this.flexHeight = flexHeight;
   }

   public int getPeriodNum()
   {
      return periodNum;
   }

   public void setPeriodNum(int periodNum)
   {
      this.periodNum = periodNum;
   }

   public String getDemoSchedule()
   {
      return demoSchedule;
   }

   public void setDemoSchedule(String demoSchedule)
   {
      this.demoSchedule = demoSchedule;
   }

//   public static ArrayList<String> getData()
//   {
//      return data;
//   }
//
//   public static void setData(ArrayList<String> data)
//   {
//      GUIMain.data = data;
//   }

   public Teacher getTeacher()
   {
      return teacher;
   }

   public void setTeacher(Teacher teacher)
   {
      this.teacher = teacher;
   }

   public CustomPanel getPanel1()
   {
      return panel1;
   }

   public void setPanel1(CustomPanel panel1)
   {
      this.panel1 = panel1;
   }

   public CustomPanel getPanel2()
   {
      return panel2;
   }

   public void setPanel2(CustomPanel panel2)
   {
      this.panel2 = panel2;
   }

   public int getWIDTH()
   {
      return WIDTH;
   }

   public int getHEIGHT()
   {
      return HEIGHT;
   }

   @Override
   public String toString()
   {
      return "GUIMain [flexWidth=" + flexWidth + ", flexHeight=" + flexHeight + ", WIDTH=" + WIDTH + ", HEIGHT="
            + HEIGHT + ", periodNum=" + periodNum + ", demoSchedule=" + demoSchedule + ", teacher=" + teacher
            + ", panel1=" + panel1 + ", panel2=" + panel2 + "]";
   }
   

}
