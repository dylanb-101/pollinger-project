import java.util.*;

//Max Nadel
//Program description
//Sep 24, 2024

public class BigDuty
{

	public static Map<String, String> schedule = new HashMap<>();

	private ArrayList<Assignment> assignments;

	private ArrayList<Teacher> teachers;

	private static final int PASCACKS_PER_PERIOD = 2;
	private static final int HALLS_PER_PERIOD = 1;
	private static Integer[] FROSH_PASCACK_PERIODS = {4, 6, 7};

	public static String[] days = {"M", "T", "W", "R", "F"};

	private String semester;

	public BigDuty(String dataFile)
	{

		System.out.println("Making Big Duty...");

		schedule.put("M_SCHEDULE", "01234L56789");
		schedule.put("T_SCHEDULE", "0123L5679");
		schedule.put("W_SCHEDULE", "0412L8569");
		schedule.put("R_SCHEDULE", "0341L7859");
		schedule.put("F_SCHEDULE", "0234L6789");

		this.semester = "S2";

		System.out.println("Reading in teachers...");
		this.teachers = FileUtility.createTeachers(dataFile);
		System.out.println("Filling In assignments...");
		this.assignments = new ArrayList<>();

		fillInTeacherAssignments();
		System.out.println("Big Duty has been made!");

		assignDuties();
	}

//	public void assignDuties() {
//
//		String[] days = {"M", "T", "W", "R", "F"};
//
//		ArrayList<Duty> duties = new ArrayList<>();
//
//		for(int d = 0; d < days.length; d++) {
//
//			for(int p = 1; p <= 8; p++) {
//
//				if(!days[d].equals("M") && (p == 4 || p == 8)) continue;
//
//
//
//
//				for(int i = 0; i < PASCACKS_PER_PERIOD; i++) {
//
//					duties.add(new Duty(new Period(p), "FY", "Pascack", "CAF", days[d], DutyType.PASCACK));
//
//				}
//
//				for(int i = 0; i < HALLS_PER_PERIOD; i++) {
//
//					duties.add(new Duty(new Period(p), "FY", "Hall", "Hall", days[d], DutyType.HALL));
//
//				}
//
//			}
//		}
//
//		for(Duty duty : duties) {
//
//			Teacher teacher = getBestTeacher(duty);
//
//			duty.setTeacher(teacher);
//
//			replaceAssignment(duty);
//
//		}
//	}

	public void assignDuties() {

		assignFroshPascacks();

		assignPascacks();

		assignHalls();

		assignCoverages();
	}

	public void assignFroshPascacks() {

		ArrayList<Duty> duties = new ArrayList<>();

		for(int i = 0; i < schedule.size(); i++) {

			String day = schedule.get(days[i]+"_SCHEDULE");

			for(int j = 0; j < FROSH_PASCACK_PERIODS.length; j++) {

				int periodIndex = day.indexOf(FROSH_PASCACK_PERIODS[j]);

				if(periodIndex != 1 && periodIndex >= 0 && periodIndex != day.length()-2) {

					Duty d = new Duty(new Period(FROSH_PASCACK_PERIODS[j], days[i]), semester, "Freshman Pascack", "NONE", days[i], DutyType.FROSH_PASCACK);

					duties.add(d);

				}
			}
		}

		System.out.println(duties);

		fillInNonCoverageDuties(duties);
	}

	public void clearDuties() {

		ArrayList<Assignment> dutiesToReset = new ArrayList<>();

		for(Assignment a : assignments) {

			if(a instanceof Duty) {
				dutiesToReset.add(a);
			}
		}

		for(Assignment a : dutiesToReset) {

			replaceAssignment(new Free(a));

		}

	}

	private void fillInNonCoverageDuties(ArrayList<Duty> duties) {

		for(Duty d : duties) {

			Teacher t = getBestTeacher(d);

			int i = 1;

			while(t.getTotalNonCoverages() >= 2) {

				t = getBestTeacher(d, i++);

			}
			d.setTeacher(t);
			replaceAssignment(d);
		}
	}

	public Teacher getBestTeacher(Assignment a, int index) {

		return getBestTeacher(a.getDay(), a.getPeriod(), index);

	}

	public void assignPascacks() {

		ArrayList<Duty> duties = new ArrayList<>();

		for(int d = 0; d < days.length; d++) {

			String day = days[d];

			for(int p = 1; p <= 8; p++) {

				if(!day.equals("M") && (p == 4 || p == 8)) continue;

				if(p == 1 || p == 8 || (p == 7 && !day.equals("M"))) {

					duties.add(new Duty(new Period(p), semester, "Pascack", "Caf", day, DutyType.PASCACK));

				} else {

					for(int i = 0; i < PASCACKS_PER_PERIOD; i++) duties.add(new Duty(new Period(p), semester, "Pascack", "Caf", day, DutyType.PASCACK));

				}
			}
		}

		fillInNonCoverageDuties(duties);

	}

	public void assignHalls() {

		ArrayList<Duty> duties = new ArrayList<>();

		for(int d = 0; d < days.length; d++) {

			String day = days[d];

			for(int p = 1; p <= 8; p++) {

				if(!day.equals("M") && p == 4 || p == 8) continue;

				for(int i = 0; i < HALLS_PER_PERIOD; i++) duties.add(new Duty(new Period(p), semester, "Hall", "Hall", day, DutyType.HALL));
			}
		}

		fillInNonCoverageDuties(duties);

	}

