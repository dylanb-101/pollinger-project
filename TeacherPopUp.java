//Dylan Barrett
//Description
//11/15/24

import javax.swing.*;
import java.awt.*;

public class TeacherPopUp extends JPopupMenu {

    private final Teacher teacher;

    private final BigDuty bigDuty;
    private final TeachersPanel panel;

    public TeacherPopUp(Teacher teacher, BigDuty bigDuty, TeachersPanel panel) {

        super(teacher.getName());

        this.teacher = teacher;
        this.bigDuty = bigDuty;
        this.panel = panel;

        addLockTeacher();
        addClearDuties();

    }

    public void addLockTeacher() {

        JCheckBoxMenuItem lockTeacher = new JCheckBoxMenuItem("Lock Teacher");

        if(teacher.isLocked()) lockTeacher.setSelected(true);

        lockTeacher.addActionListener(e -> {

            if(teacher.isLocked()) {
                teacher.unlockFrees();
                lockTeacher.setSelected(false);
            } else {
                teacher.lockFrees();
                lockTeacher.setSelected(true);
            }

            panel.updateScheduleDisplay(panel.getActiveTeacher());

        });

        this.add(lockTeacher);


    }

    public void addClearDuties() {

        JMenuItem clearDuties = new JMenuItem("Clear Duties");

        clearDuties.addActionListener(e -> {

            for(Duty d : teacher.getDuties()) {

                Free free = new Free(d);

                teacher.replaceAssignment(free);

            }

            panel.updateScheduleDisplay(panel.getActiveTeacher());

        });

        this.add(clearDuties);

    }

}
