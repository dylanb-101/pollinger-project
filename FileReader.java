//Dylan Barrett
//Description
//10/7/24

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FileReader {

    private static ArrayList<Teacher> teachers;
    private static ArrayList<Assignment> assignments;


//    the keys for each item needed in the dictionary
    public static String COURSE_CODE = "Course Code";
    public static String PERIOD = "Period";
    public static String SEMESTER = "Semester";
    public static String COURSE_NAME = "Course Description";
    public static String ROOM = "Room";
    public static String TEACHER_NAME = "Teacher";

    public static void parseGenesisReport(String filename) {


        ArrayList<String> textData = readCSV(filename);

        ArrayList<String> keys = new ArrayList<>();

        ArrayList<Map<String, String>> parsedData = new ArrayList<>();

        String regex = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";

        for(String key : textData.get(0).split(regex)) {

            keys.add(key);

        }

        for(int i = 1; i < textData.size(); i++)  {

            String dataString = textData.get(i);

            String[] data = dataString.split(regex);

            Map<String, String> map = new HashMap<>();

            for(int j = 0; j < data.length; j++) {

                map.put(keys.get(i), data[i]);

            }

            parsedData.add(map);

        }

        for(Map<String, String> dict : parsedData) {

            if(!teacherExists(dict.get(TEACHER_NAME))) {

                Teacher teacher = new Teacher(dict.get(TEACHER_NAME));





            }

        }



    }

    public static boolean teacherExists(String teacher) {

        for(Teacher t : teachers) {

            if(t.getName() == teacher) return true;

        }

        return false;

    }

    public static ArrayList<String> readCSV(String filename) {

        filename = "src/" + filename;

        ArrayList<String> textData = new ArrayList<>();

        File filePath = new File(filename);

        try {
            System.out.println("Reading data in " + filename + "...");
            Scanner dataGetter = new Scanner(filePath);

            while(dataGetter.hasNext()) {
                textData.add(dataGetter.nextLine());
            }
            dataGetter.close();
            System.out.println("Data Read!");

        } catch (FileNotFoundException e) {
            System.out.println("Could not find file!");
        }

        return textData;

    }

}