	public void assignCoverages() {

		for(Teacher t : getTeachersWithOpenDuties()) {

			Free free = new Free(t.getBestFree().getAssignment());

			replaceAssignment(free);

		}

	}

	public ArrayList<Teacher> getTeachersWithOpenDuties() {

		ArrayList<Teacher> openDuties = new ArrayList<>();

		for(Teacher t : teachers) {

			if(t.getDuties().size() < 3) openDuties.add(t);

		}

		openDuties.sort((o1, o2) -> o1.totalDuties()-o2.totalDuties() == 0 ? o2.getSocialCredit()-o1.getSocialCredit() : o1.totalCourses()-o2.totalDuties());

		return openDuties;

	}

	public Teacher getTeacherByLastName(String name) {

		for(Teacher teacher : teachers) {

			if(teacher.getLastName().equals(name)) return teacher;

		}

		return null;

	}

	// returns a list of teachers with an attached score going from largest score to smallest
	public ArrayList<ScoredPeriod> getListOfRankedAssignments(String day, Period period)
	{
		ArrayList<ScoredPeriod> scores = new ArrayList<ScoredPeriod>();
		ArrayList<Teacher> fTeachers = getFreeTeachers(day, period);
		for(int i = 0; i < fTeachers.size(); i++)
		{
			int score = fTeachers.get(i).scoreAssignment(day, period);
			scores.add(new ScoredPeriod(score, fTeachers.get(i).getAssignment(day, period)));
		}

		Collections.sort(scores);

		return scores;

//		   sort scores

	}

	public void rankTeacher(String name) {

		Teacher t = null;

		for(Teacher teacher : teachers) {

			if(teacher.getLastName().equals(name)) {
				t = teacher;
			}

		}

		t.printScoredSchedule();

	}

	public ArrayList<Teacher> getFreeTeachers(String day, Period period)
	{
		ArrayList<Teacher> freeTeachers = new ArrayList<Teacher>();

		for(int i = 0; i < teachers.size(); i++)
			for(int k = 0; k < teachers.get(i).getFrees().size(); k++)
				if(teachers.get(i).getFrees().get(k).getDay().equals(day) && teachers.get(i).getFrees().get(k).getPeriod().equals(period))
					freeTeachers.add(teachers.get(i));

		return freeTeachers;
	}



	public Teacher getBestTeacher(String day, Period period, int index) {

		ArrayList<ScoredPeriod> p = getListOfRankedAssignments(day, period);

		return p.get(index).getAssignment().getTeacher();

	}

	public Teacher getBestTeacher(Assignment assignment) {
		return getBestTeacher(assignment.getDay(), assignment.getPeriod(), 0);
	}

	public void fillInTeacherAssignments() {

		for(Teacher teacher : teachers) {
			teacher.fillInAssignments();

			assignments.addAll(teacher.getAssignments());
		}
	}

	public ArrayList<Teacher> getTeachers() {
		return teachers;
	}

	/**
	 * Gets the teacher in the Teachers ArrayList at index i
	 * @param i the index of the teacher
	 * @return the teacher at the specified index
	 */
	public Teacher getTeacher(int i) {
		return teachers.get(i);
	}

	public Teacher getTeacher(String teacher) {

		Teacher teach = null;

		for(Teacher t : teachers) {

			if(t.getLastName().equals(teacher)) teach = t;

		}

		return teach;

	}

	/**
	 * Gets the Assignment in the Assignments ArrayList at index i
	 * @param i the index of the Assignment
	 * @return the Assignment at the specified index
	 */
	public Assignment getAssignment(int i) {
		return assignments.get(i);
	}

	public ArrayList<Assignment> getAssignments() {
		return assignments;
	}

	public ArrayList<Teacher> getTeachersWithDutyInPeriod(Period period, String day) {

		ArrayList<Teacher> ts = new ArrayList<>();

		for(Assignment a : assignments) {


			if(a.getDay().equals(day) && a.getPeriod().equals(period) && a instanceof Duty) {
				ts.add(a.getTeacher());
			}

		}


		return ts;

	}

	public void replaceAssignment(Assignment assignment) {

		Teacher t = assignment.getTeacher();

		Assignment a = t.getAssignment(assignment.getDay(), assignment.getPeriod());

		assignments.remove(a);
		t.getAssignments().remove(a);

		assignments.add(assignment);
		t.addAssignment(assignment);


	}

	public void reassignDuties() {

	}

//	public void assignDutys()
//	{
//		//start by assigning the pascack periods
//		for(int x = 0; x < schedule.size(); x++){
//			String day = days[x];
//			for(int i = 0; i < schedule.get(day + "_SCHEDULE").length()-1; i++) {
//				Period period = new Period(i);
//				if(isFreshmanPascack(day, period))
//					getBestTeacher(day, period).replaceAssignment();
//			}
//
//
//		}
//	}

	public boolean isFreshmanPascack(String day, Period period)
	{
		//chekcing its period
		int[] freshmanPascackPeriods = {4, 6, 7};
		for(int i = 0; i < freshmanPascackPeriods.length; i++)
		{
			if(period.getValue() == freshmanPascackPeriods[i])
			{
				String[] FreshmanPascackDays = {"W", "W", "T"};
				if(day.equals(FreshmanPascackDays[i]))
					return false;
				return true;
			}
		}
		return false;
	}


}