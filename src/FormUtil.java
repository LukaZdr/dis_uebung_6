
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Kleine Helferklasse zum Einlesen von Formulardaten
 */
public class FormUtil {
    /**
     * Liest einen String vom standard input ein
     *
     * @param label Zeile, die vor der Eingabe gezeigt wird
     * @return eingelesene Zeile
     */
    public static String readString(String label) {
        String ret = null;
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

        try {
            System.out.print(label + ": ");
            ret = stdin.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }

    /**
     * Liest einen Integer vom standard input ein
     *
     * @param label Zeile, die vor der Eingabe gezeigt wird
     * @return eingelesener Integer
     */
    public static int readInt(String label) {
        int ret = 0;
        boolean finished = false;

        while (!finished) {
            String line = readString(label);

            try {
                ret = Integer.parseInt(line);
                finished = true;
            } catch (NumberFormatException e) {
                System.err.println("Ungültige Eingabe: Bitte geben Sie eine Zahl an!");
            }
        }

        return ret;
    }

    public static boolean readBool(String label) {
        boolean b = false;
        boolean finished = false;

        while (!finished) {
            String line = readString(label).toLowerCase();

            if (line.equals("ja")) {
                b = true;
                finished = true;
            } else if (line.equals("nein")) {
                b = false;
                finished = true;
            } else {
                System.err.println("Ungültige Eingabe: Bitte geben Sie Ja oder Nein ein!");
            }
        }
        return b;
    }
}
