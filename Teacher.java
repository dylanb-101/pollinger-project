//Dylan Barrett
//Description
//9/16/24

import java.util.ArrayList;
import java.util.Collections;

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
    private BigDuty bigDuty;
    private int maxDuties;

    private boolean isAvailable;

    
    public Teacher(String lastName, String firstName) {
        this.id = nextId++;
        this.firstName = firstName;
        this.lastName = lastName;
        this.assignments = new ArrayList<>();
        this.socialCredit = 0;
        this.isAvailable = true;
        this.maxDuties = 3;
    }
    
    public Teacher(String lastName, String firstName, String department, String room) {
    	this.id = nextId++;
    	this.firstName = firstName;
    	this.lastName = lastName;
    	this.department = department;
    	this.room = room;
        this.assignments = new ArrayList<>();
        this.socialCredit = 0;
        this.isAvailable = true;
        this.maxDuties = 3;

    }

    public Teacher(Teacher teacher) {
        this.id = nextId++;
        this.firstName = teacher.firstName;
        this.lastName = teacher.lastName;
        this.department = teacher.department;
        this.room = teacher.room;
        this.socialCredit = teacher.socialCredit;
        this.isAvailable = teacher.isAvailable;
        this.assignments = new ArrayList<>();
        this.maxDuties = 3;

        for(Assignment assignment : teacher.assignments) {

            Assignment a = null;

            if(assignment instanceof Course) {
                a = new Course((Course) assignment, this);
            }
            if(assignment instanceof Free) {
                a = new Free((Free) assignment, this);
            }
            if(assignment instanceof Lunch) {
                a = new Lunch((Lunch) assignment, this);
            }
            if(assignment instanceof Duty) {
                a = new Free(assignment.getPeriod(), "Free", assignment.getDay(), assignment.getSemester(), this);
            }

            assignments.add(a);

        }

    }

    public boolean isAvailable(String day, Period period)
    {
        return isAvailable;
    }

    public void setAvailability(boolean isAvailable)
    {
        this.isAvailable = isAvailable;
    }

    public int getMaxDuties() {
        return maxDuties;
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


//                checks if there is an ap science class lab
                if(periods.length > 1 && assignment.getName().contains("AP") && (day.length() == 2)) {


                    String period = String.valueOf(day.charAt(0));

                    if(period.equals("-1")) {
                        System.out.println("Skipping an assignment, class does not run on specified day!");
                        continue;
                    }

                    Assignment lab = new Course(assignment.getName() + " Lab", ((Course) assignment).getCourseCode(), ((Course) assignment).getSection(), period, String.valueOf(day.charAt(1)), assignment.getSemester(), assignment.getRoom(), assignment.getDepartment(), assignment.getTeacher(), true);

                    newAssignments.add(lab);
                    continue;
                }


//              check for Lunch Labs
                if(periods.length > 1 && (day.length() == 2 || day.contains("Lab")) && !assignment.getName().contains("AP")) {

                    if(day.contains("Lab")) {
                        day = day.substring(4);
                    } else {
                    }

//                    int period = BigDuty.translatePeriod(Integer.parseInt(String.valueOf(day.charAt(0))), String.valueOf(day.charAt(1)));

                    Assignment lunchLab = new Course(assignment.getName() + " Lunch Lab", ((Course) assignment).getCourseCode(), ((Course) assignment).getSection(), Assignment.LUNCH, String.valueOf(day.charAt(1)), assignment.getSemester(), assignment.getRoom(), assignment.getDepartment(), assignment.getTeacher(), true);

                    newAssignments.add(lunchLab);
                    continue;
                }

                String monNumber = String.valueOf(day.charAt(0));

                for(int j = 1; j < day.length(); j++) {

                    String translatedPeriod = monNumber;

                    if(monNumber.equals("-1")) continue;

                    Assignment course = new Course(assignment.getName(), ((Course) assignment).getCourseCode(), ((Course) assignment).getSection(), translatedPeriod, String.valueOf(day.charAt(j)), assignment.getSemester(), assignment.getRoom(), assignment.getDepartment(), assignment.getTeacher(), true);
                    newAssignments.add(course);

                }
            }
        }

        assignments = newAssignments;

