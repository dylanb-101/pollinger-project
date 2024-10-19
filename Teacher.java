//Dylan Barrett
//Description
//9/16/24

import java.util.ArrayList;
import java.util.Arrays;

public class Teacher {

    private static int nextId = 0;
    private String firstName;
    private String lastName;
    private ArrayList<Assignment> assignments;
//    private ArrayList<Assignment> courses;
    private int id;
    private int socialCredit;
    private String department;
    private String room;

    
    public Teacher(String lastName, String firstName) {
        this.id = nextId++;
        this.firstName = firstName;
        this.lastName = lastName;
        this.assignments = new ArrayList<>();
        this.socialCredit = 0;

    }
    
    public Teacher(String lastName, String firstName, String department, String room) {
    	this.id = nextId++;
    	this.firstName = firstName;
    	this.lastName = lastName;
    	this.department = department;
    	this.room = room;
        this.assignments = new ArrayList<>();
        this.socialCredit = 0;


    }

    public void fillInAssignments() {

        ArrayList<Assignment> newAssignments = new ArrayList<>();

        for(Assignment assignment : assignments) {

//            System.out.println(assignment.getName() + ", " + assignment.getDay());

            String[] periods = assignment.getDay().split(",");
//            System.out.println(Arrays.toString(periods));

            for(int i = 0; i < periods.length;i++) {

                String day = periods[i];

                if(day.charAt(0) == ' ') day = day.substring(1);

                if(day.contains("-")) {
                    System.out.println("Skipping an assignment, contains a dash");
                    continue;
                }

                if(assignment.getName().contains("TA")) {
                    System.out.println("Skipping an assignment, is a TA class");
                    continue;
                }


//                checks if there is an ap science class lab
                if(periods.length > 1 && assignment.getName().contains("AP") && (day.length() == 2)) {

//                    System.out.println("Lab! " + assignment.getName());

                    int period = BigDuty.translatePeriod(Integer.parseInt(String.valueOf(day.charAt(0))), String.valueOf(day.charAt(1)));

                    if(period == -1) {
                        System.out.println("Skipping an assignment, class does not run on specified day!");
                        continue;
                    }

                    Assignment lab = new Course(assignment.getName() + " Lab", ((Course) assignment).getCourseCode(), ((Course) assignment).getSection(), period, String.valueOf(day.charAt(1)), assignment.getSemester(), assignment.getRoom(), ((Course) assignment).getDepartment(), assignment.getTeacher());

                    newAssignments.add(lab);
                    continue;
                }


//                check for Lunch Labs
                if(periods.length > 1 && (day.length() == 2 || day.contains("Lab")) && !assignment.getName().contains("AP")) {

                    if(day.contains("Lab")) {
                        day = day.substring(3);
                    }

//                    int period = BigDuty.translatePeriod(Integer.parseInt(String.valueOf(day.charAt(0))), String.valueOf(day.charAt(1)));

                    Assignment lunchLab = new Course(assignment.getName() + " Lunch Lab", ((Course) assignment).getCourseCode(), ((Course) assignment).getSection(), Assignment.LUNCH, String.valueOf(day.charAt(1)), assignment.getSemester(), assignment.getRoom(), ((Course) assignment).getDepartment(), assignment.getTeacher());

                    newAssignments.add(lunchLab);
                    continue;
                }

                int monNumber = Integer.parseInt(String.valueOf(day.charAt(0)));

                for(int j = 1; j < day.length(); j++) {

//                    System.out.println(day + ", " + j);

                    int translatedPeriod = BigDuty.translatePeriod(monNumber, String.valueOf(day.charAt(j)));

                    if(translatedPeriod == -1) continue;

                    Assignment course = new Course(assignment.getName(), ((Course) assignment).getCourseCode(), ((Course) assignment).getSection(), translatedPeriod, String.valueOf(day.charAt(j)), assignment.getSemester(), assignment.getRoom(), ((Course) assignment).getDepartment(), assignment.getTeacher());
                    newAssignments.add(course);

                }
            }
        }

        assignments = newAssignments;

//        fill in lunch and frees

        String[] days = {"M", "T", "W", "R", "F"};

//        for(int i = 0; i < days.length; i++) {
//
//            if(!dayHasLunchPeriod(days[i])) {
//
//                newAssignments.add(new Lunch(Assignment.LUNCH, "FY", "Lunch", days[i], this));
//
//            }
//
//
//
//            while(!dayIsFull(days[i])) {
//
////                System.out.println(dayIsFull(days[i]));
//
//                int period = getNextEmptyPeriod(days[i]);
//
////                if(period == -1)
//
//                Assignment free = new Free(period,"FY", "Free", days[i], this);
//                newAssignments.add(free);
//
//            }
//        }

    }

