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
   private CustomPanel panel1, panel2, panel3, panel1Right;
   
   private static GUIMainFrame frame;

   public GUIMain() //default constructor
   {
      
         //Initial Init
         this.addKeyListener(this);
         this.addMouseListener(this);
         this.setFocusable(true);

         //********************DATA BEGIN************************//
         
         String[] colHeadingsPanel1 = {"TEACHER","COURSE", "ROOM", "DEPT", "YEAR"}; //tables
         String[] colHeadingsPanel2 = {"TEACHER", "ROOM", "DEPT", "NAME", "PERIOD", "DAY", "SEMESTER"};
         this.setLayout(new BorderLayout()); //setting layout
      
         this.bigDuty = new BigDuty("src/PollingerProject-DutyData.csv"); //cleaned-up data import
         Object[][]data = new Object[bigDuty.getTeachers().size()][colHeadingsPanel1.length]; //size of the dataframe
      
         int numRows = data.length; //number of rows
         //********************DATA END**************************//
         
         
         //JTabbedPane ADD
         JTabbedPane panedTabs = new JTabbedPane();
         panedTabs.setVisible(true);

         //*********************PANELS BEGIN**********************//
   
          //Panel1 #homepage#
          String panel1HoverMessage = "Shows all teachers' general information";
          panel1 = new Panel1("All Teacher View", getPreferredSize(), new BorderLayout(), bigDuty, colHeadingsPanel1, numRows, data, panel1HoverMessage);
          panedTabs.addTab(panel1.getName(), null, panel1, ((Panel1) panel1).getHover());
  
          //panel2 #no function yet#
          String panel2HoverMessage = "Shows individual teachers' general information";
          panel2 = new Panel2("Individual Teacher View", getPreferredSize(), new BorderLayout(), bigDuty, colHeadingsPanel2, numRows, panel2HoverMessage);
          panedTabs.addTab(panel2.getName(), null, panel2, ((Panel2)(panel2)).getHover());
          
          //panel3
          panel3 = new Panel3("Panel 3", new Dimension(WIDTH, HEIGHT), bigDuty);
          panedTabs.addTab("Panel 3", panel3);
          
          //********************PANELS END*************************//
          //Initialization 
          this.add(panedTabs);
   }
   public static void setWarningMsg(String text){
      Toolkit.getDefaultToolkit().beep();
      JOptionPane optionPane = new JOptionPane(text, JOptionPane.OK_OPTION);
      JDialog dialog = optionPane.createDialog(text);
      dialog.setAlwaysOnTop(true);
      dialog.setVisible(true);
  }
   public static void main(String[] args)
   {
      
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
      //imports a mainframe
      frame = new GUIMainFrame();
  }
   public Dimension getPreferredSize()
   {
      return new Dimension(WIDTH, HEIGHT);
   }
   @Override
   public void mouseClicked(MouseEvent e)
   {
//      int x = e.getX();
//      int y = e.getY();
//      System.out.println(x + " " + y);
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
