import java.util.ArrayList;
import java.util.Collections;

//Max Nadel
//Program description
//Sep 24, 2024

public class BigDuty
{
   private ArrayList<Assignment> assignments;
   
   private ArrayList<Teacher> teachers;

   public static String M_FORMAT = "01234L5678";
   public static String T_FORMAT = "0123L567";
   public static String W_FORMAT = "0412L856";
   public static String R_FORMAT = "0341L785";
   public static String F_FORMAT = "234L678";
   
   public BigDuty(ArrayList<Assignment> assignments, ArrayList<Teacher> teachers)
   {
      this.assignments = assignments;
      this.teachers = teachers;

//	  populateAssignments();


   }
   
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
   
    
}