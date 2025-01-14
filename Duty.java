//Dylan Barrett
//Description
//9/23/24

public class Duty extends Assignment {

    public static final String UNFILLED = "UNFILLED";
    public static final String FROSH_PASCACK = "FROSH_PASCACK";
    public static final String HALL = "HALL";
    public static final String PASCACK = "PASCACK";
    public static final String COVERAGE = "COVERAGE";

    private final String room;

    private DutyType type;



    public Duty(Assignment assignment, String name, String room, DutyType type) {

        super(assignment.getPeriod().getPeriod(), assignment.getSemester(), name, assignment.getDay(), assignment.getTeacher(), false);

        this.room = room;
        this.type = type;

    }

    public Duty(Period period, String semester, String name, String room, String day, DutyType type) {

        super(period.getPeriod(), semester, name, day, null, false);

        this.room = room;
        this.type = type;

    }

    public Duty(String period, String semester, String name, String room, String day, Teacher teacher, boolean adjustPeriod) {
        super(period, semester, name, day, teacher, adjustPeriod);

        this.room = room;

    }



    public Duty(String period, String semester, String name, String room, String day, Teacher teacher, boolean adjustPeriod, DutyType type) {
        super(period, semester, name, day, teacher, adjustPeriod);

        this.room = room;
        this.type = type;

    }

    @Override
    public String getRoom() {
        return room;
    }

    public double getWeight() {

        if(type == DutyType.UNASSIGNED) return 0;
        if(type == DutyType.FROSH_PASCACK) return -10;
        if(type == DutyType.HALL) return 5;
        if(type == DutyType.PASCACK) return -5;
        if(type == DutyType.COVERAGE) return 10;
        return 1000000;

    }

    @Override
    public String getDepartment() {
        return "None";
    }

    public DutyType getType() {
        return type;
    }

    public void setType(DutyType type) {
        this.type = type;
    }

    public String toString() {
        return "Duty" + super.toString();
    }

}
