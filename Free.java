//Dylan Barrett
//Description
//9/23/24

public class Free extends Assignment {

    private boolean isLocked;

    public Free(int period, String semester, String name, String day, Teacher teacher, boolean adjustPeriod) {
        super(period, semester, name, day, teacher, adjustPeriod);

    }

    public Free(Period period, String semester, String name, String day, Teacher teacher) {
        super(period.getValue(), semester, name, day, teacher, false);
    }

    public Free(Assignment assignment) {

        super(assignment.getPeriod().getValue(), assignment.getSemester(), "Free", assignment.getDay(), assignment.getTeacher(), false);

    }

    public Free(Free free, Teacher teacher) {
        super(free, teacher);
        this.isLocked = free.isLocked;
    }


    @Override
    public String getRoom() {
        return "Any";
    }

    @Override
    public double getWeight() {
        return 0;
    }

    @Override
    public String getDepartment() {
        return "None";
    }

    public void setLocked(boolean locked) {

        this.isLocked = locked;

    }

    public boolean isLocked() {
        return isLocked;
    }

    public String toString() {
        return "Free" + super.toString();
    }
}
