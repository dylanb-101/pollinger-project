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





    public Duty(int period, int semester, String name, String room, int day, Teacher teacher) {
        super(period, semester, name, day, teacher);

        this.room = room;

    }

    @Override
    public String getRoom() {
        return room;
    }

    public double getWeight() {

        if(getName() == UNFILLED) return 0;
        if(getName() == FROSH_PASCACK) return -10;
        if(getName() == HALL) return 5;
        if(getName() == PASCACK) return -5;
        if(getName() == COVERAGE) return 10;
        return 1000000;

    }
}
