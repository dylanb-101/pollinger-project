//import java.io.File;
//import java.io.FileNotFoundException;
//import java.util.ArrayList;
//import java.util.Scanner;
//
//public class FileReadIn {
//    protected static ArrayList<FileReadIn> row = new ArrayList<FileReadIn>();
//    protected static ArrayList<Teacher> teachers = new ArrayList<Teacher>();
//    private static String fileName;
//
//    private String schoolYear;
//    private String schoolCode;
//    private String courseCode;
//    private int section;
//    private int period;
//    private String semester;
//    private String course;
//    private String room;
//    private String Department;
//    private String lastName;
//    private String firstName;
//
//
//
//    public FileReadIn(String schoolYear, String schoolCode, String courseCode, int section, int period, String semester,
//                      String course, String room, String department, String lastName, String firstName)
//    {
//        this.schoolYear = schoolYear;
//        this.schoolCode = schoolCode; //dont use
//        this.courseCode = courseCode;
//        this.section = section; //dont use
//        this.period = period;
//        this.semester = semester;
//        this.course = course;
//        this.room = room;
//        Department = department;
//        this.lastName = lastName;
//        this.firstName = firstName;
//    }
//
//    public static void createTeachers()
//    {
//        ArrayList<String> data = readData();
//
//        //adding all teachers from the file to the list
//        for(int i = 1; i < data.size(); i++)
//        {
//            addRow(i, data);
//        }
//
//        for(FileReadIn line : row)
//        {
//            String name = line.firstName + " " + line.lastName;
//            boolean reTeacher = checkReTeacher(line, name); //checking if there is the same teacher already
//
//
//            if(!reTeacher) {
////                teacher.add(new Teacher(name, new Course(line.period, line.semester,
////                        line.course, line.room, name), 1));
//
//                Teacher t = new Teacher(name);
//
////                t.addAssignment(new Course(Assignment.adjustPeriod(line.period), Integer.parseInt(line.semester), line.course, line.room, 1, t));
//
//                teachers.add(new Teacher(name));
//            }
//
//            if(reTeacher) {
////                teachers.get(teachers.size()-1).addAssignment(new Course(line.period, Integer.parseInt(line.semester),
//                        line.course, line.room, 1, teachers.get(teachers.size()-1)));
////				System.out.println("add course");
//            }
//
//        }
//
//    }
//
//    private static boolean checkReTeacher(FileReadIn line, String name)
//    {
//        boolean reTeacher = false;
//        try
//        {
//            for(Teacher t : teachers)
//            {
//                reTeacher = name.equals(t.getName());
//            }
//        }
//        catch(IndexOutOfBoundsException e)
//        {
//            Teacher t = new Teacher(name);
//
//            t.addAssignment(new Course(line.period, Integer.parseInt(line.semester), line.course, line.room, 1, t));
//        }
//
//        return reTeacher;
//    }
//
//    private static void addRow(int i, ArrayList<String> data)
//    {
//        /*
//         * creating an array list with all the seperate items and then
//         * creating the Teacher objects
//         */
//        ArrayList<String> dataPoints = new ArrayList<String>();
//
//        String str = data.get(i);
//        while(str.indexOf(",") != -1)
//        {
//            dataPoints.add(str.substring(0, str.indexOf(",")));
//            str = str.substring(str.indexOf(",")+1);
//        }
//        dataPoints.add(str.substring(1, str.length()-1));
//        dataPoints.set(dataPoints.size() - 2, dataPoints.get(dataPoints.size() - 2).substring(1));
//
//        //setting the values to a teacher
//        row.add(new FileReadIn(dataPoints.get(0), dataPoints.get(1), dataPoints.get(2), Integer.parseInt(dataPoints.get(3)),
//                Integer.parseInt(dataPoints.get(4)), dataPoints.get(5), dataPoints.get(6),
//                dataPoints.get(7), dataPoints.get(8), dataPoints.get(9), dataPoints.get(10)));
//        dataPoints.clear();
//    }
//
//    private static ArrayList<String> readData()   {
//        ArrayList<String> textData = new ArrayList<String>();
//        File filePath = new File(fileName);
//        try  {
////	         System.out.println("Reading data");
//            Scanner dataGetter = new Scanner(filePath);
//            while(dataGetter.hasNext())
//                textData.add(dataGetter.nextLine());
//            dataGetter.close();
//        }
//        catch (FileNotFoundException e){
//            System.out.println("Cannot find data file.");
//        }
//        return textData;
//    }
//
//    public static void readCSV(String file)
//    {
//        fileName = file;
//        createTeachers();
//    }
//
//    @Override
//    public String toString() {
//        return "Teacher " + firstName + " [schoolYear=" + schoolYear + ", schoolCode=" + schoolCode + ", courseCode=" + courseCode
//                + ", section=" + section + ", period=" + period + ", Semester=" + semester + ", course=" + course
//                + ", room=" + room + ", Department=" + Department + ", lastName=" + lastName + ", firstName="
//                + firstName + "]";
//    }
//
//}
//
