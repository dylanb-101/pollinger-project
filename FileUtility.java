import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class FileUtility {


    public static void main(String[] args) {
        ArrayList<Teacher> teachers = createTeachers("src/PollingerProject-DutyData.csv");

        saveData(teachers);

        System.out.println("Done!");
    }


    public static ArrayList<Teacher> createTeachers(String filename) {
        ArrayList<Teacher> teachers = new ArrayList<Teacher>();

        ArrayList<String> data = readData(filename);



        //adding all teachers from the file to the list
        for (int i = 1; i < data.size(); i++) {

            for (Teacher nextTeach : getTeachersFromRowData(data.get(i))) {

                boolean addTeacher = true;

                if (nextTeach != null) {
                    for (Teacher t : teachers)
                        if (nextTeach.getLastName().equals(t.getLastName())
                                && nextTeach.getFirstName().equals(t.getFirstName())) {
                            addTeacher = false;
                            break;
                        }
                    if (addTeacher)
                        teachers.add(nextTeach);
                }
            }

        }

        //add courses to teachers

        for (int i = 1; i < data.size(); i++) {
            addCourseToTeacher(data.get(i), teachers);
        }

        return teachers;

    }

    private static void addCourseToTeacher(String courseData, ArrayList<Teacher> teachers) {
        String[] tokens = courseData.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        String first = "First";
        String last = "Staff";

        String[] courseTeachers = tokens[9].split(";");

        if (tokens[9].equals("\"\"")) return;

        for (int i = 0; i < courseTeachers.length; i++) {


            if (tokens[9].indexOf(",") >= 0) {
                last = parseTeacherLastname(courseTeachers[i]);
//			first = courseTeachers[i].split(",")[1].substring(0, courseTeachers[i].split(",")[1].length()-1);
                first = parseTeacherFirstName(courseTeachers[i]);
            }
            Teacher teacher = null;
            boolean addCourse = true;
            for (Teacher t : teachers) {
                if (t.getLastName().equals(last) && t.getFirstName().equals(first)) teacher = t;

                for(Assignment a : t.getAssignments()) {

                    if ((a.getDay() == tokens[4].substring(1, tokens[4].length() - 1))) {
                        addCourse = false;
                        break;
                    }

                }

            }


            String name = tokens[6].substring(1, tokens[6].length() - 1);
            String semester = tokens[5].substring(1, tokens[5].length() - 1);
            String courseCode = tokens[2].substring(1, tokens[2].length() - 1);
            String day = tokens[4].substring(1, tokens[4].length() - 1);
            String period = tokens[4].substring(1, 2);
            int section = Integer.parseInt(tokens[3].substring(1, tokens[3].length() - 1));
            String department = tokens[8].substring(1, tokens[8].length() - 1);
            String room = tokens[7].substring(1, tokens[7].length() - 1);

            addCourse = !name.contains("TA") && day.length() >= 5;
            System.out.println(day);

            if(addCourse) {
                Course nextCourse = new Course(name, courseCode, section, period, day, semester, room, department, teacher, true);

                if(teacher.getAssignment(nextCourse.getDay(), nextCourse.getPeriod()) == null) {
                    teacher.addAssignment(nextCourse);
                }
            }
        }

    }

    public static String parseTeacherLastname(String teacher) {
        return teacher.split(",")[0].substring(1);
    }

    public static String parseTeacherFirstName(String teacher) {

        String last = teacher.split(",")[1];

        if (last.charAt(last.length() - 1) == '"') last = last.substring(0, last.length() - 1);

        return last;

    }

    private static ArrayList<Teacher> getTeachersFromRowData(String rowData) {
        /*
         * creating an array list with all the separate items and then
         * creating the Teacher objects
         */

        String[] tokens = rowData.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

        ArrayList<Teacher> teachers = new ArrayList<>();

        if (tokens[9].length() == 2)
            return teachers;

        String[] teacherStrings = tokens[9].split(";");

        for (int i = 0; i < teacherStrings.length; i++) {

            String first = "First";
            String last = "Staff";

            if (teacherStrings[i].contains(",")) {
                last = parseTeacherLastname(teacherStrings[i]);
                first = parseTeacherFirstName(teacherStrings[i]);
            }

            String department = tokens[8].substring(1, tokens[8].length() - 1);
            String room = tokens[7].substring(1, tokens[7].length() - 1);

            teachers.add(new Teacher(last, first, department, room));

        }

        return teachers;
    }

    public static void saveData(ArrayList<Teacher> teachers)
    {
    	//saving all the data, append being false as it clears the file so there is no conflict
    	try (FileWriter writer = new FileWriter("src/saveData.txt", false))
    	{

    		//fail safe making sure there are items to save
            if(teachers.size() == 0) {
            	System.out.println("no teachers in list");
            	return;
            }

            //saving all teacher assignments
            writer.write("Space Holder (normal csv has all the sections here but would be useless as our system ignores it)\n");

            //constants
            String year = Year.now().getValue() + "";

            //variable indicators
            String COURSE_INDACATOR = "#";
            String FREE_INDACATOR = "$";
            String DUTY_INDACATOR = "@";

            for(Teacher t : teachers)
            {
            	for(Assignment a : t.getAssignments())
            	{
            		String preciseAssingmentToString = "";

            		//finding the type and setting what to print
            		if(a instanceof Course c) {
                        preciseAssingmentToString =  COURSE_INDACATOR + " :" + c.getName() + "\",\"" + c.getCourseCode() + "\",\"" + c.getSection() +
            												"\",\"" + c.getPeriod() + "\",\"" + c.getDay() + "\",\"" + c.getSemester()
            												+ "\",\"" + c.getRoom() + "\",\"" + c.getDepartment() + "\",\"" + c.getTeacher();
            		}

            		if(a instanceof Free c) {
                        preciseAssingmentToString =  FREE_INDACATOR + " :" + c.getPeriod() +
            					"\",\"" + c.getSemester() + "\",\"" + c.getName() + "\",\"" + c.getDay()
            					+ "\",\"" + c.getTeacher();
            		}

            		if(a instanceof Duty c) {
                        preciseAssingmentToString =  DUTY_INDACATOR + " :" + c.getPeriod() +
            					"\",\"" + c.getSemester() + "\",\"" + c.getName() + "\",\"" + c.getRoom()
            					+ "\",\"" + c.getDay() + "\",\"" + c.getTeacher();
            		}

            		//printing the data onto the save file
            		writer.write(preciseAssingmentToString + "\n");
            	}
            }            
        } 
    	catch (IOException e)
    	{
            System.out.println("no file found");
        }
    }

    public void downloadSchedule(ArrayList<Teacher> teachers)
    {
    	saveData(teachers);
    }

    private static ArrayList<String> readData(String fileName) {
        ArrayList<String> textData = new ArrayList<String>();
        File filePath = new File(fileName);
        try {
            Scanner dataGetter = new Scanner(filePath);
            while (dataGetter.hasNext())
                textData.add(dataGetter.nextLine());
            dataGetter.close();
        } catch (FileNotFoundException e) {
            System.out.println("Cannot find data file.");
        }
        return textData;
    }


}
