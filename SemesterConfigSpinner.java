//Dylan Barrett
//Description
//1/26/25

import javax.swing.*;
import javax.swing.event.ChangeEvent;

public class SemesterConfigSpinner extends JSpinner {

    private DutyType type;
    private Period period;
    private String day;
    private BigDuty bigDuty;

    public SemesterConfigSpinner(SpinnerModel model, DutyType type, Period period, String day, BigDuty bigDuty) {

        super(model);

        this.type = type;
        this.period = period;
        this.day = day;
        this.bigDuty = bigDuty;

        bigDuty.addDutySlot(period, day, type, getValue());

        this.addChangeListener(this::stateChanged);

    }

    public void stateChanged(ChangeEvent e) {

        System.out.println("CustomSpinner.stateChanged");

        bigDuty.addDutySlot(period, day, type, getValue());

        bigDuty.refreshPanels();

    }

    public Integer getValue() {
        return (Integer) super.getValue();
    }

    public DutyType getType() {
        return type;
    }

    public Period getPeriod() {
        return period;
    }

    public String getDay() {
        return day;
    }

    public boolean equals(Object o) {

        if(!(o instanceof SemesterConfigSpinner)) return false;

        SemesterConfigSpinner spinner = (SemesterConfigSpinner) o;

        return spinner.getDay().equals(this.getDay()) && spinner.getType().equals(this.getType()) && spinner.getPeriod().equals(this.getPeriod());

    }
}
