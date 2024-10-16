//Dylan Barrett
//Description
//9/23/24

public class Free extends Assignment {

    public Free(int period, String semester, String name, int day, Teacher teacher) {
        super(period, semester, name, day, teacher);

    }


    @Override
    public String getRoom() {
        return "Any";
    }

    @Override
    public double getWeight() {
        return 0;
    }
}
