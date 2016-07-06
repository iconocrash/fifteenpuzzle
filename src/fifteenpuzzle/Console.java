/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fifteenpuzzle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author JJ
 */
public class Console {

    public static void waitForEnter() {
        System.out.println("Press enter to continue...");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            br.readLine();
        } catch (IOException ioe) {
            System.out.println(ioe + "\nExiting.");
            System.exit(1);
        }
    }

    public static String getConsoleInput() {
        String s = null;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            s = br.readLine();
        } catch (IOException ioe) {
            System.out.println(ioe + "\nExiting.");
            System.exit(1);
        }
        return s;
    }

    public static int getIntConsoleInput()
    {
        int value = -1;
        while (true) {
            try {
              value = Integer.parseInt( getConsoleInput() );
            } catch (NumberFormatException nfe) {
                System.out.println("Invalid number format entered. Try again.");
                System.out.println("Please enter a valid integer and press enter: ");
                continue;
            }
            break;
        }
        return value;
    }

    /* min, max inclusive */
    public static int getIntConsoleInputWithBounds(int min, int max)
    {
        int value = -1;

        while (true) {
            value = getIntConsoleInput();
            if ( value < min || value > max ) {
                System.out.println("Input number out of range!\nSelect a number between " + min + " and " + max + ": ");
                continue;
            }
            break;
        }

        return value;
    }

}
