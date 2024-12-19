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
import java.util.Scanner;

public class FileUtility {
//        ArrayList<Teacher> teachers = new BigDuty("/Users/nmadaio26/Desktop/BigDuty2024-25.csv", true).getTeachers();

//        downloadCurrentSchedule(teachers);
//        readSave("/Users/nmadaio26/Desktop/BigDuty2024-25.csv");
        
//        System.out.println(teachers.get(0).getFrees());
    	
        
//        System.out.println("Done!");
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


    /*
     * exporting / importing saved data -------
     */

    
    //saving data
    public static void saveData(ArrayList<Teacher> teachers)
    {
    	//saving all the data, append being false as it clears the file so there is no conflict
    	try (FileWriter writer = new FileWriter("src/saveData.csv", false)) 
    	{
    		
    		//fail safe making sure there are items to save
            if(teachers.size() == 0) {
            	System.out.println("no teachers in list");
            	return;
            }
                        
            
            //variable indicators
            String TEACHER_INDACATOR = "*";
            
            String COURSE_INDACATOR = "#";
            String FREE_INDACATOR = "$";
            String DUTY_INDACATOR = "@";
            String LUNCH_INDACATOR = "&";
                        
            for(Teacher t : teachers)
            {
            	
            	writer.write(TEACHER_INDACATOR + "," + t.getLastName() + "," + t.getFirstName() + "," + 
            				 t.getDepartment() + "," + t.getRoom() + "\n");
            	
            	for(Assignment a : t.getAssignments())
            	{
            		String preciseAssingmentToString = "ERROR! PLEASE SEND FOR HELP!";
            		
            		//finding the type and setting what to print
            		if(a instanceof Course) {
            			Course c = (Course) a;
//            			preciseAssingmentToString = "Course: " + c.toString();
            			preciseAssingmentToString =  COURSE_INDACATOR + "," + c.getName() + "," + c.getCourseCode() + "," + c.getSection() + 
            										 "," + c.getPeriod() + "," + c.getDay() + "," + c.getSemester()
    												 + "," + c.getRoom() + "," + c.getDepartment() + "," + c.getTeacher().getName();
            		}
            		
            		if(a instanceof Free) {
            			Free c = (Free) a;
//            			preciseAssingmentToString = "Free: " + c.toString();
            			preciseAssingmentToString =  FREE_INDACATOR + "," + c.getPeriod() + 
            										 "," + c.getSemester() + "," + c.getName() + "," + c.getDay()
            										 + "," + c.getTeacher().getName() + ", " + c.isLocked();
            		}
            		
            		if(a instanceof Duty) {
            			Duty c = (Duty) a;
//            			preciseAssingmentToString = "Duty: " + c.toString();
            			preciseAssingmentToString =  DUTY_INDACATOR + "," + c.getPeriod() + 
            										 "," + c.getSemester() + "," + c.getName() + "," + c.getRoom()
            										 + "," + c.getDay() + "," + c.getTeacher().getName();
            		}
            		
            		if(a instanceof Lunch) {
            			Lunch c = (Lunch) a;
//            			preciseAssingmentToString = "Duty: " + c.toString();
            			preciseAssingmentToString =  LUNCH_INDACATOR + "," + c.getPeriod() + 
            					"," + c.getSemester() + "," + c.getName()
            					+ "," + c.getDay() + "," + c.getTeacher().getName();
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
    
    public static void downloadCurrentSchedule(ArrayList<Teacher> teachers)
    {
        //saving a new set of data
        saveData(teachers);
    	download();
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
    public static ArrayList<Teacher> readSaveFile()
    {
    	return readSave("src/saveData.csv");
    }
    
    /*
     * -will never be THREE of ONE duty
     * -coverages are last
     */
    
    private static ArrayList<Teacher> readSave(String fileLocation)
    {
		ArrayList<Teacher> teachers = new ArrayList<Teacher>();
		ArrayList<String> data = readData(fileLocation);
		
		String TEACHER_INDACATOR = "*";
		
		String COURSE_INDACATOR = "#";
		String FREE_INDACATOR = "$";
		String DUTY_INDACATOR = "@";
        String LUNCH_INDACATOR = "&";
    	 
        int i = 0;
    	for(String rowData : data)
    	{
    		System.out.println("\n" + i + "\n");
    		i++;
    		String[] tokens = rowData.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
    		 
    		if(tokens[0].equals(TEACHER_INDACATOR)) {
    			 teachers.add(new Teacher(tokens[1], tokens[2], tokens[3], tokens[4]));
    		}
    		if(tokens[0].equals(COURSE_INDACATOR)) {
    			teachers.get(teachers.size()-1).addAssignment(new Course(tokens[1], tokens[2], Integer.parseInt(tokens[3]), tokens[4], tokens[5], 
    																	 tokens[6], tokens[7], tokens[8], teachers.get(teachers.size()-1), true));
    		}
    		if(tokens[0].equals(FREE_INDACATOR)) {
    			teachers.get(teachers.size()-1).addAssignment(new Free(new Period(tokens[1]), tokens[2], tokens[3], 
    																   tokens[4], teachers.get(teachers.size()-1)));
    			Free f = (Free) teachers.get(teachers.size()-1).getAssignment(tokens[4], new Period(tokens[1]));
    			
    			boolean locked = tokens[6].equalsIgnoreCase("true") ? true : false;
    			
    			f.setLocked(locked);
    		}
    		if(tokens[0].equals(DUTY_INDACATOR)) {
    			teachers.get(teachers.size()-1).addAssignment(new Duty(tokens[1], tokens[2], tokens[3], tokens[4], 
    														  		   tokens[5], teachers.get(teachers.size()-1), false));
    		}
    		if(tokens[0].equals(LUNCH_INDACATOR)) {
    			teachers.get(teachers.size()-1).addAssignment(new Lunch(tokens[1], tokens[2], tokens[3], tokens[4], teachers.get(teachers.size()-1)));
    		}
    		 
    	}
    	
    	return teachers;
    	 
   }
    
}
