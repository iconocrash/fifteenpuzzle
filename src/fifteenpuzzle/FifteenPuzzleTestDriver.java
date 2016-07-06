/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fifteenpuzzle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author JJ
 */
public class FifteenPuzzleTestDriver {

    public static final String nameBFS = "Breadth-First Search";
    public static final String nameIDLS = "Iterative Depth-Limited Search";
    public static final String nameAStarH1 = "A* (h1)";
    public static final String nameAStarH2 = "A* (h2)";

    static final int MENU_CREATE = 1;
    static final int MENU_LOAD = 2;
    static final int MENU_DELETE = 3;
    static final int MENU_EDIT = 4;
    static final int MENU_SOLVE = 5;
    static final int MENU_STATS = 6;
    static final int MENU_VERBOSE = 7;
    static final int MENU_SEED = 8;
    static final int MENU_SETGOAL = 9;
    static final int MENU_EXIT = 10;

    static String dataDir = "data/";
    //static FifteenPuzzleGame loaded = FifteenPuzzleGenerator.generateRandomPuzzle();
    //static String loadedName = "Random default";
    static FifteenPuzzleGame loaded = FifteenPuzzleGame.goal.clone();
    static String loadedName = "Default";
    
    public static void optionMenu()
    {
        while (true)
        {
            System.out.println("*********************");
            System.out.println("15-Puzzle Solver\nCS 411 - Assignment 3\nby James Jilek");
            System.out.println("*********************");
            System.out.println();
            System.out.println("Loaded puzzle: [" + loadedName + "]");
            System.out.println(loaded);
            System.out.println();
            System.out.println("(1) Create a board");
            System.out.println("(2) Load a board");
            System.out.println("(3) Delete a board");
            System.out.println();
            System.out.println("(4) Edit loaded board");
            System.out.println("(5) Solve loaded board");
            System.out.println("(6) Perform search statistics");
            System.out.println();
            System.out.println("(7) Toggle verbose searching" + (FifteenPuzzleSearch.verboseSearch ? " (currently ON)" : " (currently OFF)"));
            System.out.println("(8) Set random seed");
            System.out.println("(9) Set goal board");
            System.out.println();
            System.out.println("(10) Exit");
            System.out.println();

            System.out.println("Enter selection: ");
            int option = Console.getIntConsoleInput();

            System.out.println();

            switch (option)
            {
                case MENU_LOAD:
                    loadedName = selectFile();
                    loaded = loadFile(loadedName);
                    System.out.println();
                    System.out.println("Loaded " + loadedName + ": ");
                    System.out.println( loaded );
                    System.out.println();
                    Console.waitForEnter();
                    break;
                    
                case MENU_CREATE:
                    System.out.println("Enter a name for the new file (must be a valid filename): ");
                    String newFileName = Console.getConsoleInput();
                    System.out.println();
                    final int CREATE_INPUT = 1;
                    final int CREATE_FROMCOPY = 2;
                    //from goal
                    //random
                    //generate depth

                    System.out.println("(1) Input board");
                    System.out.println("(2) Copy existing file");
                    System.out.println();
                    System.out.println("Select option: ");
                    int create_option = Console.getIntConsoleInputWithBounds(1,2);
                    System.out.println();

                    FifteenPuzzleGame newGame = null;
                    switch (create_option)
                    {
                        case CREATE_INPUT:
                            newGame = FifteenPuzzleGenerator.generatePuzzleUserInput();
                            break;
                        case CREATE_FROMCOPY:
                            System.out.println("Select file to copy from.");
                            //TODO: handle if there are no files yet
                            FifteenPuzzleGame gameToCopy = loadFile( selectFile() );
                            newGame = gameToCopy.clone();
                            break;
                    }

                    saveFile(newGame, newFileName);
                    
                    System.out.println("Created board named " + newFileName + ":");
                    System.out.println(newGame);
                    System.out.println();

                    //TODO: load the created game here

                    break;

                case MENU_DELETE:
                    System.out.print("Select file number to delete.");
                    deleteFile( selectFile() );
                    break;

                case MENU_EDIT:
                    System.out.println("Not yet implemented");
                    System.out.print("Don't really need it");
                    System.out.println();
                    break;

                case MENU_SOLVE:
                    final int SOLVE_BFS = 1;
                    final int SOLVE_IDLS = 2;
                    final int SOLVE_ASTAR_H1 = 3;
                    final int SOLVE_ASTAR_H2 = 4;
                    FifteenPuzzleSearch.SearchMethod searchMethod = null;
                    System.out.println("(" + SOLVE_BFS + ") " + "Solve with " + nameBFS );
                    System.out.println("(" + SOLVE_IDLS + ") " + "Solve with " + nameIDLS );
                    System.out.println("(" + SOLVE_ASTAR_H1 + ") " + "Solve with " + nameAStarH1 );
                    System.out.println("(" + SOLVE_ASTAR_H2 + ") " + "Solve with " + nameAStarH2 );
                    System.out.println();
                    System.out.println("Select solution search method: ");
                    int solve_option = Console.getIntConsoleInputWithBounds(1,4);
                    System.out.println();
                    switch (solve_option)
                    {
                        case SOLVE_BFS: searchMethod = FifteenPuzzleSearch.SearchMethod.BFS; break;
                        case SOLVE_IDLS: searchMethod = FifteenPuzzleSearch.SearchMethod.IDLS; break;
                        case SOLVE_ASTAR_H1: searchMethod = FifteenPuzzleSearch.SearchMethod.ASTAR_H1; break;
                        case SOLVE_ASTAR_H2: searchMethod = FifteenPuzzleSearch.SearchMethod.ASTAR_H2; break;
                        default:
                            System.out.println("Invalid solution selection input. Something is wrong as the input should be bounded.\nExiting");
                            System.exit(1);
                    }
                    SearchSolution solution = null;
                    boolean solutionSuccess = false;
                    try {
                        solution = FifteenPuzzleSearch.findSolution(searchMethod, loaded);
                        solutionSuccess = true;
                    } catch (OutOfMemoryError oome) {
                        System.out.println("Ran out of memory trying to find solution!");
                        solutionSuccess = false;
                    }
                    if (solutionSuccess == false) {
                        // do nothing
                    } else if (solution == null) {
                        System.out.print("No solution.");
                    } else {
                        System.out.println("Initial board state: ");
                        System.out.println(loaded);
                        System.out.println();
                        MoveSequence movesequence = solution.solutionSequence;
                        System.out.println("Solution move sequence: ");
                        System.out.println(movesequence);
                        System.out.println();
                        FifteenPuzzleGame solvedGame = loaded.clone();
                        solvedGame.performMoveSequence(movesequence);
                        System.out.println("Solved board state:");
                        System.out.println(solvedGame); 
                    }
                    System.out.println();
                    System.out.println("Solution stats: ");
                    System.out.println(solution.toString());
                    System.out.println();
                    Console.waitForEnter();
                    System.out.println();

                    break;

                case MENU_STATS:
                    System.out.println("Performing statistics test...");
                    System.out.println();
                    trialStatTest();
                    System.out.println();
                    Console.waitForEnter();
                    System.out.println();
                    break;

                case MENU_VERBOSE:
                    if (FifteenPuzzleSearch.verboseSearch == true)
                        FifteenPuzzleSearch.verboseSearch = false;
                    if (FifteenPuzzleSearch.verboseSearch == false)
                        FifteenPuzzleSearch.verboseSearch = true;
                    break;

                case MENU_SEED:
                    System.out.println("Enter random seed: ");
                    FifteenPuzzleGenerator.setSeed(
                            Console.getIntConsoleInput() );
                    break;

                case MENU_SETGOAL:
                    System.out.println("Not yet implemented.");
                    break;

                case MENU_EXIT:
                    System.out.println("Exiting.");
                    System.exit(0);
                    break;
                    
                default:
                    System.out.println("Invalid menu option. Try again.");
                    System.out.println();
                    break;
            }
        }

    }

