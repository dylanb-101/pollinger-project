//Dylan Barrett
//Description
//1/26/25

public class DutySlot {

    private DutyType type;
    private Period period;
    private String day;
    private int amount;

    public DutySlot(DutyType type, Period period, String day, int amount) {
        this.type = type;
        this.period = period;
        this.day = day;
        this.amount = amount;
    }

    public boolean equals(Object o) {

        DutySlot slot = (DutySlot) o;

        return this.period.equals(slot.getPeriod()) && this.day.equals(slot.getDay()) && this.type.equals(slot.getType());

    }

    public Duty makeEmptyDuty(String semester) {
        return new Duty(period, semester, Duty.getName(type), "???", day, type);
    }

    public String toSaveString() {
        return type.toString() + "|" + period.getPeriod() + "|" + day + "|" + amount;
    }


    public DutyType getType() {
        return type;
    }

    public void setType(DutyType type) {
        this.type = type;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
