//Dylan Barrett
//Description
//9/16/24

import java.util.ArrayList;
import java.util.Arrays;

public class Teacher implements Comparable {

    private static int nextId = 0;
    private String firstName;
    private String lastName;
    private ArrayList<Assignment> assignments;
//    private ArrayList<Assignment> courses;
    private int id;
    private int socialCredit;
    private String department;
    private String room;
    private boolean isAvailable;

    
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


            String[] periods = assignment.getDay().split(",");

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


                    String period = String.valueOf(day.charAt(0));

                    if(period.equals("-1")) {
                        System.out.println("Skipping an assignment, class does not run on specified day!");
                        continue;
                    }

                    Assignment lab = new Course(assignment.getName() + " Lab", ((Course) assignment).getCourseCode(), ((Course) assignment).getSection(), period, String.valueOf(day.charAt(1)), assignment.getSemester(), assignment.getRoom(), ((Course) assignment).getDepartment(), assignment.getTeacher(), true);

                    newAssignments.add(lab);
                    continue;
                }


//                check for Lunch Labs
                if(periods.length > 1 && (day.length() == 2 || day.contains("Lab")) && !assignment.getName().contains("AP")) {

                    if(day.contains("Lab")) {
                        day = day.substring(4);
                    } else {
                    }

//                    int period = BigDuty.translatePeriod(Integer.parseInt(String.valueOf(day.charAt(0))), String.valueOf(day.charAt(1)));

                    Assignment lunchLab = new Course(assignment.getName() + " Lunch Lab", ((Course) assignment).getCourseCode(), ((Course) assignment).getSection(), Assignment.LUNCH, String.valueOf(day.charAt(1)), assignment.getSemester(), assignment.getRoom(), ((Course) assignment).getDepartment(), assignment.getTeacher(), true);

                    newAssignments.add(lunchLab);
                    continue;
                }

                String monNumber = String.valueOf(day.charAt(0));

                for(int j = 1; j < day.length(); j++) {

                    String translatedPeriod = monNumber;

                    if(monNumber.equals("-1")) continue;

                    Assignment course = new Course(assignment.getName(), ((Course) assignment).getCourseCode(), ((Course) assignment).getSection(), translatedPeriod, String.valueOf(day.charAt(j)), assignment.getSemester(), assignment.getRoom(), ((Course) assignment).getDepartment(), assignment.getTeacher(), true);
                    newAssignments.add(course);

                }
            }
        }

        assignments = newAssignments;

