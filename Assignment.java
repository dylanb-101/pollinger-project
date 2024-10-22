//Dylan Barrett
//Description
//9/23/24

public abstract class Assignment {
    public static String P0 = "0";
    public static String P1 = "1";
    public static String P2 = "2";
    public static String P3 = "3";
    public static String P4 = "4";
    public static String LUNCH = "L";
    public static String P5 = "5";
    public static String P6 = "6";
    public static String P7 = "7";
    public static String P8 = "8";
    public static String AFTER_SCHOOL = "9";


    private Period period;
    private String semester; // do we really need this?
    private String name;
    private String day;

    private Teacher teacher;

//    public Assignment(String period, String semester, String name, String day, Teacher teacher) {
//        this.period = new Period(period, day);
//        this.semester = semester;
//        this.name = name;
//        this.day = day;
//        this.teacher = teacher;
//    }
//
//    public Assignment(int period, String semester, String name, String day, Teacher teacher) {
//        this.period = new Period(period, day);
//        this.semester = semester;
//        this.name = name;
//        this.day = day;
//        this.teacher = teacher;
//    }

    public Assignment(int period, String semester, String name, String day, Teacher teacher, boolean adjustPeriod) {
        if(adjustPeriod) this.period = new Period(period, day);
        else this.period = new Period(period);
        this.semester = semester;
        this.name = name;
        this.day = day;
        this.teacher = teacher;
    }

    public Assignment(String period, String semester, String name, String day, Teacher teacher, boolean adjustPeriod) {
        if(adjustPeriod) this.period = new Period(period, day);
        else this.period = new Period(period);
        this.semester = semester;
        this.name = name;
        this.day = day;
        this.teacher = teacher;
    }
    
    public Assignment(String period, String semester, String name) {
    	this.period = new Period(period);
    	this.semester = semester;
    	this.name = name;
    }

    public Period getPeriod() {
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

    public abstract String getDepartment();

}
