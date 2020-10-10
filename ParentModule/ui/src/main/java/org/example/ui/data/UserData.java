package org.example.ui.data;

import org.example.ui.exception.UserDataException;
import java.util.Scanner;

public final class UserData {

    private final static Scanner sc = new Scanner(System.in);

    public static String getString(String message) {
        System.out.println(message);
        return sc.nextLine();
    }

    public static int getInt(String message) {
        System.out.println(message);
        var line = sc.nextLine();
        if (!line.matches("\\d+")) {
            throw new UserDataException("not an integer value");
        }
        return Integer.parseInt(line);
    }

    public static boolean getBoolean(String message) {
        System.out.println(message + " Press 'y' to say yes");
        var line = sc.nextLine();
        return line.equalsIgnoreCase("y");
    }

    public static void close() {
        if (sc != null) {
            sc.close();
        }
    }
}
