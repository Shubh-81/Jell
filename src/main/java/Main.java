import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.print("$ ");
            String input = br.readLine();

            System.out.println(input + ": command not found");
        }

        br.close();
    }
}
