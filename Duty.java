//Dylan Barrett
//Description
//9/23/24

public class Duty extends Assignment {

    public static final String UNFILLED = "UNFILLED";
    public static final String FROSH_PASCACK = "FROSH_PASCACK";
    public static final String HALL = "HALL";
    public static final String PASCACK = "PASCACK";
    public static final String COVERAGE = "COVERAGE";

    private String room;





    public Duty(String period, String semester, String name, String room, String day, Teacher teacher, boolean adjustPeriod) {
        super(period, semester, name, day, teacher, adjustPeriod);

        this.room = room;

    }

    @Override
    public String getRoom() {
        return room;
    }

    public double getWeight() {

        if(getName().equals(UNFILLED)) return 0;
        if(getName().equals(FROSH_PASCACK)) return -10;
        if(getName().equals(HALL)) return 5;
        if(getName().equals(PASCACK)) return -5;
        if(getName().equals(COVERAGE)) return 10;
        return 1000000;

    }

    @Override
    public String getDepartment() {
        return "None";
    }
}
