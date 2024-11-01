//Dylan Barrett
//Description
//9/16/24

import java.util.ArrayList;

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

    private static boolean debug;

    
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
        throw new Error("There is no next empty period!");
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

            System.out.println(assignment);

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

    public ArrayList<Assignment> getFreesDuringSchool(String day) {
        ArrayList<Assignment> frees = new ArrayList<>();

        for(Assignment a : getDayAssignments(day)) {
            if(a instanceof Free && a.getPeriod().getValue() != Period.AFTER_SCHOOL_NUM && a.getPeriod().getValue() != Period.P0_NUM) frees.add(a);
        }

        return frees;
    }

    public ArrayList<Assignment> getFreesDuringSchool() {
        ArrayList<Assignment> frees = new ArrayList<>();

        for(Assignment a : getAssignments()) {
            if(a instanceof Free && a.getPeriod().getValue() != Period.AFTER_SCHOOL_NUM && a.getPeriod().getValue() != Period.P0_NUM) frees.add(a);
        }

        return frees;
    }

    public Assignment getPeriodBefore(Assignment assignment) {

        return getAssignment(assignment.getDay(), new Period(assignment.getPeriod().increment(-1, assignment.getDay())));

    }

    public Assignment getPeriodAfter(Assignment assignment) {

        return getAssignment(assignment.getDay(), new Period(assignment.getPeriod().increment(1, assignment.getDay())));


    }

    public Assignment getTwoPeriodsAfter(Assignment assignment) {

        return getAssignment(assignment.getDay(), new Period(assignment.getPeriod().increment(2, assignment.getDay())));


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

        int count = 0;

        boolean inSecondHalf = assignment.getPeriod().greaterThan(-2);

        for(int i = (inSecondHalf ? 5 : 1); i <= (inSecondHalf ? 8 : 4); i++) {

            if(getAssignment(assignment.getDay(), new Period(i)) instanceof Free) count++;

        }

        return count;

    }

    public int freesInOtherHalf(Assignment assignment) {

        int count = 0;

        boolean inSecondHalf = assignment.getPeriod().greaterThan(-2);

        for(int i = (inSecondHalf ? 1 : 5); i <= (inSecondHalf ? 4 : 8); i++) {

            if(getAssignment(assignment.getDay(), new Period(i)) instanceof Free) count++;

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

        boolean debug = false;

        int score = adjustForSocialCredit();

        if(debug) System.out.println(score);

        if(assignment instanceof Course) return -1000000;

        if(hasDuty(assignment.getDay())) score += -100;
        if(debug) System.out.println(score);

        if(getFreesDuringSchool(assignment.getDay()).size() <= 1) score += -100;
        if(debug) System.out.println(score);

//        if(assignment.getPeriod().getValue() == 1) score -= 20;

        score += freesInHalf(assignment)*10;
        if(debug) System.out.println(score + " " + freesInHalf(assignment));

//        if(freesInHalf(assignment) == 1) score += 0;
//
//        if(freesInHalf(assignment) == 2) score += 30;
//
//        if(freesInHalf(assignment) == 3) score += 50;
//
//        if(freesInHalf(assignment) == 4) score += 100;

        score += -freesInOtherHalf(assignment)*10;
        debug(score + ", " + freesInOtherHalf(assignment));

//        if(freesInOtherHalf(assignment) == 2) score += -20;
//
//        if(freesInOtherHalf(assignment) == 3) score += -50;
//
//        if(freesInOtherHalf(assignment) == 4) score += -100;

        if(getPeriodBefore(assignment).getPeriod().getValue() == 0) score += -10;
        debug(score);

        if(getPeriodAfter(assignment).getPeriod().getValue() == 9) score += -20;
        debug(score);

        if(touchesFree(assignment)) score += 20;
        debug(score);

        if(inBetweenFrees(assignment)) score += 15;
        debug(score);

        if(beforeTwoFrees(assignment)) score += 30;
        debug(score + "" + beforeTwoFrees(assignment));

        if(afterTwoFrees(assignment)) score += 30;
        debug(score + "" + afterTwoFrees(assignment));

        if(touchesLunch(assignment)) score += 20;
        debug(score);

        if(assignment.getDay() == "M") score += 20;
        debug(score);


        score += -totalDuties()*20;
        debug(score);

        return score;

    }

    private boolean touchesLunch(Assignment assignment)
    {
        if(getPeriodBefore(assignment).getPeriod().isLunch() || getPeriodAfter(assignment).getPeriod().isLunch())
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

        return (getPeriodBefore(assignment) instanceof Free || getPeriodBefore(assignment) instanceof Lunch) && (getTwoPeriodsBefore(assignment) instanceof Free || getTwoPeriodsBefore(assignment) instanceof Lunch);
    }


    public Assignment getTwoPeriodsBefore(Assignment assignment) {

        return getAssignment(assignment.getDay(), new Period(assignment.getPeriod().increment(-2, assignment.getDay())));


    }



    public int rankAssignment(String day, Period period) {

        return rankAssignment(getAssignment(day, period));

    }

    public int scoreAssignment(String day, Period period) {

        return scoreAssignment(getAssignment(day, period));

    }

    public int scoreAssignment(Assignment assignment) {

        int score = rankAssignment(assignment);

//        double multi = Math.abs((score - getMeanScore())/getScoreStandardDeviation());

//        double multi = Math.abs(Math.abs(score * 1.0)/ getMeanScore());

        double multi = 1;

        debug(score+ "*" + multi + "=" + ((int) (score * multi)));

        return (int) (score * multi);

    }

    public double getScoreStandardDeviation() {

        int n = getFreesDuringSchool().size();

        double sum = 0;

        for(int i = 0; i < n; i++) {

            Assignment a = getFreesDuringSchool().get(i);

            sum += Math.pow(rankAssignment(a) - getMeanScore(), 2);

        }

        return Math.sqrt(sum/n);

    }



    public int getMeanScore() {

        int sum = 0;

        for(Assignment a : getFreesDuringSchool()) {

            sum += rankAssignment(a);

        }

        return sum/getFreesDuringSchool().size();

    }

    /**
     * Ranks all the free periods that the teacher has
     * @return a sorted ArrayList of ScoredAssignments that the teacher has from best to worst
     */
    public ArrayList<ScoredPeriod> rankFrees() {

        ArrayList<ScoredPeriod> asses = new ArrayList<>();

        for(Assignment a : getFreesDuringSchool()) {

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

    /**
     *
     * @param newAssignment the new assignment that is being added
     * @param oldAssignment the old assignment that is being removed
     */
    public void replaceAssignment(Assignment newAssignment, Assignment oldAssignment) {

        replaceAssignment(newAssignment, oldAssignment.getPeriod(), oldAssignment.getDay());

    }

    /**
     *
     * @param newAssignment the new assignment that is being added
     * @param period the period of the day of the assignment that is being removed
     * @param day the day of the assignment that is being removed
     */
    public void replaceAssignment(Assignment newAssignment, Period period, String day) {

        Assignment a = getAssignment(day, period);

        assignments.remove(a);

        assignments.add(newAssignment);

    }

    public void printScoredSchedule() {

        String[] days = {"M", "T", "W", "R", "F"};

        String out = "#----M----#----T----#----W----#----R----#----F----#\n";

        for(int i = 0; i <= 9; i++) {

            String nextLine = i + "   ...   #   ...   #   ...   #   ...   #   ...   #\n";
            for(String day : days) {

                int index = nextLine.indexOf("...");
                String text = "---";

                if(getAssignment(day, new Period(i)) instanceof Course) {

                    text = "CCC";

                } else if(getAssignment(day, new Period(i)) instanceof Free && getAssignment(day, new Period(i)).isDuringSchool()) {

                    text = scoreAssignment(day, new Period(i)) + "";

                    while(text.length() < 3) {
                        text = 0 + text;
                    }

                    if(text.length() > 3) text = text.substring(0, 3);

                } else if(getAssignment(day, new Period(i)) instanceof Lunch) {
                    text = "LLL";
                } else if(getAssignment(day, new Period(i)) instanceof Duty) {
                    text = "DDD";
                }

                nextLine = nextLine.substring(0, index) + text + nextLine.substring(index + 3);

            }
            out += nextLine;
            out += "#---------#---------#---------#---------#---------#\n";


        }

        System.out.println(out);


    }

    public static void debug(String text) {

        if(debug) System.out.println(text);

    }

    public static void debug(int text) {
        debug(text + "");
    }


}
