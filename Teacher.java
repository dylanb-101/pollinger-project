//Dylan Barrett
//Description
//9/16/24

import java.util.ArrayList;
import java.util.Comparator;

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
    }
    
    public Teacher(String lastName, String firstName, String department, String room) {
    	this.id = nextId++;
    	this.firstName = firstName;
    	this.lastName = lastName;
    	this.department = department;
    	this.room = room;
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
    
    public ArrayList<Assignment> getDayAssignments(int day) {

        ArrayList<Assignment> daySchedule = new ArrayList<>();

        for(Assignment assignment : assignments) {
            if(assignment.getDay() == day) daySchedule.add(assignment);
        }

        return daySchedule;

    }
    
    public Assignment getAssignment(int day, int period) {

        for(Assignment assignment : assignments)
            if(assignment.getDay() == day && assignment.getPeriod() == period)
                return assignment;

        return null;
    }



	public Lunch getDayLunch(int day) {
        for(Assignment assignment : assignments) {
            if(assignment instanceof Lunch && assignment.getDay() == day) return (Lunch) assignment;
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

    public ArrayList<Assignment> getFrees(int day) {

        ArrayList<Assignment> frees = new ArrayList<>();

        for(Assignment assignment : getDayAssignments(day)) {
            if(assignment instanceof Free) frees.add((Free) assignment);
        }

        return frees;

    }

    public Assignment getPeriodBefore(Assignment assignment) {

        if(assignment.getPeriod() == 1) {
            return new Free(0, assignment.getSemester(), "Before School", assignment.getDay(), assignment.getTeacher());
        }

        if(assignment.getDay() == 1 && assignment.getPeriod() == 4) return getDayLunch(assignment.getDay());

        if(assignment.getDay() != 1 && assignment.getPeriod() == 3) return getDayLunch(assignment.getDay());

        ArrayList<Assignment> daySchedule = getDayAssignments(assignment.getDay());

        for(Assignment ass : daySchedule) {
            if(ass.getPeriod() == assignment.getPeriod()-1) return ass;
        }

        return null;
    }

    public Assignment getPeriodAfter(Assignment assignment) {

        if(assignment.getPeriod() == 8 || (assignment.getPeriod() == 7 && assignment.getDay() == 1)) {
            return new Free(9, assignment.getSemester(), "After School", assignment.getDay(), assignment.getTeacher());
        }

        for(Assignment ass: getDayAssignments(assignment.getDay())) {
            if(ass.getDay() == assignment.getDay()+1) return ass;
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
    
    public int rankAssignment(int day, int period) {

        return rankAssignment(getAssignment(day, period));

    }
    
    public int scoreAssignment(int day, int period) {

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
    
//    public void addCourse(Course course) {
//		courses.add(course);
//	}


}