    static FifteenPuzzleGame loadFile(String name)
    {
        try {
            FileInputStream fis = new FileInputStream(dataDir + name);
            ObjectInputStream ois = new ObjectInputStream(fis);
            return (FifteenPuzzleGame) ois.readObject();
        } catch (FileNotFoundException fnfe) {
            System.out.println(fnfe + "\nExiting.");
            System.exit(1);
        } catch (IOException ioe) {
            System.out.println(ioe + "\nExiting");
            System.exit(1);
        } catch (ClassNotFoundException onfe) {
            System.out.println(onfe + "\nExiting.");
            System.exit(1);
        }
        return null;
    }

    /* Returns true on success, false otherwise */
    static boolean saveFile(FifteenPuzzleGame game, String fileName)
    {
        boolean success = true;

        File dir = new File( dataDir );
        File f = new File( dataDir + fileName );

        if ( f.exists() )
            deleteFile(fileName);

        if ( dir.exists() == false )
            dir.mkdir();

        /* create the file */
        try {
            f.createNewFile();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Exiting.");
            System.exit(1);
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(dataDir + fileName);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
            System.out.println("Exiting.");
            System.exit(1);
        }

        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(fos);
            oos.writeObject(game); // serializa the object

            // done
            oos.close();
            fos.close();
        } catch (IOException ioe) {
            System.out.println(ioe + "\nExiting.");
            System.exit(1);
        }

        return success;
    }

    static String selectFile()
    {
        File dir = new File( dataDir );

        /* the directory doesn't exist create it */
        if ( !dir.exists() ) {
            dir.mkdir();
        }

        if ( !dir.isDirectory() ) {
            System.out.println("Data \"directory\" is not a directory! Cannot continue.\nExiting.");
            System.exit(1);
        }

        String[] names = dir.list();

        int selection = 0;
        for ( ; selection < names.length; selection++)
        {
            System.out.println("(" + selection + ") " + names[selection]);
        }

        System.out.println();
        System.out.println("Enter number to select file: ");
        selection = Console.getIntConsoleInput();

        return names[selection];
    }

    static void deleteFile(String fileName) {
        File f = new File(fileName);
        if ( f.exists() == false ) {
            System.out.println("File does not exist!");
            System.exit(1);
        }
        if ( f.canWrite() == false ) {
            System.out.println("Cannot delete file due to acess permissions.");
            System.exit(1);
        }

        if ( f.delete() == false) {
            System.out.println("Failed to delete file for some reason!");
            System.exit(1);
        }
        
    }

