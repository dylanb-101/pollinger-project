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


    private final Period period;
    private final String semester; // do we really need this?
    private final String name;
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
		return " [period=" + period + ", semester=" + semester + ", name=" + name + ", day=" + day + ", teacher=" + (teacher != null ? teacher.getLastName() : "None")  + "]";
	}

    public abstract String getDepartment();

    public boolean isDuringSchool() {
        return period.getValue() != Period.P0_NUM && period.getValue() != Period.AFTER_SCHOOL_NUM;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

}
