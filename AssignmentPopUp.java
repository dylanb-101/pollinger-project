//Dylan Barrett
//Description
//11/14/24

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

public class AssignmentPopUp extends JPopupMenu {

    private Assignment assignment;
    private BigDuty bigDuty;

    private TeachersPanel parent;
    public AssignmentPopUp(Assignment assignment, BigDuty bigDuty, TeachersPanel parent) {

        super(assignment.getName());
        this.assignment = assignment;
        this.bigDuty = bigDuty;
        this.parent = parent;

        addLockingFree();
        addAssignDuty();
        addRemoveDuty();

    }

    public void addLockingFree() {

        boolean locked;

        JMenuItem item;

        if(assignment instanceof Free) {

            locked = ((Free) assignment).isLocked();

            if(locked) item = new JMenuItem("Unlock Free");
            else item = new JMenuItem("Lock Free");

            boolean finalLocked = locked;
            item.addActionListener(e -> {
                ((Free) assignment).setLocked(!((Free) assignment).isLocked());

                if(((Free) assignment).isLocked()) item.setText("Unlock Free");
                else item.setText("Lock Free");

            });

        } else {
            item = new JMenuItem("Lock Free");
            item.setEnabled(false);
        }

        this.add(item);

    }

    public void addAssignDuty() {

        JMenu menu = new JMenu("Assign Duty");

        JMenuItem hall = new JMenuItem("Hall");
        hall.addActionListener(e -> {

            Duty duty = new Duty(assignment, "Hall", "Hall", DutyType.HALL);

            replaceAssignment(duty);

        });
        menu.add(hall);

        JMenuItem pascack = new JMenuItem("Pascack");
        pascack.addActionListener(e -> {
            Duty duty = new Duty(assignment, "Pascack", "Caf", DutyType.PASCACK);

            replaceAssignment(duty);
        });
        menu.add(pascack);

        JMenuItem coverage = new JMenuItem("Coverage");
        coverage.addActionListener(e -> {
            Duty duty = new Duty(assignment, "Coverage", "None", DutyType.COVERAGE);
            replaceAssignment(duty);
        });
        menu.add(coverage);

        JMenuItem froshPascack = new JMenuItem("Freshman Pascack");
        froshPascack.addActionListener(e -> {
            Duty duty = new Duty(assignment, Duty.FROSH_PASCACK, "Caf", DutyType.FROSH_PASCACK);

            replaceAssignment(duty);
        });
        menu.add(froshPascack);

        this.add(menu);

    }

    public void addRemoveDuty() {

        JMenuItem removeDuty = new JMenuItem("Remove Duty");

        if(!(assignment instanceof Duty)) removeDuty.setEnabled(false);

        removeDuty.addActionListener(e -> {
            Free free = new Free(assignment);

            replaceAssignment(free);
        });
        this.add(removeDuty);

    }

    public void replaceAssignment(Assignment assignment) {

        bigDuty.replaceAssignment(assignment);

        this.assignment = assignment;

        parent.updateScheduleDisplay(assignment.getTeacher());

    }


}