//        fill in lunch and frees

        fillInLunchesAndFrees();

    }

    public int getAdjustedSocialCredit()
    {
        int updatedSocialCredit = socialCredit;

        for(int i = 0; i < this.getDuties().size(); i++)
        {

            Duty nextDuty = this.getDuties().get(i);

            if(nextDuty.getPeriod().getValue() == 1 || (nextDuty.getPeriod().getValue() == 8 && nextDuty.getDay().equals("M"))
                    || (nextDuty.getPeriod().getValue() == 7 && !nextDuty.getDay().equals("M")))
                updatedSocialCredit -= 2;

            if(nextDuty.getType().equals(DutyType.FROSH_PASCACK))
                updatedSocialCredit -= 10;

            if(nextDuty.getType().equals(DutyType.COVERAGE))
            {
                if(nextDuty.getPeriod().getValue() == 1 || (nextDuty.getPeriod().getValue() == 8 && nextDuty.getDay().equals("M"))
                        || (nextDuty.getPeriod().getValue() == 7 && !nextDuty.getDay().equals("M")))
                    updatedSocialCredit -= 2;
            }

            if(nextDuty.getType().equals(DutyType.HALL))
                updatedSocialCredit += 1;
        }

        return updatedSocialCredit;
    }

    public void fillInLunchesAndFrees() {
        String[] days = {"M", "T", "W", "R", "F"};


        for(int i = 0; i < days.length; i++) {

            String day = days[i];

            if(!dayHasLunchPeriod(day)) {

                assignments.add(new Lunch(Assignment.LUNCH, bigDuty.getSemester(), "Lunch", days[i], this));

            }



            int count = 0;

            while(!dayIsFull(day)) {
                count++;

                Period period = getNextEmptyPeriod(day);

                if(period.getValue() != -1) {
                    Assignment free = new Free(period, bigDuty.getSemester(), "Free", day, this);
                    assignments.add(free);
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

    public boolean dayHasLunchPeriod(String day) {

        ArrayList<Assignment> dayAssigments = getDayAssignments(day);

        for(Assignment a : dayAssigments) {

            if(a.getPeriod().isLunch() && a.semesterMatches(bigDuty.getSemester())) return true;

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

    public boolean afterLunch(Assignment assignment) {

        return assignment.getPeriod().greaterThan("L");

    }

    public boolean dayAssignmentExists(String day, Period period) {

        for(Assignment a : assignments) {

            if(a.getPeriod().getValue() == period.getValue() && a.getDay().equals(day) && a.semesterMatches(bigDuty.getSemester())) return true;

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
            if(assignment.getDay().equals(day) && assignment.semesterMatches(bigDuty.getSemester())) daySchedule.add(assignment);
        }

        return daySchedule;

    }

    public Assignment getAssignment(String day, Period period) {

        for(Assignment assignment : assignments) {


            if (assignment.getDay().equals(day) && assignment.getPeriod().equals(period) && assignment.semesterMatches(bigDuty.getSemester()))
                return assignment;
        }

        return null;
    }



    public Assignment getDayLunch(String day) {
        return getAssignment(day, new Period("L"));
    }

    public ArrayList<Free> getAllFrees() {

        ArrayList<Free> frees = new ArrayList<>();

        for(Assignment assignment : assignments) {

            if(assignment instanceof Free && assignment.semesterMatches(bigDuty.getSemester())) frees.add((Free) assignment);
        }

        return frees;
    }

    public ArrayList<Free> getUnlockedFrees() {

        ArrayList<Free> frees = new ArrayList<>();

        for(Assignment assignment : assignments) {

            if(assignment instanceof Free && !((Free) assignment).isLocked() && assignment.semesterMatches(bigDuty.getSemester())) frees.add((Free) assignment);
        }

        return frees;

    }

    public ArrayList<Assignment> getAllFrees(String day) {

        ArrayList<Assignment> frees = new ArrayList<>();

        for(Assignment assignment : getDayAssignments(day)) {
            if(assignment instanceof Free) frees.add(assignment);
        }

        return frees;

    }

    public ArrayList<Assignment> getAllFreesDuringSchool(String day) {
        ArrayList<Assignment> frees = new ArrayList<>();

        for(Assignment a : getDayAssignments(day)) {
            if(a instanceof Free && a.getPeriod().getValue() != Period.AFTER_SCHOOL_NUM && a.getPeriod().getValue() != Period.P0_NUM) frees.add(a);
        }

        return frees;
    }

    public ArrayList<Free> getAllFreesDuringSchool() {
        ArrayList<Free> frees = new ArrayList<>();

        for(Assignment a : getAssignments()) {
            if(a instanceof Free && a.getPeriod().getValue() != Period.AFTER_SCHOOL_NUM && a.getPeriod().getValue() != Period.P0_NUM && a.semesterMatches(bigDuty.getSemester())) frees.add((Free) a);
        }

        return frees;
    }

    public ArrayList<Free> getUnLockedFreesDuringSchool() {

        ArrayList<Free> frees = new ArrayList<>();

        for(Assignment a : getAssignments()) {

            if(a instanceof Free && a.getPeriod().getValue() != Period.AFTER_SCHOOL_NUM && a.getPeriod().getValue() != Period.P0_NUM && !((Free) a).isLocked() && a.semesterMatches(bigDuty.getSemester())) frees.add((Free)a);

        }

        return frees;

    }

    public Assignment getPeriodBefore(Assignment assignment) {

        Assignment a = getAssignment(assignment.getDay(), new Period(assignment.getPeriod().increment(-1, assignment.getDay())));

        if(a == null) {
            a = new Free(new Period(-1), "FY", "DNE", assignment.getDay() ,assignment.getTeacher());
        }

        return a;

    }

    public Assignment getPeriodAfter(Assignment assignment) {

        return getAssignment(assignment.getDay(), new Period(assignment.getPeriod().increment(1, assignment.getDay())));


    }

    public Assignment getTwoPeriodsAfter(Assignment assignment) {

        return getAssignment(assignment.getDay(), new Period(assignment.getPeriod().increment(2, assignment.getDay())));


    }



    public boolean inBetweenFrees(Assignment assignment) {

        return (getPeriodBefore(assignment) instanceof Free || getPeriodBefore(assignment) instanceof Lunch && getPeriodBefore(assignment).getPeriod().getValue() != 0)

                && (getPeriodAfter(assignment) instanceof Free || getPeriodAfter(assignment) instanceof Lunch && getPeriodAfter(assignment).getPeriod().getValue() != 9);

    }

    public boolean beforeTwoFrees(Assignment assignment) {

        return (!(getPeriodBefore(assignment) instanceof Free) || !(getPeriodBefore(assignment) instanceof Lunch))

                && (getPeriodAfter(assignment) instanceof Free || getPeriodAfter(assignment) instanceof Lunch && getPeriodAfter(assignment).getPeriod().getValue() != 9);

    }

    public int freesInHalf(Assignment assignment) {

        int count = 0;

        boolean inSecondHalf = assignment.getPeriod().greaterThan("L");

        for(int i = (inSecondHalf ? 5 : 1); i <= (inSecondHalf ? 8 : 4); i++) {

            if(getAssignment(assignment.getDay(), new Period(i)) instanceof Free) count++;

        }

        return count;

    }

    public int freesInOtherHalf(Assignment assignment) {

        int count = 0;

        boolean inSecondHalf = assignment.getPeriod().greaterThan("L");

        for(int i = (inSecondHalf ? 1 : 5); i <= (inSecondHalf ? 4 : 8); i++) {

            if(getAssignment(assignment.getDay(), new Period(i)) instanceof Free) count++;

        }

        return count;

    }

    public int totalDuties() {

        int sum = 0;

        for(Assignment a : assignments) {
            if(a instanceof Duty && a.semesterMatches(bigDuty.getSemester())) sum+=1;
        }

        return sum;
    }

    public int totalCourses() {

        int sum = 0;
        for(Assignment a : getDayAssignments("M")) {

            if(a instanceof Course) sum+=1;

        }
        return sum;
    }

    private int rankAssignment(Assignment assignment) {

        int score = this.socialCredit;

        if(assignment instanceof Course) return -2900000;

        if(assignment instanceof Free && ((Free)(assignment)).isLocked()) return -290000000;

        if(totalDuties() >= 3) score = -29000000;

//        if(totalCourses() >= 6) return -2900000;

        if(hasDuty(assignment.getDay())) score += -100;

        if(getAllFreesDuringSchool(assignment.getDay()).size() <= 1) score += -100;

        score += freesInHalf(assignment)*10 - 10;

        score += -freesInOtherHalf(assignment)*10;

        int b = getPeriodBefore(assignment).getPeriod().getValue();

        if(b == 0 || b == -1) score += -10;

        int a = getPeriodAfter(assignment).getPeriod().getValue();

        if(a == -1 || a == 9) score += -20;

        if(touchesFree(assignment)) score += 20;

        if(inBetweenFrees(assignment)) score  += -25;

        if(beforeTwoFrees(assignment)) score += 30;

        if(afterTwoFrees(assignment)) score += 30;

        if(touchesLunch(assignment)) score -= 20;

        if(touchesLunch(assignment) && lunchIsBorderedByFrees(assignment) && afterLunch(assignment)) score += 10;

//        if(assignment.getDay() == "M") score += 20;

        score += -totalDuties()*50;

        return score;

    }

    public int getTotalNonCoverages() {

        int total = 0;

        for(Duty d : getDuties()) {

            if(d.getType() != DutyType.COVERAGE && d.getType() != DutyType.UNASSIGNED) total++;

        }

        return total;

    }

    public ScoredPeriod getBestFree() {

        return getBestFree(0);

    }

    public ArrayList<Course> getCourses() {
        ArrayList<Course> courses = new ArrayList<>();
        for(Assignment a : assignments) {
            if(a instanceof Course) courses.add((Course)a);
        }
        return courses;
    }

    public ScoredPeriod getBestFree(int index) {

        ArrayList<ScoredPeriod> frees = new ArrayList<>();

        for(Assignment a : getUnLockedFreesDuringSchool()) {

            ScoredPeriod sp = new ScoredPeriod(scoreAssignment(a), a);

            frees.add(sp);

        }

        if(index >= frees.size()-1) return null;

        Collections.sort(frees);

        return frees.get(index);

    }

    public ArrayList<Duty> getDutiesOfType(DutyType type) {

        ArrayList<Duty> duties = new ArrayList<>();

        for(Assignment a : assignments) {

            if(a instanceof Duty && (((Duty)a).getType() == type) && a.semesterMatches(bigDuty.getSemester())) duties.add((Duty) a);

        }

        return duties;

    }

    public ArrayList<Duty> getPascacks() {
        return getDutiesOfType(DutyType.PASCACK);
    }

    public ArrayList<Duty> getHalls() {
        return getDutiesOfType(DutyType.HALL);
    }

    public ArrayList<Duty> getCoverages() {
        return getDutiesOfType(DutyType.COVERAGE);
    }

    public ArrayList<Duty> getFroshPascacks() {
        return getDutiesOfType(DutyType.FROSH_PASCACK);
    }

    public boolean dayHasHigherScoringFree(Assignment assignment) {

        ArrayList<Assignment> assignments = getDayAssignments(assignment.getDay());

        int score = rankAssignment(assignment);

        for(Assignment a : assignments) {

            if(a instanceof Free) {

                if(rankAssignment(a) > score) return true;

            }
        }
        return false;

    }

    public ArrayList<Duty> getDuties() {

        ArrayList<Duty> duties = new ArrayList<>();

        for(Assignment assignment : assignments) {
            if(assignment instanceof Duty && assignment.semesterMatches(bigDuty.getSemester())) {
                duties.add((Duty) assignment);
            }
        }
        return duties;

    }

    public boolean lunchIsBorderedByFrees(Assignment assignment) {

        Assignment lunch = getDayLunch(assignment.getDay());

        return getPeriodBefore(lunch) instanceof Free && getPeriodAfter(lunch) instanceof Free;
    }

    public boolean touchesLunch(Assignment assignment)
    {
        return getPeriodBefore(assignment).getPeriod().isLunch() || getPeriodAfter(assignment).getPeriod().isLunch();
    }

    public void lockFrees() {

        for(Assignment a : assignments) {

            if(a instanceof Free && a.semesterMatches(bigDuty.getSemester())) {

                ((Free) a).setLocked(true);

            }
        }
    }

    public void unlockFrees() {

        for(Assignment a : assignments) {

            if(a instanceof Free && a.semesterMatches(bigDuty.getSemester())) {

                ((Free) a).setLocked(false);
            }
        }
    }

    public boolean isLocked() {

        for(Free f : getAllFrees()) {

            if(!f.isLocked()) return false;

        }

        return true;

    }


    public boolean touchesFree(Assignment assignment) {

        if(getPeriodBefore(assignment) instanceof Free && getPeriodBefore(assignment).getPeriod().getValue() != 0) return true;

        return getPeriodAfter(assignment) instanceof Free && getPeriodAfter(assignment).getPeriod().getValue() != 9;

    }

    private boolean hasDuty(String day)
    {
        for(Assignment a : assignments)
            if(a.getDay().equals(day) && a instanceof Duty && a.semesterMatches(bigDuty.getSemester()))
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

        if(dayHasHigherScoringFree(assignment)) score += -10;

        double multi = 1;


        return (int) (score * multi);

    }

    public double getScoreStandardDeviation() {

        int n = getAllFreesDuringSchool().size();

        double sum = 0;

        for(int i = 0; i < n; i++) {

            Assignment a = getAllFreesDuringSchool().get(i);

            sum += Math.pow(rankAssignment(a) - getMeanScore(), 2);

        }

        return Math.sqrt(sum/n);

    }

    public int getMeanScore() {

        int sum = 0;

        for(Assignment a : getAllFreesDuringSchool()) {

            sum += rankAssignment(a);

        }

        return sum/ getAllFreesDuringSchool().size();

    }

    /**
     * Ranks all the free periods that the teacher has
     * @return a sorted ArrayList of ScoredAssignments that the teacher has from best to worst
     */
    public ArrayList<ScoredPeriod> rankFrees() {

        ArrayList<ScoredPeriod> asses = new ArrayList<>();

        for(Assignment a : getAllFreesDuringSchool()) {

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

    public String getId() {
        return "" + id;
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

    public Duty getBestDutyToReplace(Assignment assignment) {

        int lowestScore = Integer.MAX_VALUE;
        Duty lowestDuty = null;

        for(Duty duty : getDuties()) {

            if(duty.sameTimeAs(assignment)) continue;

            if(duty.getType().equals(DutyType.COVERAGE)) {
                return duty;
            }

            int s = rankAssignment(duty);

            if(s < lowestScore) {
                lowestScore = s;
                lowestDuty = duty;
            }

        }

        return lowestDuty;

    }

    public void printAssignments(String day) {

        for(Assignment assignment : getDayAssignments(day)) {
            System.out.println(assignment.toString());
        }
    }

    public void removeDuty(Duty duty) {

        Assignment a = null;

        for(Assignment assignment : assignments) {
            if(assignment.getDay().equals(duty.getDay()) && assignment.getPeriod().equals(duty.getPeriod())) {

                a = assignment;
                break;
            }
        }

        assignments.remove(a);
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
     */
    public void replaceAssignment(Assignment newAssignment) {

        Assignment a = getAssignment(newAssignment.getDay(), newAssignment.getPeriod());

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

    public void setBigDuty(BigDuty bigDuty) {

        this.bigDuty = bigDuty;

    }

    public BigDuty getBigDuty() {
        return bigDuty;
    }

    public void setMaxDuties(int val) {
        this.maxDuties = val;
    }
}