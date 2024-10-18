//Dylan Barrett
//Description
//9/16/24

public class Course extends Assignment {

    private String courseCode;
    private int section;
    private String department;
    private String room;


    public Course(int period, String semester, String name, String room, String day, Teacher teacher) {
        super(period, semester, name, day, teacher);

        this.room = room;
    }

    public String getRoom() {
        return room;
    }

    @Override
    public double getWeight() {
        return -10000;
    }

    @Override
    public Assignment dupeAssignment(String day, int period) {
        return new Course(period, getSemester(), getName(), room, day, getTeacher());
    }

    public String getCourseCode() {
        return courseCode;
    }

    public int getSection() {
        return section;
    }

    public String getDepartment() {
        return department;
    }
}
