//Dylan Barrett
//Description
//9/26/24

public class Lunch extends Assignment {


    public Lunch(String period, String semester, String name, String day, Teacher teacher) {
        super(period, semester, name, day, teacher, false);
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
    public String getDepartment() {
        return "None";
    }
}
