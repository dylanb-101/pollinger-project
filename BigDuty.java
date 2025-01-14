import javax.swing.*;
import java.io.File;
import java.util.*;

//Max Nadel
//Program description
//Sep 24, 2024

public class BigDuty
{

	public static Map<String, String> schedule = new HashMap<>();

	private final ArrayList<Assignment> assignments;

	private final ArrayList<Teacher> teachers;

	private static final int PASCACKS_PER_PERIOD = 2;
	private static final int HALLS_PER_PERIOD = 1;
	private static final Integer[] FROSH_PASCACK_PERIODS = {4, 6, 7};

	public static String[] days = {"M", "T", "W", "R", "F"};

	public static String BACKUP_LOCATION = "Users/" + System.getProperty("user.name") + "/Documents/bigDuty";

	private String semester;

//	private GUIMain gui;

	private SemesterTab pane;

	public BigDuty(ArrayList<Teacher> teachers, GUIMain gui) {
		System.out.println("Making Big Duty...");

		schedule.put("M_SCHEDULE", "01234L56789");
		schedule.put("T_SCHEDULE", "0123L5679");
		schedule.put("W_SCHEDULE", "0412L8569");
		schedule.put("R_SCHEDULE", "0341L7859");
		schedule.put("F_SCHEDULE", "0234L6789");

//		this.gui = gui;
		this.semester = "S1";

		System.out.println("Reading in teachers...");
		this.teachers = teachers;
		System.out.println("Filling In assignments...");
		this.assignments = new ArrayList<>();

		readInAssignments();
		System.out.println("Big Duty has been made!");

		this.pane = gui.makeTeachersTabs(this);


//		assignDuties();
	}

	public BigDuty(ArrayList<Teacher> teachers, GUIMain gui, String semester) {

		this(teachers, gui);
		this.semester = semester;


	}



	public BigDuty(File file, GUIMain gui) {

		schedule.put("M_SCHEDULE", "01234L56789");
		schedule.put("T_SCHEDULE", "0123L5679");
		schedule.put("W_SCHEDULE", "0412L8569");
		schedule.put("R_SCHEDULE", "0341L7859");
		schedule.put("F_SCHEDULE", "0234L6789");

//		this.gui = gui;
		this.semester = "S1";


		System.out.println("Reading in teachers...");
		this.teachers = FileUtility.createTeachers(file);
		System.out.println("Filling In assignments...");
		this.assignments = new ArrayList<>();

		fillInTeacherAssignments();
		System.out.println("Big Duty has been made!");

		this.pane = gui.makeTeachersTabs(this);


	}

	public BigDuty(String dataFile, GUIMain gui)
	{

		System.out.println("Making Big Duty...");

		schedule.put("M_SCHEDULE", "01234L56789");
		schedule.put("T_SCHEDULE", "0123L5679");
		schedule.put("W_SCHEDULE", "0412L8569");
		schedule.put("R_SCHEDULE", "0341L7859");
		schedule.put("F_SCHEDULE", "0234L6789");

//		this.gui = gui;
		this.semester = "S1";

		System.out.println("Reading in teachers...");
		this.teachers = FileUtility.createTeachers(dataFile);
		System.out.println("Filling In assignments...");
		this.assignments = new ArrayList<>();

		fillInTeacherAssignments();
		System.out.println("Big Duty has been made!");

		assignDuties();

		this.pane = gui.makeTeachersTabs(this);

	}

	public void setPane(SemesterTab pane) {
		this.pane = pane;
	}

	public void refreshPanels() {
		pane.refreshPanels();
	}

	public void assignDuties() {

		assignFroshPascacks();

		assignHalls();

		assignPascacks();

		assignCoverages();

	}


	public void assignFroshPascacks() {

		ArrayList<Duty> duties = new ArrayList<>();

		for(int i = 0; i < schedule.size(); i++) {

			String day = schedule.get(days[i]+"_SCHEDULE");

			for(int j = 0; j < FROSH_PASCACK_PERIODS.length; j++) {

				int periodIndex = day.indexOf(FROSH_PASCACK_PERIODS[j] + "");

				if(periodIndex != 1 && periodIndex > 0 && periodIndex != day.length()-2) {

					Duty d = new Duty(new Period(FROSH_PASCACK_PERIODS[j], days[i]), semester, "Freshman Pascack", "NONE", days[i], DutyType.FROSH_PASCACK);

					duties.add(d);

				}
			}
		}


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

		pane.refreshPanels();

	}

