public class helpPanel extends CustomPanel
{
   private JPanel wrapper, helpDesk;
   private JComboBox<String> cb;
   private String[] choices;

	@@ -27,20 +27,14 @@ public helpPanel(String panelName, Dimension d, BorderLayout bl, String hover)
      super(panelName, d, bl);
      this.hover = hover;

      makeWrapper();
      makeHelpDesk();
      this.add(wrapper);
   }
   private void makeWrapper()
   {
      wrapper = new JPanel();
      JPanel panel = new JPanel();
      panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // added code
      JLabel lbl = new JLabel("Help Desk Directory");
      lbl.setFont(new Font("Tratatello", Font. BOLD, 18));
      lbl.setAlignmentX(Component.CENTER_ALIGNMENT);

      choices = new String[] {"Download Help", "Other Help"};
      cb = new JComboBox<String>(choices);
      cb.setMaximumSize(cb.getPreferredSize()); 
      cb.setAlignmentX(Component.CENTER_ALIGNMENT);
	@@ -51,7 +45,7 @@ private void makeWrapper()
         @Override
         public void actionPerformed(ActionEvent e)
         {
            doClicked();
            revalidate();
            repaint();
         }
	@@ -66,61 +60,10 @@ public void actionPerformed(ActionEvent e)
      panel.add(cb);
      panel.add(btn);
      wrapper.add(panel);
   }
   private void makeHelpDesk() {
      helpDesk = new JPanel();
      JLabel lbl = new JLabel(); //exchangable
      JLabel lbl2 = new JLabel();
      if(cb.getSelectedIndex() == 0) {
         lbl.setText("Download Tutorial");
         lbl2.setText("Go to the File Bar and press desired download type!");}
      if(cb.getSelectedIndex() == 1) {
         lbl.setText("Other Tutorial");
         lbl2.setText("Nothing here yet!");}
      lbl.setFont(new Font("Tratatello", Font.PLAIN, 18));
      lbl2.setFont(new Font("Tratatello", Font.PLAIN, 18));



      JButton btn = new JButton("Return");
      btn.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            returnMenu();
            revalidate();
            repaint();
         }


        });
      btn.setAlignmentX(Component.CENTER_ALIGNMENT); // added code
      btn.setFont(new Font("Tratatello", Font. BOLD, 18));
      helpDesk.add(lbl2);
      helpDesk.add(btn, BorderLayout.SOUTH);
      helpDesk.add(lbl, BorderLayout.NORTH);
   }

   public void doClicked()
   {

    //justin was here 2024 november 4th i think Trump wins

      this.remove(wrapper);
      this.remove(helpDesk);
      this.makeHelpDesk();
      this.add(helpDesk);
      revalidate();
      repaint();
   }
   public void returnMenu()
   {
      this.remove(helpDesk);
      this.add(wrapper);

//      System.out.println(teacherIndex);
      revalidate();
      repaint();
   }
   public JPanel getWrapper()
   {
	@@ -155,4 +98,4 @@ public void setHover(String hover)
      this.hover = hover;
   }

}
