//Dylan Barrett
//Description
//9/23/24

public class Free extends Assignment {

    public Free(int period, String semester, String name, String day, Teacher teacher, boolean adjustPeriod) {
        super(period, semester, name, day, teacher, adjustPeriod);

    }

    public Free(Period period, String semester, String name, String day, Teacher teacher) {
        super(period.getValue(), semester, name, day, teacher, false);
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
}
