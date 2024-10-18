import java.util.*;

//Max Nadel
//Program description
//Sep 24, 2024

public class BigDuty
{

	public static Map<String, String> schedule = new HashMap<>();

   private ArrayList<Assignment> assignments;
   
   private ArrayList<Teacher> teachers;
   
   public BigDuty(String dataFile)
   {

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
	   ArrayList<Teacher> fTeachers = getFreeTeachers(day, period);
	   for(int i = 0; i < fTeachers.size(); i++)
	   {
		   int score = fTeachers.get(i).scoreAssignment(day, period);
		   tScores.add(new TeacherWithScore(fTeachers.get(i),score));
	   }
	   
	   Collections.sort(tScores);
	   
	   return tScores;
	   
//		   sort scores

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
	   ArrayList<TeacherWithScore> possTeachers = getListOfRankedTeachers(day, period);
	   
	   return possTeachers.get(0).getTeacher();
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