	private void fillInNonCoverageDuties(ArrayList<Duty> duties) {

		for(Duty d : duties) {

			Teacher t = getBestTeacher(d);

			int i = 1;

			while(t != null && ((t.getTotalNonCoverages() >= 1 && !allTeachersHaveAtleastOneDuty()) || t.getTotalNonCoverages() >= 2 && allTeachersHaveAtleastOneDuty())) {

				t = getBestTeacher(d, i++);

			}

			if(t == null) {

				i = 0;

				t = getBestTeacher(d, i++);

				while(t.getTotalNonCoverages() >= 2) {

					t = getBestTeacher(d, i++);

				}

			}

			d.setTeacher(t);
			replaceAssignment(d);
		}
	}

	public boolean allTeachersHaveAtleastOneDuty() {

		for(Teacher t : teachers) {

			if(t.isLocked()) continue;

			if(t.getDuties().size() < 1) return false;

		}

		return true;

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

				if(!day.equals("M") && (p == 4 || p == 8)) continue;

				for(int i = 0; i < HALLS_PER_PERIOD; i++) duties.add(new Duty(new Period(p), semester, "Hall", "Hall", day, DutyType.HALL));
			}
		}

		fillInNonCoverageDuties(duties);

	}

	public void assignCoverages() {


		for(Teacher t : getTeachersWithOpenDuties()) {

			while(t.getDuties().size() < 3) {

				String name = (t.getDuties().size() < 2) ? "Paid Coverage" : "Coverage";

				Duty d = new Duty(t.getBestFree().getAssignment(), name, "None", DutyType.COVERAGE);

				int i = 0;

				while(numberOfCoveragesInPeriod(d) > 4) {

					if(t.getBestFree(i) == null) {

						d = new Duty(t.getBestFree().getAssignment(), name, "None", DutyType.COVERAGE);
						break;

					}

					d = new Duty(t.getBestFree(i++).getAssignment(), name, "None", DutyType.COVERAGE);

				}

				replaceAssignment(d);

			}

		}

	}

	public ArrayList<Assignment> getAssignmentsInPeriod(Assignment assignment) {

		ArrayList<Assignment> periods = new ArrayList<>();

		for(Assignment a : assignments) {

			if(a.getDay().equals(assignment.getDay()) && a.getPeriod().equals(assignment.getPeriod())) periods.add(a);

		}

		return periods;

	}

	public int numberOfCoveragesInPeriod(Assignment assignment) {

		int coverages = 0;

		for(Assignment a : assignments) {

			if(a instanceof Duty && ((Duty) a).getType() == DutyType.COVERAGE) coverages += 1;

		}
		return coverages;

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
			for(int k = 0; k < teachers.get(i).getUnlockedFrees().size(); k++)
				if(teachers.get(i).getUnlockedFrees().get(k).getDay().equals(day) && teachers.get(i).getUnlockedFrees().get(k).getPeriod().equals(period))
					freeTeachers.add(teachers.get(i));

		return freeTeachers;
	}



	public Teacher getBestTeacher(String day, Period period, int index) {

		ArrayList<ScoredPeriod> p = getListOfRankedAssignments(day, period);

		if(index >= p.size()) return null;

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

	public void readInAssignments() {

		for(Teacher teacher : teachers) {

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

		clearDuties();
		assignDuties();
		refreshPanels();

	}

	public boolean isFreshmanPascack(String day, Period period)
	{
		//checking its period
		int[] freshmanPascackPeriods = {4, 6, 7};
		for(int i = 0; i < freshmanPascackPeriods.length; i++)
		{
			if(period.getValue() == freshmanPascackPeriods[i])
			{
				String[] FreshmanPascackDays = {"W", "W", "T"};
				return !day.equals(FreshmanPascackDays[i]);
			}
		}
		return false;
	}

//	public GUIMain getGui() {
//		return gui;
//	}

//	public void setGui(GUIMain gui) {
//		this.gui = gui;
//	}

	public String getSemester() {
		return semester;
	}
}