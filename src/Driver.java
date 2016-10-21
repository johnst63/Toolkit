import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Sheolfire on 8/22/2016.
 */
public class Driver {
    public static void main(String[] args) {

        Toolkit tools = new Toolkit();
        boolean exit = false;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(System.in));
            while(!exit) {
                System.out.println("Enter A Command:");
                String input = reader.readLine();

                if(input.toLowerCase().equals("exit")) {
                    exit = true;
                } else {
                    StatusMessage result = tools.parseCommand(input);
                    if(result.getStatusCode() <= 0)
                        System.out.println(result.getStatusMessage());
                    else {
                        tools.addToInputCache(input);
                    }

                }
            }
            if (reader != null) {
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }



    }
}
