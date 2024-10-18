import java.util.*;

//Max Nadel
//Program description
//Sep 24, 2024

public class BigDuty
{

	public static Map<String, String> schedule = new HashMap<>();

   private ArrayList<Assignment> assignments;
   
   private ArrayList<Teacher> teachers;

   public static String M_FORMAT = "01234L5678";
   public static String T_FORMAT = "0123L567";
   public static String W_FORMAT = "0412L856";
   public static String R_FORMAT = "0341L785";
   public static String F_FORMAT = "234L678";
   
   public BigDuty(String dataFile)
   {
      this.assignments = assignments;
      this.teachers = teachers;

//	  populateAssignments();


   }
   

	   System.out.println("Making Big Duty...");

	   schedule.put("M_SCHEDULE", "01234L56789");
	   schedule.put("T_SCHEDULE", "0123L5679");
	   schedule.put("W_SCHEDULE", "0412L8569");
	   schedule.put("R_SCHEDULE", "0341L7859");
	   schedule.put("F_SCHEDULE", "234L6789");

	   System.out.println("Reading in teachers...");
      this.teachers = FileUtility.createTeachers(dataFile);
	  System.out.println("Filling In assignments...");
	  this.assignments = new ArrayList<>();

	  fillInTeacherAssignments();
	  System.out.println("Big Duty has been made!");

   }
   
   // returns a list of teachers with an attached score going from largest score to smallest
   public ArrayList<TeacherWithScore> getListOfRankedTeachers(String day, int period)
   {	   
	   ArrayList<TeacherWithScore> tScores = new ArrayList<TeacherWithScore>();
	   ArrayList<Teacher> teachers = getFreeTeachers(day, period);
	   for(int i = 0; i < teachers.size(); i++)
	   {
		   int score = teachers.get(i).scoreAssignment(day, period);
		   tScores.add(new TeacherWithScore(teachers.get(i),score));
	   }
	   
	   Collections.sort(tScores);
	   
	   return tScores;

   }
   
   public ArrayList<Teacher> getFreeTeachers(String day, int period)
   {	 
	   ArrayList<Teacher> freeTeachers = new ArrayList<Teacher>();
	   
	   for(int i = 0; i < teachers.size(); i++)
		   for(int k = 0; k < teachers.get(i).getFrees().size(); k++)
			   if(teachers.get(i).getFrees().get(k).getDay() == day &&  
			      teachers.get(i).getFrees().get(k).getPeriod() == period)
				   freeTeachers.add(teachers.get(i));
	   
	   return freeTeachers;
   }
   
   public Teacher getBestTeacher(String day, int period)
   {
	   ArrayList<TeacherWithScore> teachers = getListOfRankedTeachers(day, period);
	   
	   return teachers.get(0).getTeacher();
   }

   public void populateAssignments(ArrayList<Teacher> teachers) {

	   for(Teacher teacher : teachers) {

		   ArrayList<Assignment> monSchedule = teacher.getDayAssignments("M");

//           fill out rest of week schedule
           for(Assignment assignment : monSchedule) {

               String p = assignment.getPeriod() == Assignment.LUNCH ? "L" : "" + assignment.getPeriod();



           }






	   }


   }

   public Assignment populateDay(String daySchedule, String period, String day, Assignment assignment) {

       if(daySchedule.indexOf(period) == -1) return null;

       int p = daySchedule.indexOf(period);

       if(p > 3) ;


       return assignment.dupeAssignment(day, daySchedule.indexOf(period));



   }

   public void fillInTeacherAssignments() {

	   for(Teacher teacher : teachers) {
		   teacher.fillInAssignments();

		   assignments.addAll(teacher.getAssignments());
	   }

//	   System.out.println(assignments.size());

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

   public static int translatePeriod(int period, String day) {

//	   System.out.println(day+"_SCHEDULE");

	   int i = schedule.get(day+"_SCHEDULE").indexOf("" + period);


		if(period == 9) return Assignment.AFTER_SCHOOL;

		if(i == -1) return -1;

		boolean inFirstHalf = i < schedule.get(day+"_SCHEDULE").indexOf("L");

		if(!day.equals("F") && !inFirstHalf) return i+1;

		if(day.equals("F") && !inFirstHalf) return i+2;

		if(day.equals("F") && inFirstHalf) return i+1;

		return i;

   }

    
}
