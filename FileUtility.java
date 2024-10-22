import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class FileUtility {


    public static void main(String[] args) {
        ArrayList<Teacher> teachers = createTeachers("src/PollingerProject-DutyData.csv");

        System.out.println("Done!");
    }


    public static ArrayList<Teacher> createTeachers(String filename) {
        ArrayList<Teacher> teachers = new ArrayList<Teacher>();

        ArrayList<String> data = readData(filename);



        //adding all teachers from the file to the list
        for (int i = 1; i < data.size(); i++) {

            for (Teacher nextTeach : getTeachersFromRowData(data.get(i))) {


//                System.out.println(data.get(i));
                boolean addTeacher = true;

                if (nextTeach != null) {
                    for (Teacher t : teachers)
                        if (nextTeach.getLastName().equals(t.getLastName())
                                && nextTeach.getFirstName().equals(t.getFirstName()))
                            addTeacher = false;
                    if (addTeacher)
                        teachers.add(nextTeach);
                }
            }

        }


//		for(Teacher t : teachers)
//			System.out.println(t);

        //add courses to teachers

        for (int i = 1; i < data.size(); i++) {
            addCourseToTeacher(data.get(i), teachers);
        }

//		for(FileUtility line : row)
//		{
//			String name = line.firstName + " " + line.lastName;
//			boolean reTeacher = checkReTeacher(line, name); //checking if there is the same teacher already
//			
//			
//			if(!reTeacher) {
//				teacher.add(new Teacher(name, new Course(line.period, line.semester,
//						line.course, line.room, name), 1));
//			}
//			
//			if(reTeacher) {
//				teacher.get(teacher.size()-1).addCourse(new Course(line.period, line.semester,
//						line.course, line.room, name));
////				System.out.println("add course");
//			}

//		  }

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

                    if((a.getDay() == tokens[4].substring(1, tokens[4].length() - 1))) addCourse = false;

                }

            }

//            System.out.println(tokens[4]);

            String name = tokens[6].substring(1, tokens[6].length() - 1);
            String semester = tokens[5].substring(1, tokens[5].length() - 1);
            String courseCode = tokens[2].substring(1, tokens[2].length() - 1);
            String day = tokens[4].substring(1, tokens[4].length() - 1);
            String period = tokens[4].substring(1, 2);
            int section = Integer.parseInt(tokens[3].substring(1, tokens[3].length() - 1));
            String department = tokens[8].substring(1, tokens[8].length() - 1);
            String room = tokens[7].substring(1, tokens[7].length() - 1);



            if(addCourse) {
                Course nextCourse = new Course(name, courseCode, section, period, day,
                        semester, room, department, teacher, true);
                teacher.addAssignment(nextCourse);
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


//	private static boolean checkReTeacher(FileUtility line, String name)
//	{
//		boolean reTeacher = false;
//		try
//		{
//			for(Teacher t : teacher)
//			{
//				reTeacher = name.equals(t.getName());
//			}
//		}
//		catch(IndexOutOfBoundsException e)
//		{
//				teacher.add(new Teacher(name, new Course(line.period, line.semester,
//										line.course, line.room, name), 1));
//		}
//		
//		return reTeacher;
//	}

    private static ArrayList<Teacher> getTeachersFromRowData(String rowData) {
        /*
         * creating an array list with all the separate items and then
         * creating the Teacher objects
         */

        String[] tokens = rowData.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

        ArrayList<Teacher> teachers = new ArrayList<>();

//        System.out.println(Arrays.toString(tokens));
        if (tokens[9].length() == 2)
            return teachers;
//        System.out.println(tokens[9]);

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

//		dataPoints.add(str.substring(1, str.length()-1));
//		dataPoints.set(dataPoints.size() - 2, dataPoints.get(dataPoints.size() - 2).substring(1));
//		
//		//setting the values to a teacher
//		row.add(new FileUtility(dataPoints.get(0), dataPoints.get(1), dataPoints.get(2), Integer.parseInt(dataPoints.get(3)), 
//								 Integer.parseInt(dataPoints.get(4)), dataPoints.get(5), dataPoints.get(6), 
//								 dataPoints.get(7), dataPoints.get(8), dataPoints.get(9), dataPoints.get(10)));
//		dataPoints.clear();
    }

    private static ArrayList<String> readData(String fileName) {
        ArrayList<String> textData = new ArrayList<String>();
        File filePath = new File(fileName);
        try {
//	         System.out.println("Reading data");
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