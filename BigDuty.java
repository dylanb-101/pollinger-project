import java.io.File;
import java.util.*;

//Max Nadel
//Program description
//Sep 24, 2024

public class BigDuty
{

	public static Map<String, String> schedule = new HashMap<>();

	private ArrayList<DutySlot> dutySlots = new ArrayList<>();

	private ArrayList<Assignment> assignments;

	private ArrayList<Teacher> teachers;

	private static final int PASCACKS_PER_PERIOD = 2;
	private static final int HALLS_PER_PERIOD = 1;
	private static final Integer[] FROSH_PASCACK_PERIODS = {4, 6, 7};

	public static String[] days = {"M", "T", "W", "R", "F"};

	public static String BACKUP_LOCATION = "Users/" + System.getProperty("user.name") + "/Documents/bigDuty";

	private String semester;

//	private GUIMain gui;

	private SemesterTab pane;

	private boolean stopRefresh = false;

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

		for(Teacher teacher : teachers) {
			teacher.setBigDuty(this);
		}

		System.out.println("Filling In assignments...");
		this.assignments = new ArrayList<>();

		readInAssignments();
		System.out.println("Big Duty has been made!");

		this.pane = GUIMain.makeTeachersTabs(this);


//		assignDuties();
	}

	public BigDuty(String semester) {
		this.semester = semester;
	}

	public BigDuty(ArrayList<Teacher> teachers, GUIMain gui, String semester) {

		System.out.println("Making Big Duty...");

		schedule.put("M_SCHEDULE", "01234L56789");
		schedule.put("T_SCHEDULE", "0123L5679");
		schedule.put("W_SCHEDULE", "0412L8569");
		schedule.put("R_SCHEDULE", "0341L7859");
		schedule.put("F_SCHEDULE", "0234L6789");

		this.semester = semester;

		ArrayList<Teacher> deepCopyTeachers = new ArrayList<>();

		for(Teacher teacher : teachers) {

			ArrayList<Assignment> deepCopyAssignments = new ArrayList<>();
			Teacher deepCopyTeacher = new Teacher(teacher);

			deepCopyTeacher.setSocialCredit(teacher.getAdjustedSocialCredit());


			for(Assignment assignment : teacher.getAssignments()) {

				Assignment a = null;

				if(assignment instanceof Course) {

					Course c = (Course) assignment;

					a = new Course(c.getName(), c.getCourseCode(), c.getSection(), c.getPeriod().getPeriod(), c.getDay(), semester, c.getRoom(), c.getDepartment(), deepCopyTeacher, false);

				}

				if(assignment instanceof Duty || assignment instanceof Free) {

					a = new Free(assignment.getPeriod(), semester, "Free", assignment.getDay(), deepCopyTeacher);
				}

				if(assignment instanceof Lunch) {

					Lunch l = (Lunch) assignment;

					a = new Lunch(l.getPeriod().getPeriod(), l.getSemester(), l.getName(), l.getDay(), deepCopyTeacher);

				}

				deepCopyAssignments.add(a);

			}

//			i know this is really weird code bc it does the same thing in the teacher constructor but this fixed a null pointer bug so dont touch it pls
			deepCopyTeacher.setAssignments(deepCopyAssignments);
			deepCopyTeachers.add(deepCopyTeacher);

		}

		this.teachers = deepCopyTeachers;

		for(Teacher teacher : this.teachers) {
			teacher.setBigDuty(this);
		}

		this.assignments = new ArrayList<>();
		readInAssignments();

		this.pane = GUIMain.makeTeachersTabs(this);

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

		for(Teacher teacher : teachers) {
			teacher.setBigDuty(this);
		}

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
		for(Teacher teacher : teachers) {
			teacher.setBigDuty(this);
		}
		System.out.println("Filling In assignments...");
		this.assignments = new ArrayList<>();

		fillInTeacherAssignments();
		System.out.println("Big Duty has been made!");

		assignDuties();

		this.pane = gui.makeTeachersTabs(this);

	}

	public BigDuty(ArrayList<Teacher> teachers, String semester) {

		System.out.println("Making Big Duty...");

		schedule.put("M_SCHEDULE", "01234L56789");
		schedule.put("T_SCHEDULE", "0123L5679");
		schedule.put("W_SCHEDULE", "0412L8569");
		schedule.put("R_SCHEDULE", "0341L7859");
		schedule.put("F_SCHEDULE", "0234L6789");

		this.semester = semester;

		this.teachers = teachers;

		this.assignments = new ArrayList<>();

		for(Teacher teacher : teachers) {
			teacher.setBigDuty(this);
			assignments.addAll(teacher.getAssignments());
		}


		System.out.println("Filling In assignments...");
//		fillInTeacherAssignments();

		System.out.println("Big Duty has been made!");


	}

	public void setPane(SemesterTab pane) {
		this.pane = pane;
	}

	public void refreshPanels() {

		if(!stopRefresh) {
			pane.refreshPanels();
		}

	}

	public void assignDuties() {

		stopRefresh = true;
//
		assignDutiesOfType(DutyType.FROSH_PASCACK);
		System.out.println("Done with frosh pascacks!");

		assignDutiesOfType(DutyType.PASCACK);
		System.out.println("Done with pascacks!");

		assignDutiesOfType(DutyType.HALL);
		System.out.println("Done with halls!");

		assignDutiesOfType(DutyType.COVERAGE);
		System.out.println("Done with coverages!");

		stopRefresh = false;



	}

	public ArrayList<Teacher> getTeachersWithFreeInPeriod(Period period, String day) {

		ArrayList<Teacher> teachers = new ArrayList<>();

		for(Assignment a : assignments) {

			if(a instanceof Free && a.getPeriod().equals(period) && a.getDay().equals(day)) {
				teachers.add(a.getTeacher());
			}

		}

		return teachers;

	}

	public DutySlot getDutySlot(Period period, String day, DutyType type) {
		for(DutySlot slot : this.dutySlots) {

			if(slot.equals(new DutySlot(type, period, day, 0))) return slot;

		}

		return null;
	}

	public ArrayList<Teacher> getUnlockedTeachers() {

		ArrayList<Teacher> unlockedTeachers = new ArrayList<>();

		for(Teacher teacher : teachers) {

			if(!teacher.isLocked()) {
				unlockedTeachers.add(teacher);
			}

		}

		return unlockedTeachers;

	}

	public ArrayList<DutySlot> getDutySlots() {
		return dutySlots;
	}

	public void addDutySlot(Period period, String day, DutyType type, int amount) {
		addDutySlot(new DutySlot(type, period, day, amount));
	}

	public int getNumberOfDutySlots() {

		int num = 0;

		for(DutySlot slot : dutySlots) {
			num += slot.getAmount();
		}

		return num;

	}

	public void addDutySlot(DutySlot dutySlot) {

		boolean add = true;

		for(DutySlot slot : dutySlots) {

			if(slot.equals(dutySlot)) {

				slot.setAmount(dutySlot.getAmount());
				add = false;
			}

		}

		if(add) {
			dutySlots.add(dutySlot);
		}

	}


	public void assignFroshPascacks() {

		ArrayList<Duty> duties = new ArrayList<>();

//		for(int i = 0; i < schedule.size(); i++) {
//
//			String day = schedule.get(days[i]+"_SCHEDULE");
//
//			for(int j = 0; j < FROSH_PASCACK_PERIODS.length; j++) {
//
//				int periodIndex = day.indexOf(FROSH_PASCACK_PERIODS[j] + "");
//
//				if(periodIndex != 1 && periodIndex > 0 && periodIndex != day.length()-2) {
//
//					Duty d = new Duty(new Period(FROSH_PASCACK_PERIODS[j], days[i]), semester, "Freshman Pascack", "NONE", days[i], DutyType.FROSH_PASCACK);
//
//					duties.add(d);
//
//				}
//			}
//		}

		for(DutySlot slot : dutySlots) {
			if(slot.getType() == DutyType.FROSH_PASCACK) duties.add(slot.makeEmptyDuty(semester));
		}


		fillInDuties(duties);
	}

	public void assignDutiesOfType(DutyType type) {

		ArrayList<Duty> duties = new ArrayList<>();

		for(DutySlot slot : dutySlots) {

			if(slot.getType() == type) {

				for(int i = 0; i < slot.getAmount(); i++) {
					duties.add(slot.makeEmptyDuty(semester));
				}

			}

		}

		duties.sort((o1, o2) -> {

			double o1Ratio = (double) getFreeTeachers(o1.getDay(), o1.getPeriod()).size() / getDutiesForPeriod(o1);
			double o2Ratio = (double) getFreeTeachers(o2.getDay(), o2.getPeriod()).size() / getDutiesForPeriod(o2);
			System.out.println(o1Ratio + "," + o2Ratio+ ", " + getDutiesForPeriod(o1));

			return (int) ((o1Ratio - o2Ratio) * 1000);

        });

		fillInDuties(duties);

	}

	public int getDutiesForPeriod(Duty duty) {

		int duties = 0;

		for(DutySlot slot : dutySlots) {

			if(slot.getPeriod().equals(duty.getPeriod()) && slot.getDay().equals(duty.getDay())) duties+= slot.getAmount();

		}

		return duties;

	}

	public void clearDuties() {

		ArrayList<Assignment> dutiesToReset = new ArrayList<>();

		for(Assignment a : assignments) {

			if(a instanceof Duty && a.semesterMatches(semester)) {
				dutiesToReset.add(a);
			}
		}

		stopRefresh = true;

		for(Assignment a : dutiesToReset) {

			replaceAssignment(new Free(a));

		}

		stopRefresh = false;

		pane.refreshPanels();

	}

	private void fillInDuties(ArrayList<Duty> duties) {

		for(Duty d : duties) {


			fillInDuty(d);
			System.out.println(d);
		}

	}

	public void fillInDuty(Duty duty) {

		Teacher t = getBestTeacher(duty);

		int i = 0;

		while(t != null && ((t.getTotalNonCoverages() >= 1 && !allTeachersHaveAtleastOneDuty()) || t.getTotalNonCoverages() >= 2 && allTeachersHaveAtleastOneDuty())
		&& t.getDuties().size() >= 3) {

			System.out.println("hi im in the first loop" + t.getName());

			t = getBestTeacher(duty, i++);

		}

		if(t == null) {

			i = 0;


            do {

				System.out.println("hi im in the second loop");

                t = getBestTeacher(duty, i++);

				if(t == null) {

					t = getBestTeacher(duty);

					Duty oldDuty = t.getBestDutyToReplace(duty);

					fillInDuty(oldDuty);

					replaceAssignment(new Free(oldDuty.getPeriod(), semester, "Free", oldDuty.getDay(), t));
					break;
				}

            } while (t.getTotalNonCoverages() >= 2);



		}

		if(t.getDuties().size() == t.getMaxDuties()) {

			Duty oldDuty = t.getBestDutyToReplace(duty);

			fillInDuty(oldDuty);

			replaceAssignment(new Free(oldDuty.getPeriod(), semester, "Free", oldDuty.getDay(), t));

		}

		duty.setTeacher(t);
		replaceAssignment(duty);


	}

	public String dutySlotsToSaveString() {

		String str = "";

		for(DutySlot slot : dutySlots) {
			str += slot.toSaveString() + ";";
		}

		return str;

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

		fillInDuties(duties);

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

		fillInDuties(duties);

	}

	public void assignCoverages() {

		ArrayList<Duty> duties = new ArrayList<>();

		for(int d = 0; d < days.length; d++) {

			String day = days[d];

			for(int p = 1; p <= 8; p++) {

				if(!day.equals("M") && (p == 4 || p == 8)) continue;

				for(int i = 0; i < 2; i++) duties.add(new Duty(new Period(p), semester, "Coverage", "None", day, DutyType.COVERAGE));
			}
		}

		fillInDuties(duties);

	}

	public void fillInExtraCoverages() {


		for(Teacher t : getTeachersWithOpenDuties()) {

			while(t.getDuties().size() < 3) {

//				String name = (t.getDuties().size() < 2) ? "Paid Coverage" : "Coverage";

				String name = "Coverage";

				Duty d = new Duty(t.getBestFree().getAssignment(), name, "None", DutyType.COVERAGE);

				int i = 0;

				while(numberOfCoveragesInPeriod(d) > 2) {

					if(t.getBestFree(i) == null) {

						d = new Duty(t.getBestFree().getAssignment(), name, "None", DutyType.COVERAGE);
						break;

					}

					d = new Duty(t.getBestFree(i++).getAssignment(), name, "None", DutyType.COVERAGE);

				}

				replaceAssignment(d);

			}

		}

		refreshPanels();

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
				if(teachers.get(i).getUnlockedFrees().get(k).getDay().equals(day) && teachers.get(i).getUnlockedFrees().get(k).getPeriod().equals(period) && teachers.get(i).getUnlockedFrees().get(k).semesterMatches(semester)) {
					freeTeachers.add(teachers.get(i));
				}

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

			teacher.fillInLunchesAndFrees();


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

		refreshPanels();


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

	public SemesterTab getPane() {
		return pane;
	}

}