    public boolean dayIsFull(String  day) {

        ArrayList<Assignment> dayAssigments = getDayAssignments(day);

//        if(dayAssigments.size() > BigDuty.schedule.get(day+"_SCHEDULE").length()) {
//            System.out.println(dayAssigments.size() + ", " + BigDuty.schedule.get(day + "_SCHEDULE").length());
//        }

        String schedule = BigDuty.schedule.get(day+"_SCHEDULE");

        for(int i = 0; i < schedule.length();i++) {

            if(!dayAssignmentExists(day, String.valueOf(schedule.charAt(i)))) return false;

        }

        return true;
    }

    public ArrayList<Assignment> getDayAssignments(ArrayList<Assignment> assignments, String day) {

        ArrayList<Assignment> dayAssignments = new ArrayList<>();

        for(Assignment a : assignments) {

            if(a.getDay().equals(day))  dayAssignments.add(a);

        }

        return dayAssignments;
    }

    public boolean dayHasLunchPeriod(String day) {

        ArrayList<Assignment> dayAssigments = getDayAssignments(day);

        for(Assignment a : dayAssigments) {

            if(a.getPeriod() == Assignment.LUNCH) return true;

        }

        return false;

    }

    public int getNextEmptyPeriod(String day) {

        ArrayList<Assignment> dayAssigments = getDayAssignments(day);

        String schedule = BigDuty.schedule.get(day+"_SCHEDULE");

//        if(schedule.contains("0")) if(!dayAssignmentExists(day, 0)) return 0;

        for(int i = 0; i < schedule.length(); i++) {

            String period = String.valueOf(schedule.indexOf(i));

            if(period.equals("-1")) continue;

            if(period.equals("L")) if(!dayAssignmentExists(day, "" + Assignment.LUNCH)) return Assignment.LUNCH;

            if(!dayAssignmentExists(day, "" + BigDuty.translatePeriod(Integer.parseInt(period), day))) return BigDuty.translatePeriod(Integer.parseInt(period), day);

        }
        return -1;
    }

    public boolean afterLunch(String schedule, int i) {

        return i > schedule.indexOf("L");

    }

    public boolean dayAssignmentExists(String day, String period) {

        for(Assignment a : assignments) {

            if(String.valueOf(a.getPeriod()).equals(period) && a.getDay().equals(day)) return true;

        }

        return false;

    }

    
    
    public void addAssignment(Assignment a)
    {
    	assignments.add(a);
    }

//    public Teacher(String name, ArrayList<Assignment> courses) {
//    	this.id = previousId++;
//    	firstName = name.substring(1, name.indexOf(','));
//    	lastname = name.substring(name.indexOf(' '+1), -1);
//    	this.assignments = new ArrayList<Assignment>();
//    	this.assignments.addAll(courses);
//    }
//    
    
    public ArrayList<Assignment> getDayAssignments(String day) {

        ArrayList<Assignment> daySchedule = new ArrayList<>();

        for(Assignment assignment : assignments) {
            if(assignment.getDay().equals(day)) daySchedule.add(assignment);
        }

        return daySchedule;

    }
    
    public Assignment getAssignment(String day, int period) {

        for(Assignment assignment : assignments)
            if(assignment.getDay().equals(day) && assignment.getPeriod() == period)
                return assignment;

        return null;
    }



	public Lunch getDayLunch(String day) {
        for(Assignment assignment : assignments) {
            if(assignment instanceof Lunch && assignment.getDay().equals(day)) return (Lunch) assignment;
        }

        return null;
    }

    public ArrayList<Assignment> getFrees() {

        ArrayList<Assignment> frees = new ArrayList<>();

        for(Assignment assignment : assignments) {
            if(assignment instanceof Free) frees.add(assignment);
        }

        return frees;
    }

    public ArrayList<Assignment> getFrees(String day) {

        ArrayList<Assignment> frees = new ArrayList<>();

        for(Assignment assignment : getDayAssignments(day)) {
            if(assignment instanceof Free) frees.add(assignment);
        }

        return frees;

    }

    public Assignment getPeriodBefore(Assignment assignment) {

        if(assignment.getPeriod() == 1) {
            return new Free(0, assignment.getSemester(), "Before School", assignment.getDay(), assignment.getTeacher());
        }

        if(assignment.getDay().equals("M") && assignment.getPeriod() == Assignment.P4) return getDayLunch(assignment.getDay());

        if(assignment.getDay().equals("M") && assignment.getPeriod() == Assignment.P3) return getDayLunch(assignment.getDay());

        ArrayList<Assignment> daySchedule = getDayAssignments(assignment.getDay());

        for(Assignment ass : daySchedule) {
            if(ass.getPeriod() == assignment.getPeriod()-1) return ass;
        }

        return null;
    }

