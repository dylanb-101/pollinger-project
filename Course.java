//Dylan Barrett
//Description
//9/16/24

public class Course extends Assignment {

    private String courseCode;
    private int section;
    private String department;
    private String room;
    
    public Course(String name, String courseCode, int section, String period, String day,
                  String semester , String room, String department, Teacher teacher, boolean adjustPeriod) {
        super(period, semester, name, day, teacher, adjustPeriod);
        this.courseCode = courseCode;
        this.section = section;
        this.department = department;
        this.room = room;
    }

//	public Course(int period, String semester, String course, String room, String name) {
//		super(period, semester, name);
//		this.room = room;
//	    this.courseCode = course;
//		
//	}
	
//	public Course(int period, String semester, String course, String room, String courseName)
//	{
//		this.period = period;
//		this.semester = semester;
//		this.course = course;
//		this.room = room;
//		this.courseName = courseName;
//	}

	public String getRoom() {
        return room;
    }

    @Override
    public double getWeight() {
        return -10000;
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
