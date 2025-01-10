// rollover and update social credit
// scrollable panel 3 period data
// add total duties column - DONE
// colors
// export as csv/excel
// saving and loading on main panel
// stating panel has load from save, or create new - DONE
// undo



import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.awt.Component;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;

//Justin Yoo
//Program Description:
//Sep 20, 2024

public class GUIMain extends JPanel implements KeyListener, MouseListener
{

   //DECOR
   // Specify the look and feel to use by defining the LOOKANDFEEL constant
   // Valid values are: null (use the default), "Metal", "System", "Motif",
   // and "GTK"
   final static String LOOKANDFEEL = "Metal";

   // If you choose the Metal L&F, you can also choose a theme.
   // Specify the theme to use by defining the THEME constant
   // Valid values are: "DefaultMetal", "Ocean",  and "Test"
   final static String THEME = "Ocean";

   //Frame
   private int flexWidth;
   private int flexHeight;
   public static final int WIDTH = 800; //temp
   public static final int HEIGHT = 720; //temp
    public static ArrayList<PopUp> menus = new ArrayList<>();

   //Data
   private int periodNum;
   private String demoSchedule;
   private Object[][] data;
   private Teacher teacher;
   private Course course;
//   private FileReadIn dataset;
   private BigDuty bigDuty;
   //Panels
   private CustomPanel panel1, panel2, helpPanel, panel3, panel4, teachersPanel, homePanel;

   private static JFrame frame;


   public GUIMain() //default constructor
   {

       System.out.println(BigDuty.BACKUP_LOCATION);
       new File(BigDuty.BACKUP_LOCATION).mkdir();

         //Initial Init
         this.addKeyListener(this);
         this.addMouseListener(this);
         this.setFocusable(true);

         this.setLayout(new BorderLayout()); //setting layout

       bigDuty = new BigDuty("src/PollingerProject-DutyData.csv", this);


       Component contents = this.createComponents();

       JPanel panel = makeLandingScreen();
       this.add(panel, BorderLayout.CENTER);

//       this.add(contents);


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

      try {
         // Set cross-platform Java L&F (also called "Metal")
     UIManager.setLookAndFeel(
         UIManager.getCrossPlatformLookAndFeelClassName());
 }
 catch (UnsupportedLookAndFeelException e) {
    // handle exception
 }
 catch (ClassNotFoundException e) {
    // handle exception
 }
 catch (InstantiationException e) {
    // handle exception
 }
 catch (IllegalAccessException e) {
    // handle exception
 }

      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
             //Turn off metal's use of bold fonts
     UIManager.put("swing.boldMetal", Boolean.FALSE);
     createAndShowGUI(); //initializes all GUI stuff
         }
     });


   }

   public JPanel makeLandingScreen() {

       JPanel frame = new JPanel();

       frame.setLayout(new BorderLayout());

       JButton loadPolButton = new JButton("Load from .pol");

       loadPolButton.addActionListener(e -> {

           JFileChooser chooser = new JFileChooser();

           FileFilter filter = new FileNameExtensionFilter(".pol", "pol", "csv");
           chooser.setFileFilter(filter);
           chooser.showDialog(frame, "Load Save");

           if(chooser.getSelectedFile() != null) {

               ArrayList<Teacher> teachers = FileUtility.readSaveFile(chooser.getSelectedFile());
               bigDuty = new BigDuty(teachers, this);

               this.remove(frame);

               Component screen = this.createComponents();

               this.add(screen);
               this.refreshPanels();

           }




       });

       JButton loadReportButton = new JButton("Load from Genesis");
       loadReportButton.addActionListener(e -> {

           JFileChooser chooser = new JFileChooser();

           FileFilter filter = new FileNameExtensionFilter(".csv",  "csv");
           chooser.setFileFilter(filter);
           chooser.showDialog(frame, "Load Genesis Report");

           if(chooser.getSelectedFile() != null) {

               this.bigDuty = new BigDuty(chooser.getSelectedFile(), this);

               this.remove(frame);

               Component screen = this.createComponents();

               this.add(screen);
               this.refreshPanels();
           }



       });

       frame.add(loadPolButton, BorderLayout.WEST);
       frame.add(loadReportButton, BorderLayout.EAST);



       return frame;

   }

   public void refreshPanels() {

       panel1.refreshPanel();
       panel2.refreshPanel();
       panel3.refreshPanel();
       panel4.refreshPanel();
       teachersPanel.refreshPanel();
       helpPanel.refreshPanel();
       homePanel.refreshPanel();

   }

   public Component createComponents() {
    //Initial Init


      JPanel pane = new JPanel();
      //********************DATA BEGIN************************//

      String[] colHeadingsPanel1 = {"TEACHER","COURSES", "ROOM", "DEPT", "YEAR"}; //tables
      String[] colHeadingsPanel2 = {"TEACHER", "ROOM", "DEPT", "NAME", "PERIOD", "DAY", "SEMESTER"};



      Object[][]data = new Object[bigDuty.getTeachers().size()][colHeadingsPanel1.length]; //size of the dataframe

      int numRows = data.length; //number of rows
      //********************DATA END**************************//




      //JTabbedPane ADD	   }
      JTabbedPane panedTabs = new JTabbedPane();
      panedTabs.setVisible(true);

      //*********************PANELS BEGIN**********************//



       //Panel1 #homepage#
       String panel1HoverMessage = "Shows all teachers' general information";
       panel1 = new Panel1("All Teacher View", getPreferredSize(), new BorderLayout(), bigDuty, colHeadingsPanel1, data, panel1HoverMessage);


       homePanel = new HomePanel("Home Panel", getPreferredSize(), bigDuty);

       //panel2 #no function yet#
       String panel2HoverMessage = "Shows individual teachers' general information";
       panel2 = new Panel2("Individual Teacher View", getPreferredSize(), new BorderLayout(), bigDuty, colHeadingsPanel2, numRows, panel2HoverMessage);

//       panel3
         panel3 = new PollingerView("Pollinger View", new Dimension(WIDTH, HEIGHT), bigDuty);

       panel4 = new Panel4("Panel 4", new Dimension(WIDTH, HEIGHT), bigDuty);

       teachersPanel = new TeachersPanel("Teacher View", new Dimension(WIDTH, HEIGHT), bigDuty);

//         panel 4
//       panel4 = new Panel4("Panel 4", new Dimension(WIDTH, HEIGHT), bigDuty);
//       panedTabs.addTab("Panel 4", panel4);

       String helpPanelHoverMessage = "\" UHL \" love this help desk!";
       helpPanel = new helpPanel("Help Panel", getPreferredSize(), new BorderLayout(), helpPanelHoverMessage);


       panedTabs.addTab(homePanel.getName(), homePanel);
       panedTabs.addTab(panel1.getName(), null, panel1, ((Panel1) panel1).getHover());
       panedTabs.addTab(panel2.getName(), null, panel2, ((Panel2)(panel2)).getHover());
       panedTabs.addTab("Pollinger View", panel3);
//       panedTabs.addTab("Panel 4", panel4);
       panedTabs.addTab("Teacher View", teachersPanel);
       panedTabs.addTab(helpPanel.getName(), null, helpPanel, ((helpPanel)(helpPanel)).getHover());

       //********************PANELS END*************************//
       //Initialization
       pane.add(panedTabs);


      return pane;
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
      initLookAndFeel();
      //imports a mainframe
      JFrame.setDefaultLookAndFeelDecorated(true);
      frame = new GUIMainFrame();
  }

