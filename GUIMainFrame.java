import java.awt.BorderLayout;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;

//Justin Yoo
//Program Description:
//Oct 18, 2024

public class GUIMainFrame extends JPanel
{
  public GUIMainFrame()
  {
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
//   menuBar.add(edit);
   //view
   view = new JMenu("VIEW");
   view.setMnemonic(KeyEvent.VK_A);
   view.getAccessibleContext().setAccessibleDescription("File");
//   menuBar.add(view);
   //tools
   tools = new JMenu("TOOLS");
   tools.setMnemonic(KeyEvent.VK_A);
   tools.getAccessibleContext().setAccessibleDescription("TOOLS");
//   menuBar.add(tools);
   
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
}
