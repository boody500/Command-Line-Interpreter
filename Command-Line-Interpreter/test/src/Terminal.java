import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.StandardCopyOption;
import java.util.regex.Pattern;
import java.nio.file.*;
import java.util.*;
import java.lang.*;
import java.util.regex.Matcher;


public class Terminal {
    private static Parser p = new Parser();
    private static List<String> commandHistory = new ArrayList<>();

    // ls function //

    public static void ls() {
        File f = new File(pwd());
        File[] list = f.listFiles();
        for (File t : list)
            System.out.println(t.getName());
    }

    // lsr function //
    
    public static void lsr() {
        File f = new File(pwd());
        File[] list = f.listFiles();

        for (int i = list.length - 1; i >= 0; i--) {
            System.out.println(list[i].getName());
        }
    }

    // rmdir function //

    public static void rmdir(char c, File f) {

        if (c == '*') {

            if (f.listFiles().length == 0) {
                f.delete();
                return;
            }

            for (File subfile : f.listFiles()) {

                if (subfile.isDirectory()) {
                    rmdir('*', subfile);
                }

            }
        }

        else {
            if (f.listFiles().length > 0) {
                System.out.println(f.getName() + " is not empty!");

            } else
                f.delete();

            return;
        }

    }

    // mkdir function //

    public static void mkdir(ArrayList<String> args) {

        for (String arg : args) {
            File f = new File(arg);

            if (f.mkdir())
                System.out.println("done");

            else
                System.out.println("invalid directory");
        }
    }

    // pwd function //

    public static String pwd() {
        return String.valueOf(System.getProperty("user.dir"));
    }

    public static String echo(ArrayList<String> args) {
        String Return = "";
        for (int i = 0; i < args.size(); i++) {
            System.out.print(args.get(i));
            Return += args.get(i);

            if (i != args.size() - 1) {

                System.out.print(" ");
                Return += " ";
            }
        }
        System.out.println();
        return Return;
    }

    // rm function //

    public static void rm(ArrayList<String> fileNames) {
        for (String fileName : fileNames) {
            fileName = fileName + ".txt";

            File file = new File(fileName);

            if (file.exists() && file.isFile()) {
                if (file.delete()) {
                    System.out.println("File '" + fileName + "' deleted successfully.");
                } else {
                    System.out.println("Failed to delete the file: " + fileName);
                }
            } else {
                System.out.println("The file does not exist or is not a regular file: " + fileName);
            }
        }
    }

    // touch function //


    public static void touch(ArrayList<String> filePaths) {
        for (String filePath : filePaths) {
            File file = new File(filePath);

            try {
                if (file.createNewFile()) {
                    System.out.println("File '" + filePath + "' created successfully.");
                } else {
                    System.out.println("File '" + filePath + "' already exists.");
                }
            } catch (IOException e) {
                System.out.println("An error occurred while creating the file: " + e.getMessage());
            }
        }
    }


    // cp function //

    public static void cp(String sourceFileName, String destinationFileName) {
        String currentDirectory = System.getProperty("user.dir");
        String sourceFilePath = currentDirectory + File.separator + sourceFileName;
        String destinationFilePath = currentDirectory + File.separator + destinationFileName;

        File sourceFile = new File(sourceFilePath);
        File destinationFile = new File(destinationFilePath);

        if (!sourceFile.exists()) {
            System.out.println("Source file does not exist: " + sourceFileName);
            return;
        }

        try (FileInputStream in = new FileInputStream(sourceFile);
                FileOutputStream out = new FileOutputStream(destinationFile, true)) {
            byte[] buffer = new byte[4096];
            int bytesRead;

            out.write(' ');

            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }

            System.out.println("File '" + sourceFileName + "' appended to '" + destinationFileName + "' successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while copying and appending the file: " + e.getMessage());
        }
    }

    // history function //

    public static void history() {
        int commandNumber = 1;
        for (String command : commandHistory) {
            System.out.println(commandNumber + " " + command);
            commandNumber++;
        }
    }

    // Helper for History function //

    public static void executeCommand(String command) {

        commandHistory.add(command);
    }




    public static void main(String[] args) throws IOException {
        while (true) {

            System.out.print('>');
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if (input.equals("exit"))
                System.exit(0);

            if (p.parse(input)) {
                /*
                 * System.out.println(p.getCmd_name());
                 * for (String ss : p.getCmd_args()) System.out.println(ss);
                 */


                if (p.getCmd_name().equals("mkdir")) {
                    mkdir(p.getCmd_args());
                    executeCommand("mkdir");
                }


                else if (p.getCmd_name().equals("pwd")) {
                    System.out.println(pwd());
                    executeCommand("pwd");
                }


                else if (p.getCmd_name().equals("rmdir")) {
                    if (p.getCmd_args().size() > 1)
                        System.out.println("invalid parameters");


                    else {

                        String temp = p.getCmd_args().get(0);
                        if (temp.equals("*")) {
                            rmdir('*', new File(pwd()));
                            executeCommand("rmdir");

                        }

                        else {
                            rmdir(' ', new File(p.getCmd_args().get(0)));
                            executeCommand("rmdir");

                        }
                    }
                }


                else if (p.getCmd_name().equals("ls")) {
                    ls();
                    executeCommand("ls");
                }


                else if (p.getCmd_name().equals("ls -r")) {
                    lsr();
                    executeCommand("ls -r");
                }


                else if (p.getCmd_name().equals("echo")) {
                    echo(p.getCmd_args());
                    executeCommand("echo");
                }


                else if (p.getCmd_name().equals("rm")) {
                    rm(p.getCmd_args());
                    executeCommand("rm");
                }


                else if (p.getCmd_name().equals("touch")) {
                    touch(p.getCmd_args());
                    executeCommand("touch");
                }


                else if (p.getCmd_name().equals("cp")) {
                    cp(p.getCmd_args().get(0), p.getCmd_args().get(1));
                    executeCommand("cp");
                }


                else if (p.getCmd_name().equals("history")) {
                    history();
                    executeCommand("history");
                }


            } else
                System.out.println("invalid command or parameters");
        }

    }
}
        /*
         * ;
         * 
         * rmdir(f);
         * 
         * File [] l = f.listFiles();
         * String [] s = f.list();
         * for (File ff : l)
         * System.out.println(ff);
         * 
         * System.out.println('\n');
         * 
         * for(String ss : s )
         * System.out.println(ss);
         */