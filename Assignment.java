//Dylan Barrett
//Description
//9/23/24

public abstract class Assignment {
    public static int P0 = 0;
    public static int P1 = 1;
    public static int P2 = 2;
    public static int P3 = 3;
    public static int P4 = 4;
    public static int LUNCH = 5;
    public static int P5 = 6;
    public static int P6 = 7;
    public static int P7 = 8;
    public static int P8 = 9;
    public static int AFTER_SCHOOL = 10;


    private int period;
    private String semester; // do we really need this?
    private String name;
    private String day;

    private Teacher teacher;

    public Assignment(int period, String semester, String name, String day, Teacher teacher) {
        this.period = period;
        this.semester = semester;
        this.name = name;
        this.day = day;
        this.teacher = teacher;
    }
    
    public Assignment(int period, String semester, String name) {
    	this.period = period;
    	this.semester = semester;
    	this.name = name;
    }

    public int getPeriod() {
        return period;
    }

    public String getSemester() {
        return semester;
    }

    public String getName() {
        return name;
    }

    public abstract String getRoom();

    public String getDay() {
        return day;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public abstract double getWeight();

    @Override
	public String toString() {
		return "Assignment [period=" + period + ", semester=" + semester + ", name=" + name + ", day=" + day + "]";
	}

}