//   static final int MAX_TEST_DEPTH = 35;
//   public static void depthStatTest()
//   {
//       //
//   }

   static final int MAX_TEST_TRIALS = 35;
   public static void trialStatTest()
   {
       SearchSolution[] idlsStats = new SearchSolution[MAX_TEST_TRIALS+1];
       SearchSolution[] aStarH1Stats = new SearchSolution[MAX_TEST_TRIALS+1];
       SearchSolution[] aStarH2Stats = new SearchSolution[MAX_TEST_TRIALS+1];

       // Hasn't ran out of memory at a previous solution depth?
       boolean idlsOK = true, aStarH1OK = true, aStarH2OK = true;

       FifteenPuzzleGame game = FifteenPuzzleGame.goal.clone(); // start with the goal, has a solution depth of 0
       Move prevMove = null;

       System.out.println("Search Algorithms Stats\nCS 411 - Assignment 3\nJames Jilek");

       for (int trial = 0;
            trial <= MAX_TEST_TRIALS;
            trial++)
       {

           System.out.println("Search Stats: Doing test trial " + trial + "." );
           System.out.println("IDLS OK? " + idlsOK + ", A*h1 OK? " + aStarH1OK + ", A*h2 OK? " + aStarH2OK);

           /* Perform the trials for the current puzzle and corresponding
              solution depth for each soluton type. */

           idlsOK = statTestPerformTrial(nameIDLS, FifteenPuzzleSearch.SearchMethod.IDLS, game,
                   idlsStats, trial, idlsOK);       /* Interative DLS */
           aStarH1OK = statTestPerformTrial(nameAStarH1, FifteenPuzzleSearch.SearchMethod.ASTAR_H1, game,
                   aStarH1Stats, trial, aStarH1OK); /* A* (heuristic 1) */
           aStarH2OK = statTestPerformTrial(nameAStarH2, FifteenPuzzleSearch.SearchMethod.ASTAR_H2, game,
                   aStarH2Stats, trial, aStarH2OK); /* A* (heuristic 2) */

           System.out.println("\nGenerating next puzzle...");
           System.out.println("Previous move " + prevMove);
           prevMove = FifteenPuzzleGenerator.generateNext(trial + 1, game, prevMove);
           if ( prevMove == null )
           {
               System.out.println("Could not make next move.");
               break;
           }
           System.out.println("Did move " + prevMove);
           System.out.println("Next puzzle:");
           System.out.println(game);

           System.out.println();
       }

       System.out.println("\nTrials complete. Resulting data...\n");

       /* Now print the data */
       System.out.print("Trial   ");
       printCol("[depth/nodes/time] (IDLS)");
       printCol("[depth/nodes/time] (A*h1)");
       printCol("[depth/nodes/time] (A*h2)");
       System.out.println();
       for (int trial = 0;
            trial <= MAX_TEST_TRIALS;
            trial++)
       {
           if (trial < 10) System.out.print(trial + "   ");
           else System.out.print(trial + "  ");
           statTestPrintTrial(idlsStats, trial);
           statTestPrintTrial(aStarH1Stats, trial);
           statTestPrintTrial(aStarH2Stats, trial);
           System.out.println();

       }
   }

   static boolean statTestPerformTrial(String searchName, FifteenPuzzleSearch.SearchMethod searchType,
           FifteenPuzzleGame game, SearchSolution[] statTable, int trial, boolean prevSuccess)
   {
        if (prevSuccess == false) {
            System.out.println("Skipping " + searchName + " due to previous memory max out with this search.");
            return false;
        }

        boolean success = true;
        System.out.println("Testing " + searchName + " (trial " + trial + ").");
        try {
            statTable[trial] = FifteenPuzzleSearch.findSolution(searchType, game);
        } catch (OutOfMemoryError oome) {
            System.out.println(searchName + " ran out of memory! " + searchName + " will be skipped in further trials.");
            statTable[trial] = null;
            success = false;
        }
        return success;
    }

//   static void statTestPrintTrial(SearchSolution[] statTable, int trial) {
//       if (statTable[trial] == null)
//           System.out.print("-");
//       else
//           System.out.print( statTable[trial].toStringTiny() );
//       System.out.print("\t\t");
//   }

   static final int COL_WIDTH = 26; // try to make each column have this width

   static void printCol(String s) {
       int addSpaces = COL_WIDTH - s.length();

       System.out.print(s);
       
       if (addSpaces > 0) {
           for (int i = 0; i < addSpaces; i++) {
               System.out.print(" ");
           }
       }
   }

   static void statTestPrintTrial(SearchSolution[] statTable, int trial)
   {
       String s = null;
       if (statTable[trial] == null)
           s =  "-";
       else
           s = statTable[trial].toStringTiny();
       printCol(s);
   }
}
