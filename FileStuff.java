import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileStuff {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        ArrayList<String> data = readData("teacherSchedules_24-25.csv");
        System.out.println(data.get(0));




    }

    public static ArrayList<String> readData(String fileName) {

        fileName = "src/" + fileName;

        ArrayList<String> textData = new ArrayList<String>();
        File filePath = new File(fileName);
        try {
            System.out.println("Reading data...");
            Scanner dataGetter = new Scanner(filePath);
            while(dataGetter.hasNext()) {
                textData.add(dataGetter.nextLine());
            }
            dataGetter.close();
            System.out.println("Data Read!");
        } catch(FileNotFoundException e) {
            System.out.println("Could not find Data file!");
        }
        return textData;
    }
}