package nl.topicus.all_rise.utility;

public class Print {
    private static boolean debug = true;

    public static void echo (String line) {
        if (debug) {
            System.out.println(line);
        }
    }

    public static void echo (String line, int color) {
        if (debug) {
            System.out.println(color);
            System.out.println(line);
            System.out.println(Color.RESET);
        }
    }

    public static void echo (int line) {
        if (debug) {
            System.out.println(line);
        }
    }

    public static void echo (int line, int color) {
        if (debug) {
            System.out.println(color);
            System.out.println(line);
            System.out.println(Color.RESET);
        }
    }

    public static void echo (double line) {
        if (debug) {
            System.out.println(line);
        }
    }

    public static void echo (double line, int color) {
        if (debug) {
            System.out.println(color);
            System.out.println(line);
            System.out.println(Color.RESET);
        }
    }
}