private static void initLookAndFeel() {
      String lookAndFeel = null;

      if (LOOKANDFEEL != null) {
          if (LOOKANDFEEL.equals("Metal")) {
              lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
            //  an alternative way to set the Metal L&F is to replace the
            // previous line with:
            // lookAndFeel = "javax.swing.plaf.metal.MetalLookAndFeel";

          }

          else if (LOOKANDFEEL.equals("System")) {
              lookAndFeel = UIManager.getSystemLookAndFeelClassName();
          }

          else if (LOOKANDFEEL.equals("Motif")) {
              lookAndFeel = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
          }

          else if (LOOKANDFEEL.equals("GTK")) {
              lookAndFeel = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
          }

          else {
              System.err.println("Unexpected value of LOOKANDFEEL specified: "
                                 + LOOKANDFEEL);
              lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
          }

          try {


              UIManager.setLookAndFeel(lookAndFeel);

              // If L&F = "Metal", set the theme

              if (LOOKANDFEEL.equals("Metal")) {
                if (THEME.equals("DefaultMetal"))
                   MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
                else if (THEME.equals("Ocean"))
                   MetalLookAndFeel.setCurrentTheme(new OceanTheme());

                UIManager.setLookAndFeel(new MetalLookAndFeel());
              }




          }

          catch (ClassNotFoundException e) {
              System.err.println("Couldn't find class for specified look and feel:"
                                 + lookAndFeel);
              System.err.println("Did you include the L&F library in the class path?");
              System.err.println("Using the default look and feel.");
          }

          catch (UnsupportedLookAndFeelException e) {
              System.err.println("Can't use the specified look and feel ("
                                 + lookAndFeel
                                 + ") on this platform.");
              System.err.println("Using the default look and feel.");
          }

          catch (Exception e) {
              System.err.println("Couldn't get specified look and feel ("
                                 + lookAndFeel
                                 + "), for some reason.");
              System.err.println("Using the default look and feel.");
              e.printStackTrace();
          }
      }
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

       for(PopUp popUp : menus) {

           popUp.popUp(e);

       }

   }

   @Override
   public void mouseReleased(MouseEvent e)
   {

       int i = 0;

       for(PopUp popUp : menus) {

           popUp.popUp(e);

       }

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

       bigDuty.rankTeacher("Uhl");

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
