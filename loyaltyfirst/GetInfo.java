import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GetInfo {
    public static String[] getInfo(){
        String connString = "";
        String user = "";
        String pass = "";

        try(Scanner sc = new Scanner(new File("info.txt"))){
            String[] info = sc.next().split("\n");

            if(info.length<3){
                System.err.println("Not enough information was provided");
                System.exit(1);
            }

            connString = info[0].trim().replace("connection = ", "");
            user = info[1].trim().replace("user = ", "");
            pass = info[2].trim().replace("pass = ", "");

        } catch(FileNotFoundException ex){
            System.err.println("No information file found");
            System.exit(1);
        }

        return new String[]{connString, user, pass};
    }
}
