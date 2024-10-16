import java.util.ArrayList;
import java.util.Collections;

//Max Nadel
//Program description
//Sep 24, 2024

public class BigDuty
{
   private ArrayList<Assignment> assignments;
   
   private ArrayList<Teacher> teachers;
   
   public BigDuty(ArrayList<Assignment> assignments, ArrayList<Teacher> teachers)
   {
      this.assignments = assignments;
      this.teachers = teachers;
   }
   
   // returns a list of teachers with an attached score going from largest score to smallest
   public ArrayList<TeacherWithScore> getListOfRankedTeachers(int day, int period)
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
   
   public ArrayList<Teacher> getFreeTeachers(int day, int period)
   {	 
	   ArrayList<Teacher> freeTeachers = new ArrayList<Teacher>();
	   
	   for(int i = 0; i < teachers.size(); i++)
		   for(int k = 0; k < teachers.get(i).getFrees().size(); k++)
			   if(teachers.get(i).getFrees().get(k).getDay() == day &&  
			      teachers.get(i).getFrees().get(k).getPeriod() == period)
				   freeTeachers.add(teachers.get(i));
	   
	   return freeTeachers;
   }
   
   public Teacher getBestTeacher(int day, int period)
   {
	   ArrayList<TeacherWithScore> possTeachers = getListOfRankedTeachers(day, period);
	   
	   return possTeachers.get(0).getTeacher();
   }
   
    
}