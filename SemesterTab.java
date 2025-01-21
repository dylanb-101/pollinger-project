//Dylan Barrett
//Description
//1/13/25

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class SemesterTab extends JTabbedPane {

    private BigDuty bigDuty;

    private ArrayList<CustomPanel> panels;

    public static GUIMain gui;

    public SemesterTab(BigDuty bigDuty) {

        super();

        this.bigDuty = bigDuty;
        this.panels = new ArrayList<>();
        this.bigDuty.setPane(this);



    }

    public void addTab(String title, Component component) {
        super.addTab(title, component);

        if(component instanceof CustomPanel) {
            panels.add((CustomPanel) component);
        }


    }

    public void refreshPanels() {
        for (CustomPanel panel : panels) {
            panel.refreshPanel();
        }

    }

    public void keyPressed(KeyEvent e) {

        System.out.println(bigDuty.getSemester());

    }

}
