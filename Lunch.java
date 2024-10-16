//Dylan Barrett
//Description
//9/26/24

public class Lunch extends Assignment {


    public Lunch(int period, String semester, String name, String day, Teacher teacher) {
        super(period, semester, name, day, teacher);
    }

    @Override
    public String getRoom() {
        return "Lunch";
    }

    @Override
    public double getWeight() {
        return -10000;
    }

    @Override
    public Assignment dupeAssignment(String day, int period) {
        return new Lunch(period, getSemester(), getName(), day, getTeacher());
    }
}
