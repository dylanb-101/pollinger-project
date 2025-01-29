import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class FileUtility {


//        ArrayList<Teacher> teachers = new BigDuty("/Users/nmadaio26/Desktop/BigDuty2024-25.csv", true).getTeachers();
//
//        downloadCurrentSchedule(teachers);
//        readSave("/Users/nmadaio26/Desktop/BigDuty2024-25.csv");
//
//        System.out.println(teachers.get(0).getFrees());
//
//
//        System.out.println("Done!");


    private static String TEACHER_INDICATOR = "*";
    private static String SEMESTER_INDICATOR = "^";

    private static String COURSE_INDICATOR = "#";
    private static String FREE_INDICATOR = "$";
    private static String DUTY_INDICATOR = "@";
    private static String LUNCH_INDICATOR = "&";

    public static ArrayList<BigDuty> createBigDuties(File file, GUIMain gui) {
        ArrayList<BigDuty> semesters = new ArrayList<>();

        ArrayList<String> data = readData(file);

        int i = 0;

        while(i < data.size()) {

            String line = data.get(i);

            if(line.charAt(0) == SEMESTER_INDICATOR.charAt(0)) {

                ArrayList<Teacher> teachers = new ArrayList<>();

                ArrayList<DutySlot> slots = new ArrayList<>();

                for(int m = 0; m < line.split(",")[2].split(";").length; m++) {

                    String[] parts = line.split(",")[2].split(";")[m].split("\\|");

                    DutyType type = DutyType.valueOf(parts[0]);
                    Period p = new Period(parts[1]);
                    String day  = parts[2];
                    int number = Integer.parseInt(parts[3]);

                    slots.add(new DutySlot(type, p, day, number));

                }

                int j = i+1;

                System.out.println("heyy");
                while(j < data.size() && data.get(j).charAt(0) != SEMESTER_INDICATOR.charAt(0)) {

                    String[] tokens = data.get(j).split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

                    if(tokens[0].equals(TEACHER_INDICATOR)) {

                        Teacher t = new Teacher(tokens[1], tokens[2], tokens[3], tokens[4]);
                        t.setSocialCredit(Integer.parseInt(tokens[5]));

                        try {
                            t.setMaxDuties(Integer.parseInt(tokens[6]));

                        } catch (Exception e) {
                            System.out.println("ERROR READING IN TEACHER DATA SOME DATA WILL BE MISSING BECAUSE OF OLD FILE FORMAT");
                        }
                        t.setBigDuty(new BigDuty(line.split(",")[1]));

                        teachers.add(t);

                    }
                    if (tokens[0].equals(COURSE_INDICATOR)) {

                        Course course = new Course(tokens[1], tokens[2], Integer.parseInt(tokens[3]), tokens[4], tokens[5],
                                tokens[6], tokens[7], tokens[8], teachers.get(teachers.size() - 1), false);

                        teachers.get(teachers.size() - 1).addAssignment(course);
                    }
                    if (tokens[0].equals(FREE_INDICATOR)) {
                        Free f = new Free(new Period(tokens[1]), tokens[2], tokens[3],
                                tokens[4], teachers.get(teachers.size() - 1));
                        teachers.get(teachers.size() - 1).addAssignment(f);

                        boolean locked = tokens[6].equalsIgnoreCase(" true");
                        System.out.println(tokens[6]);

                        f.setLocked(locked);
                    }
                    if (tokens[0].equals(DUTY_INDICATOR)) {
                        Duty a = new Duty(tokens[1], tokens[2], tokens[3], tokens[4],
                                tokens[5], teachers.get(teachers.size() - 1), false);

                        if (tokens[7].equals("PASCACK")) {
                            a.setType(DutyType.PASCACK);
                        } else if (tokens[7].equals("COVERAGE")) {
                            a.setType(DutyType.COVERAGE);
                        } else if (tokens[7].equals("FROSH_PASCACK")) {
                            a.setType(DutyType.FROSH_PASCACK);
                        } else if (tokens[7].equals("HALL")) {
                            a.setType(DutyType.HALL);
                        } else if (tokens[7].equals("UNASSIGNED")) {
                            a.setType(DutyType.UNASSIGNED);
                        }


                        teachers.get(teachers.size() - 1).addAssignment(a);
                    }
                    if (tokens[0].equals(LUNCH_INDICATOR)) {
                        teachers.get(teachers.size() - 1).addAssignment(new Lunch(tokens[1], tokens[2], tokens[3], tokens[4], teachers.get(teachers.size() - 1)));
                    }


                    j++;

                }

                BigDuty duty = new BigDuty(teachers, line.split(",")[1]);

                for(DutySlot d : slots) {
                    duty.addDutySlot(d);
                }


                semesters.add(duty);

                i = j;



            }

        }

        return semesters;



    }

    public static ArrayList<Teacher> createTeachers(File file) {

        ArrayList<Teacher> teachers = new ArrayList<Teacher>();

        ArrayList<String> data = readData(file);

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

    public static ArrayList<Teacher> createTeachers(String filename) {
        File file = new File(filename);

        return createTeachers(file);

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

        System.out.print(Arrays.toString(tokens));

        if(tokens[9].length() == 2)
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

            Teacher t = new Teacher(last, first, department, room);
            t.setBigDuty(new BigDuty("S1"));
            teachers.add(t);

        }

        return teachers;
    }

    private static ArrayList<String> readData(String fileName) {

        File filePath = new File(fileName);

        return readData(filePath);
    }

    private static ArrayList<String> readData(File filePath) {
        ArrayList<String> textData = new ArrayList<String>();

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


    /*
     * exporting / importing saved data -------
     */


    // saving data
    public static void saveData(GUIMain gui)
    {
    	//saving all the data, append being false as it clears the file so there is no conflict
    	try (FileWriter writer = new FileWriter("src/saveData.csv", false))
    	{

    		//fail safe making sure there are items to save
            if(gui.semesters.size() == 0) {
            	System.out.println("no semesters in list");
            	return;
            }


            //variable indicators



            for(SemesterTab semester : gui.semesters) {

                writer.write(SEMESTER_INDICATOR + "," + semester.getName() + "," + semester.getBigDuty().dutySlotsToSaveString() + "\n");

                for (Teacher t : semester.getBigDuty().getTeachers()) {

                    writer.write(TEACHER_INDICATOR + "," + t.getLastName() + "," + t.getFirstName() + "," +
                            t.getDepartment() + "," + t.getRoom() + "," + t.getSocialCredit() + "," + t.getMaxDuties() + "\n");

                    int i = 0;
                    for (Assignment a : t.getAssignments()) {
                        String preciseAssingmentToString = "ERROR! PLEASE SEND FOR HELP!";

                        i++;


                        //finding the type and setting what to print
                        if (a instanceof Course) {
                            Course c = (Course) a;
//            			preciseAssingmentToString = "Course: " + c.toString();
                            preciseAssingmentToString = COURSE_INDICATOR + "," + c.getName() + "," + c.getCourseCode() + "," + c.getSection() +
                                    "," + c.getPeriod().getPeriod() + "," + c.getDay() + "," + c.getSemester()
                                    + "," + c.getRoom() + "," + c.getDepartment() + "," + c.getTeacher().getName();
                        }

                        if (a instanceof Free) {
                            Free c = (Free) a;
//            			preciseAssingmentToString = "Free: " + c.toString();
                            preciseAssingmentToString = FREE_INDICATOR + "," + c.getPeriod().getPeriod() +
                                    "," + c.getSemester() + "," + c.getName() + "," + c.getDay()
                                    + "," + c.getTeacher().getName() + ", " + c.isLocked();
                        }

                        if (a instanceof Duty) {
                            Duty c = (Duty) a;
//            			preciseAssingmentToString = "Duty: " + c.toString();
                            preciseAssingmentToString = DUTY_INDICATOR + "," + c.getPeriod().getPeriod() +
                                    "," + c.getSemester() + "," + c.getName() + "," + c.getRoom()
                                    + "," + c.getDay() + "," + c.getTeacher().getName() + "," + c.getType().name();
                        }

                        if (a instanceof Lunch) {
                            Lunch c = (Lunch) a;
//            			preciseAssingmentToString = "Duty: " + c.toString();
                            preciseAssingmentToString = LUNCH_INDICATOR + "," + c.getPeriod().getPeriod() +
                                    "," + c.getSemester() + "," + c.getName()
                                    + "," + c.getDay() + "," + c.getTeacher().getName();
                        }


                        //printing the data onto the save file
                        writer.write(preciseAssingmentToString + "\n");
                    }

                }
            }
        }
    	catch (IOException e)
    	{
            System.out.println("no file found");
        }
    }

    private static void copyFile(String sourceFilePath, String destinationFilePath) throws IOException {
    	File sourceFile = new File(sourceFilePath);

    	if (!sourceFile.exists())
    		throw new IOException("Source file does not exist: " + sourceFilePath);

    	FileInputStream inputStream = new FileInputStream(sourceFile);
    	ReadableByteChannel readableByteChannel = Channels.newChannel(inputStream);
    	FileOutputStream fileOutputStream = new FileOutputStream(destinationFilePath);


    	fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);

    	fileOutputStream.close();
    	readableByteChannel.close();

    	inputStream.close();
    }

    private static void download(File destination) {

        String nextYear = (Year.now().getValue()+1) + "";
        String yearCombined = Year.now().getValue() + "-" + nextYear.substring(2);

        String sourceFilePath = "src/saveData.csv"; // Replace with the actual path to your source file
        String destPath = destination.getAbsolutePath(); // Get the desktop path
        String destinationFilePath = destPath + "/BigDuty" + yearCombined + ".pol"; // Specify the filename

        try {
            copyFile(sourceFilePath, destinationFilePath);
        }
        catch (IOException e) {
            System.out.println("ERROR FINDING FILE. PLEASE CONTACT SUPPORT");
        }

//        System.out.println("File copied successfully to your desktop!");

    }

//    private static void save(ArrayList<Teacher> teachers, File destination, String name) {
//
//        try (FileWriter writer = new FileWriter(destination.getAbsolutePath() + "/" + name, false))
//        {
//
//            //fail safe making sure there are items to save
//            if(teachers.size() == 0) {
//                System.out.println("no teachers in list");
//                return;
//            }
//
//
//            //variable indicators
//            String TEACHER_INDACATOR = "*";
//
//            String COURSE_INDACATOR = "#";
//            String FREE_INDACATOR = "$";
//            String DUTY_INDACATOR = "@";
//            String LUNCH_INDACATOR = "&";
//
//            for(Teacher t : teachers)
//            {
//
//                writer.write(TEACHER_INDACATOR + "," + t.getLastName() + "," + t.getFirstName() + "," +
//                        t.getDepartment() + "," + t.getRoom() + "\n");
//
//                int i = 0;
//                for(Assignment a : t.getAssignments())
//                {
//                    String preciseAssingmentToString = "ERROR! PLEASE SEND FOR HELP!";
//
//                    i++;
//
//
//                    //finding the type and setting what to print
//                    if(a instanceof Course) {
//                        Course c = (Course) a;
////            			preciseAssingmentToString = "Course: " + c.toString();
//                        preciseAssingmentToString =  COURSE_INDACATOR + "," + c.getName() + "," + c.getCourseCode() + "," + c.getSection() +
//                                "," + c.getPeriod().getPeriod() + "," + c.getDay() + "," + c.getSemester()
//                                + "," + c.getRoom() + "," + c.getDepartment() + "," + c.getTeacher().getName();
//                    }
//
//                    if(a instanceof Free) {
//                        Free c = (Free) a;
////            			preciseAssingmentToString = "Free: " + c.toString();
//                        preciseAssingmentToString =  FREE_INDACATOR + "," + c.getPeriod().getPeriod() +
//                                "," + c.getSemester() + "," + c.getName() + "," + c.getDay()
//                                + "," + c.getTeacher().getName() + ", " + c.isLocked();
//                    }
//
//                    if(a instanceof Duty) {
//                        Duty c = (Duty) a;
////            			preciseAssingmentToString = "Duty: " + c.toString();
//                        preciseAssingmentToString =  DUTY_INDACATOR + "," + c.getPeriod().getPeriod() +
//                                "," + c.getSemester() + "," + c.getName() + "," + c.getRoom()
//                                + "," + c.getDay() + "," + c.getTeacher().getName();
//                    }
//
//                    if(a instanceof Lunch) {
//                        Lunch c = (Lunch) a;
////            			preciseAssingmentToString = "Duty: " + c.toString();
//                        preciseAssingmentToString =  LUNCH_INDACATOR + "," + c.getPeriod().getPeriod() +
//                                "," + c.getSemester() + "," + c.getName()
//                                + "," + c.getDay() + "," + c.getTeacher().getName();
//                    }
//
//
//
//                    //printing the data onto the save file
//                    writer.write(preciseAssingmentToString + "\n");
//                }
//
//            }
//        }
//        catch (IOException e)
//        {
//            System.out.println("no file found");
//        }
//
//    }

    private static void download()
    {
    	//getting the year to add to the file name
        String nextYear = (Year.now().getValue()+1) + "";
        String yearCombined = Year.now().getValue() + "-" + nextYear.substring(2);

    	String sourceFilePath = "src/saveData.csv"; // Replace with the actual path to your source file
        String desktopPath = System.getProperty("user.home") + "/Desktop/"; // Get the desktop path
        String destinationFilePath = desktopPath + "BigDuty" + yearCombined + ".csv"; // Specify the filename

        try {
			copyFile(sourceFilePath, destinationFilePath);
		}
        catch (IOException e) {
			System.out.println("ERROR FINDING FILE. PLEASE CONTACT SUPPORT");
		}

//        System.out.println("File copied successfully to your desktop!");
    }

    public static void downloadCurrentSchedule(GUIMain gui)
    {
        //saving a new set of data
        saveData(gui);
    	download();
    }

    public static void downloadCurrentSchedule(GUIMain gui, File file) {

        saveData(gui);
        download(file);

    }

    public static void downloadPreviousSave()
    {
    	download();
    }

    //reading/importing data

    public static ArrayList<Teacher> readSaveFile(String fileLocation)
    {
    	return readSave(fileLocation);
    }

    public static ArrayList<Teacher> readSaveFile(File file) {

        return readSave(file);

    }

    public static ArrayList<Teacher> readSaveFile()
    {
    	return readSave("src/saveData.csv");
    }

    /*
     * -will never be THREE of ONE duty
     * -coverages are last
     */

    private static ArrayList<Teacher> readSave(String fileLocation) {

        ArrayList<String> data = readData(fileLocation);

        return readSave(data);

    }

    private static ArrayList<Teacher> readSave(ArrayList<String> data) {

        ArrayList<Teacher> teachers = new ArrayList<Teacher>();

        final String TEACHER_INDACATOR = "*";

        final String COURSE_INDACATOR = "#";
        final String FREE_INDACATOR = "$";
        final String DUTY_INDACATOR = "@";
        final String LUNCH_INDACATOR = "&";

        int i = 0;
        for(String rowData : data)
        {
            i++;
            String[] tokens = rowData.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

            if(tokens[0].equals(TEACHER_INDACATOR)) {

                Teacher t = new Teacher(tokens[1], tokens[2], tokens[3], tokens[4]);
                t.setSocialCredit(Integer.parseInt(tokens[5]));
                t.setBigDuty(new BigDuty("S1"));

                teachers.add(t);

            }
            if(tokens[0].equals(COURSE_INDACATOR)) {

                Course course = new Course(tokens[1], tokens[2], Integer.parseInt(tokens[3]), tokens[4], tokens[5],
                        tokens[6], tokens[7], tokens[8], teachers.get(teachers.size() - 1), false);
                teachers.get(teachers.size()-1).addAssignment(course);
            }
            if(tokens[0].equals(FREE_INDACATOR)) {
                Free f = new Free(new Period(tokens[1]), tokens[2], tokens[3],
                        tokens[4], teachers.get(teachers.size() - 1));
                teachers.get(teachers.size()-1).addAssignment(f);

                boolean locked = tokens[6].equalsIgnoreCase("true");

                f.setLocked(locked);
            }
            if(tokens[0].equals(DUTY_INDACATOR)) {
                Duty a = new Duty(tokens[1], tokens[2], tokens[3], tokens[4],
                        tokens[5], teachers.get(teachers.size() - 1), false);

                if(tokens[7].equals("PASCACK")) {
                    a.setType(DutyType.PASCACK);
                } else if(tokens[7].equals("COVERAGE")) {
                    a.setType(DutyType.COVERAGE);
                } else if(tokens[7].equals("FROSH_PASCACK")) {
                    a.setType(DutyType.FROSH_PASCACK);
                } else if(tokens[7].equals("HALL")) {
                    a.setType(DutyType.HALL);
                } else if(tokens[7].equals("UNASSIGNED")) {
                    a.setType(DutyType.UNASSIGNED);
                }



                teachers.get(teachers.size()-1).addAssignment(a);
            }
            if(tokens[0].equals(LUNCH_INDACATOR)) {
                teachers.get(teachers.size()-1).addAssignment(new Lunch(tokens[1], tokens[2], tokens[3], tokens[4], teachers.get(teachers.size()-1)));
            }

        }

        return teachers;
    }

    private static ArrayList<Teacher> readSave(File file)
    {
		ArrayList<String> data = readData(file);
        return readSave(data);

   }

}