    public Assignment getPeriodAfter(Assignment assignment) {

        if(assignment.getPeriod() == 8 || (assignment.getPeriod() == 7 && !assignment.getDay().equals("M"))) {
            return new Free(9, assignment.getSemester(), "After School", assignment.getDay(), assignment.getTeacher());
        }

        for(Assignment ass: getDayAssignments(assignment.getDay())) {
            if(ass.getPeriod() == assignment.getPeriod()+1) return ass;
        }

        return null;

    }

    public boolean touchesFree(Assignment assignment) {

        if(getPeriodBefore(assignment) instanceof Free || getPeriodBefore(assignment) instanceof Lunch && getPeriodBefore(assignment).getPeriod() != 0) return true;

        if(getPeriodAfter(assignment) instanceof Free || getPeriodAfter(assignment) instanceof Lunch && getPeriodAfter(assignment).getPeriod() != 9) return true;

        return false;

    }

    public int freesInHalf(Assignment assignment) {

        ArrayList<Assignment> day = getDayAssignments(assignment.getDay());

        day.sort((o1, o2) -> o1.getPeriod() - o2.getPeriod());

        int min = 0;
        int max = day.size()/2;
        int count = 0;

        if(assignment.getPeriod() > 4) {
            min = day.size()/2;
            max = day.size();
        }

        for(int i = min; i < max; i++) {
            if(day.get(i) instanceof Free) count++;
        }

        return count;

    }

    public int totalDuties() {

        int sum = 0;

        for(Assignment a : assignments) {
            if(a instanceof Duty) sum+=1;
        }

        return sum;
    }

    private int rankAssignment(Assignment assignment) {

        int score = adjustForSocialCredit();

        if(assignment instanceof Course) score = -1000;

        if(getFrees(assignment.getDay()).size() <= 1) score = -1000;

        if(freesInHalf(assignment) == 1) score = 0;

        if(freesInHalf(assignment) == 2) score = 30;

        if(freesInHalf(assignment) == 3) score = 50;

        if(freesInHalf(assignment) == 4) score = 100;

        if(getPeriodBefore(assignment).getPeriod() == 0) score -= 20;

        if(getPeriodAfter(assignment).getPeriod() == 9) score -= 10;

        if(touchesFree(assignment)) score += 50;

        score -= totalDuties()*20;

        return score;

    }
    
    public int rankAssignment(String day, int period) {

        return rankAssignment(getAssignment(day, period));

    }
    
    public int scoreAssignment(String day, int period) {

        int score = rankAssignment(day, period);

        double multi = (score * 1.0)/getAverageScore();

        return (int) (score * multi);

    }

    public int scoreAssignment(Assignment assignment) {

        int score = rankAssignment(assignment);

        double multi = (score * 1.0)/getAverageScore();

        return (int) (score * multi);

    }
    
    

    public int getAverageScore() {

        int sum = 0;

        for(Assignment a : getFrees()) {

            sum += rankAssignment(a);

        }

        return sum/getFrees().size();

    }

    /**
     * Ranks all the free periods that the teacher has
     * @return a sorted ArrayList of ScoredAssignments that the teacher has from best to worst
     */
    public ArrayList<ScoredPeriod> rankFrees() {

        ArrayList<ScoredPeriod> asses = new ArrayList<>();

        for(Assignment a : getFrees()) {

            asses.add(new ScoredPeriod(scoreAssignment(a), a));

        }

        asses.sort((o1, o2) -> o1.getScore() - o2.getScore());

        return asses;
    }

    public int adjustForSocialCredit() {

        // TO BE IMPLEMENTED: Need to adjust the Assignment score for the rankAssignment() method

        return 0;
    }

    public String getName() {

        return firstName + " " + lastName;

    }

	public static int getNextId() {
		return nextId;
	}

	public static void setNextId(int nextId) {
		Teacher.nextId = nextId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public ArrayList<Assignment> getAssignments() {
		return assignments;
	}

	public void setAssignments(ArrayList<Assignment> assignments) {
		this.assignments = assignments;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSocialCredit() {
		return socialCredit;
	}

	public void setSocialCredit(int socialCredit) {
		this.socialCredit = socialCredit;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	@Override
	public String toString() {
		return "Teacher [firstName=" + firstName + ", lastname=" + lastName + ", assignments=" + assignments + ", id="
				+ id + ", socialCredit=" + socialCredit + ", department=" + department + ", room=" + room + "]";
	}

    public void printAssignments() {

        for(Assignment assignment : assignments) {
            System.out.println(assignment.toString());
        }

    }
    
//    public void addCourse(Course course) {
//		courses.add(course);
//	}


}
