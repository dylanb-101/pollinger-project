//Dylan Barrett
//Description
//1/7/25

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;

public class HomePopUp extends JPopupMenu {

    private BigDuty bigDuty;

    private JTable table;
    private HomePanel homePanel;

    public HomePopUp(BigDuty bigDuty, JTable table, HomePanel homePanel) {

        this.bigDuty = bigDuty;
        this.table = table;
        this.homePanel = homePanel;

        this.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
//                table.rowAtPoint(new Point(e))
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
            }
        });

    }

    @Override
    protected void firePopupMenuWillBecomeVisible() {
        super.firePopupMenuWillBecomeVisible();
    }

    public Teacher getTeacher() {

        return null;

    }

    public void addLockTeacher() {



    }



}