//        fill in lunch and frees

        String[] days = {"M", "T", "W", "R", "F"};


        for(int i = 0; i < days.length; i++) {

            String day = days[i];

            if(!dayHasLunchPeriod(day)) {

                assignments.add(new Lunch(Assignment.LUNCH, "FY", "Lunch", days[i], this));

            }



            int count = 0;

            while(!dayIsFull(day)) {
                count++;

                Period period = getNextEmptyPeriod(day);

                if(period.getValue() != -1) {
                    Assignment free = new Free(period,"FY", "Free", day, this);
                    assignments.add(free);
                }

                if(count > 9) {
                    printAssignments(day);
                }

            }
        }

    }

    public boolean dayIsFull(String day) {

        String schedule = BigDuty.schedule.get(day+"_SCHEDULE");

        for(int i = 0; i < schedule.length(); i++) {

            if(schedule.charAt(i) == '-') continue;

            Period period = new Period(schedule.charAt(i) +"", day);

            if(!dayAssignmentExists(day, period)) return false;

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

            if(a.getPeriod().isLunch()) return true;

        }

        return false;

    }

    public Period getNextEmptyPeriod(String day) {

        ArrayList<Assignment> dayAssignments = getDayAssignments(day);

        String schedule = BigDuty.schedule.get(day+"_SCHEDULE");

//        if(schedule.contains("0")) if(!dayAssignmentExists(day, new Period(0))) return new Period(0);

        for(int i = 0; i < schedule.length(); i++) {

            if(schedule.charAt(i) == '-') continue;

            Period p = new Period(schedule.charAt(i) + "", day);

            if(!dayAssignmentExists(day, p)) return p;

        }
        return new Period("-1");
    }

    public boolean afterLunch(String schedule, int i) {

        return i > schedule.indexOf("L");

    }

    public boolean dayAssignmentExists(String day, Period period) {

        for(Assignment a : assignments) {

            if(a.getPeriod().getValue() == period.getValue() && a.getDay().equals(day)) return true;

        }

        return false;

    }

    public void addAssignment(Assignment a)
    {
        assignments.add(a);
    }

    public ArrayList<Assignment> getDayAssignments(String day) {

        ArrayList<Assignment> daySchedule = new ArrayList<>();

        for(Assignment assignment : assignments) {
            if(assignment.getDay().equals(day)) daySchedule.add(assignment);
        }

        return daySchedule;

    }

    public Assignment getAssignment(String day, Period period) {

        for(Assignment assignment : assignments)
            if(assignment.getDay().equals(day) && assignment.getPeriod().equals(period))
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
    
    public boolean isAvailable(String day, Period period)
    {
       return isAvailable;
    }
    
    public void setAvailability(boolean isAvailable)
    {
       this.isAvailable = isAvailable;
    }

    public Assignment getPeriodBefore(Assignment assignment) {

        if(assignment.getPeriod().getValue() == 0) {
            return new Free(0, assignment.getSemester(), "Before School", assignment.getDay(), assignment.getTeacher(), false);
        }

        if(assignment.getDay().equals("M") && assignment.getPeriod().getValue() == 4) return getDayLunch(assignment.getDay());

        if(assignment.getDay().equals("M") && assignment.getPeriod().getValue() == 3) return getDayLunch(assignment.getDay());

        ArrayList<Assignment> daySchedule = getDayAssignments(assignment.getDay());

        for(Assignment ass : daySchedule) {
            if(ass.getPeriod().getValue() == ass.getPeriod().increment(-1)) return ass;
        }

        return null;
    }

    public Assignment getPeriodAfter(Assignment assignment) {

        if(assignment.getPeriod().getValue() == 8 || (assignment.getPeriod().getValue() == 7 && !assignment.getDay().equals("M"))) {
            return new Free(9, assignment.getSemester(), "After School", assignment.getDay(), assignment.getTeacher(), false);
        }

        for(Assignment ass: getDayAssignments(assignment.getDay())) {
            if(ass.getPeriod().getValue() == assignment.getPeriod().increment(1)) return ass;
        }

        return null;

    }

    public Assignment getTwoPeriodSAfter(Assignment assignment) {

        if(assignment.getPeriod().getValue() == 8 || (assignment.getPeriod().getValue() == 7 && !assignment.getDay().equals("M"))) {
            return new Free(9, assignment.getSemester(), "After School", assignment.getDay(), assignment.getTeacher(), false);
        }

        for(Assignment ass: getDayAssignments(assignment.getDay())) {
            if(ass.getPeriod().getValue() == assignment.getPeriod().increment(2)) return ass;
        }

        return null;

    }

    public Assignment getTwoPeriodSBefore(Assignment assignment) {

        if(assignment.getPeriod().getValue() == 8 || (assignment.getPeriod().getValue() == 7 && !assignment.getDay().equals("M"))) {
            return new Free(9, assignment.getSemester(), "After School", assignment.getDay(), assignment.getTeacher(), false);
        }

        for(Assignment ass: getDayAssignments(assignment.getDay())) {
            if(ass.getPeriod().getValue() == assignment.getPeriod().increment(-2)) return ass;
        }

        return null;

    }



    public boolean inBetweenFrees(Assignment assignment) {

        if((getPeriodBefore(assignment) instanceof Free || getPeriodBefore(assignment) instanceof Lunch && getPeriodBefore(assignment).getPeriod().getValue() != 0)

                && (getPeriodAfter(assignment) instanceof Free || getPeriodAfter(assignment) instanceof Lunch && getPeriodAfter(assignment).getPeriod().getValue() != 9)) return true;

        return false;

    }

    public boolean beforeTwoFrees(Assignment assignment) {

        if((!(getPeriodBefore(assignment) instanceof Free) || !(getPeriodBefore(assignment) instanceof Lunch))

                && (getPeriodAfter(assignment) instanceof Free || getPeriodAfter(assignment) instanceof Lunch && getPeriodAfter(assignment).getPeriod().getValue() != 9)) return true;

        return false;

    }

    public int freesInHalf(Assignment assignment) {

        ArrayList<Assignment> day = getDayAssignments(assignment.getDay());

        day.sort((o1, o2) -> o1.getPeriod().compareTo(o2));

        int min = 0;
        int max = day.size()/2;
        int count = 0;

        if(assignment.getPeriod().greaterThan(4)) {
            min = day.size()/2;
            max = day.size();
        }

        for(int i = min; i < max; i++) {
            if(day.get(i) instanceof Free) count++;
        }

        return count;

    }

    public int freesInOtherHalf(Assignment assignment) {

        ArrayList<Assignment> day = getDayAssignments(assignment.getDay());

        day.sort((o1, o2) -> o1.getPeriod().compareTo(o2));

        int min = day.size();
        int max = day.size()/2;
        int count = 0;

        if(assignment.getPeriod().greaterThan(4)) {
            min = 0;
            max = day.size()/2;
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

//    private int rankAssignment(Assignment assignment) {
//
//        int score = adjustForSocialCredit();
//
//        if(assignment instanceof Course) score = -1000;
//
//        if(getFrees(assignment.getDay()).size() <= 1) score = -1000;
//
//        if(assignment.getPeriod().getValue() == 1) score -= 20;
//
//        if(freesInHalf(assignment) == 1) score = 0;
//
//        if(freesInHalf(assignment) == 2) score = 30;
//
//        if(freesInHalf(assignment) == 3) score = 50;
//
//        if(freesInHalf(assignment) == 4) score = 100;
//
//        if(freesInOtherHalf(assignment) == 2) score -= 20;
//
//        if(freesInOtherHalf(assignment) == 3) score -= 50;
//
//        if(freesInOtherHalf(assignment) == 4) score -= 100;
//
//        if(getPeriodBefore(assignment).getPeriod().getValue() == 0) score -= 20;
//
//        if(getPeriodAfter(assignment).getPeriod().getValue() == 9) score -= 10;
//
//        if(touchesFree(assignment)) score += 50;
//
//        if(inBetweenFrees(assignment)) score += 50;
//
//        if(beforeTwoFrees(assignment)) score += 50;
//
//
//        score -= totalDuties()*20;
//
//        return score;
//
//    }

    private int rankAssignment(Assignment assignment) {
       
       if(!isAvailable(assignment.getDay(), assignment.getPeriod()))
          return -1000;

        if(hasDuty(assignment.getDay()))
            return -1000;

        int score = adjustForSocialCredit();

        if(assignment instanceof Course) score = -1000;

        if(getFrees(assignment.getDay()).size() <= 1) score = -1000;

//        if(assignment.getPeriod().getValue() == 1) score -= 20;

        if(freesInHalf(assignment) == 1) score = 0;

        if(freesInHalf(assignment) == 2) score = 30;

        if(freesInHalf(assignment) == 3) score = 50;

        if(freesInHalf(assignment) == 4) score = 100;

        if(freesInOtherHalf(assignment) == 2) score -= 20;

        if(freesInOtherHalf(assignment) == 3) score -= 50;

        if(freesInOtherHalf(assignment) == 4) score -= 100;

        if(getPeriodBefore(assignment).getPeriod().getValue() == 0) score -= 20;

        if(getPeriodAfter(assignment).getPeriod().getValue() == 9) score -= 10;

        if(touchesFree(assignment)) score += 50;

        if(inBetweenFrees(assignment)) score += 50;

        if(beforeTwoFrees(assignment)) score += 50;

        if(afterTwoFrees(assignment)) score +=50;

        if(touchesLunch(assignment)) score -=50;

        score -= totalDuties()*20;

        return score;

    }

    private boolean touchesLunch(Assignment assignment)
    {
        if(getPeriodBefore(assignment) instanceof Lunch || getPeriodAfter(assignment) instanceof Lunch)
            return true;
        return false;
    }


    public boolean touchesFree(Assignment assignment) {

        if(getPeriodBefore(assignment) instanceof Free || getPeriodBefore(assignment) instanceof Lunch && getPeriodBefore(assignment).getPeriod().getValue() != 0) return true;

        if(getPeriodAfter(assignment) instanceof Free || getPeriodAfter(assignment) instanceof Lunch && getPeriodAfter(assignment).getPeriod().getValue() != 9) return true;

        return false;

    }

    private boolean hasDuty(String day)
    {
        for(Assignment a : assignments)
            if(a.getDay().equals(day) && a instanceof Duty)
                return true;
        return false;
    }

    public boolean afterTwoFrees(Assignment assignment) {

        if((!(getPeriodAfter(assignment) instanceof Free) || !(getPeriodAfter(assignment) instanceof Lunch))
                && (getPeriodBefore(assignment) instanceof Free || getPeriodBefore(assignment) instanceof Lunch && getPeriodBefore(assignment).getPeriod().getValue() != 0)
                && (getTwoPeriodsBefore(assignment) instanceof Free || getTwoPeriodsBefore(assignment) instanceof Lunch) && (getTwoPeriodsBefore(assignment).getPeriod().getValue() != 0))
            return true;

        return false;

    }


    public Assignment getTwoPeriodsBefore(Assignment assignment) {

        if(assignment.getPeriod().getValue() == 8 || (assignment.getPeriod().getValue() == 7 && !assignment.getDay().equals("M"))) {
            return new Free(9, assignment.getSemester(), "After School", assignment.getDay(), assignment.getTeacher(), false);
        }

        for(Assignment ass: getDayAssignments(assignment.getDay())) {
            if(ass.getPeriod().getValue() == assignment.getPeriod().increment(-2)) return ass;
        }

        return null;

    }



    public int rankAssignment(String day, Period period) {

        return rankAssignment(getAssignment(day, period));

    }

    public int scoreAssignment(String day, Period period) {

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

    public void printAssignments(String day) {


        for(Assignment assignment : getDayAssignments(day)) {
            System.out.println(assignment.toString());
        }
    }

    @Override
    public int compareTo(Object o) {

        Teacher teacher = (Teacher) o;

        String n1 = lastName + " " + firstName;
        String n2 = teacher.getLastName() + " " + firstName;

        return n1.compareTo(n2);
    }

    public void replaceAssignment(Assignment assignment) {



    }

//    public void addCourse(Course course) {
//		courses.add(course);
//	}


}
