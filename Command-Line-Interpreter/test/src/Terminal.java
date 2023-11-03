import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.StandardCopyOption;
import java.util.regex.Pattern;
import java.nio.file.*;
import java.util.*;
import java.lang.*;
import java.util.regex.Matcher;
import java.util.Scanner;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Terminal {

    private static Parser p = new Parser();
    private static String currentDirectory = System.getProperty("user.dir");
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

    public static void mkdir(ArrayList<String> args){

        for (int i = 0; i < args.size(); i++) {

            File f = new File(args.get(i));

            if (f.mkdir())
                System.out.println("directory " + f.getName() + " has been created successfully");

            else
                System.out.println("invalid directory");
        }
    }

    // pwd function //

    public static String pwd() {
        return currentDirectory;
    }

    // cd function //
    public static void cd(String directory) {
        File f2 = new File(directory);

        /*cd ~ = cd ==> home*/
        if (directory == "")
        {
            currentDirectory = System.getProperty("user.home");
            System.out.println(currentDirectory);
            System.setProperty("user.dir" , currentDirectory);
        }

        /*cd ..  ==> parent*/

        else if (directory.equals(".."))
        {
            Path p = Paths.get(currentDirectory);
            currentDirectory  = (p.getParent()).toString();
            System.out.println(currentDirectory);
            System.setProperty("user.dir" , currentDirectory);
        }

        // cd C:\Users\Farah\IdeaProjects\cmd>
        // cd ////////////////

        else if (f2.exists())
        {
            currentDirectory = (f2.getAbsolutePath()).toString();
            System.out.println(currentDirectory);
            System.setProperty("user.dir" , currentDirectory);

        }
        else
        {
            System.out.println("Directory not found: " + directory);
        }
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

        if (fileNames.size() == 1){

            for (String fileName : fileNames) {

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
        else{
                System.out.println("Invalid parameters.");
        }
    }

    // touch function //
    public static void touch(ArrayList<String> filePaths) {
        if(filePaths.size() == 1){
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
        else
        {
            System.out.println("Invalid parameters.");
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

    // cpr function
    public static void cpR(String source, String destination) {
        File sourceDir = new File(source);
        File destinationDir = new File(destination);
        // Check if the source directory exists
        if (sourceDir.exists() && sourceDir.isDirectory()) {
            // Create the destination directory if it doesn't exist
            if (!destinationDir.exists()) {
                destinationDir.mkdirs();
            }
            // Get a list of files in the source directory
            File[] files = sourceDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        // Copy individual files
                        cp(file.getAbsolutePath(), destinationDir.getAbsolutePath() + File.separator + file.getName());
                    } else if (file.isDirectory()) {
                        // Recursively copy subdirectories
                        cpR(file.getAbsolutePath(), destinationDir.getAbsolutePath() + File.separator + file.getName());
                    }
                }
                System.out.println(source + " copied to " + destination);
            }
        } else {
            System.out.println("Source directory " + source + " does not exist or is not a directory");
        }
    }


    // cat function
    public static void cat(String fileName) {
        try{
            Scanner scanner = new Scanner(new File(fileName));
            while (scanner.hasNextLine())
            {
                System.out.println(scanner.nextLine());
            }
        } catch (IOException e) {
            System.out.println("Error reading " + fileName);
        }

    }

    // word count function
    public static void wc(String fileName) {
        int wordCount = 0;
        int chars = 0;
        int lines = 0;
        File f = new File(fileName);
        try {
            Scanner scanner = new Scanner(f);
            while (scanner.hasNext() && scanner.hasNextLine())
            {
                String S = scanner.next();
                wordCount++;
                chars += S.length()+1;

            }
            chars--;
            Scanner scan = new Scanner(f);
            while(scan.hasNextLine()) {
                scan.nextLine();
                lines++;
            }
        } catch (IOException e) {
            System.out.println("Error reading " + fileName);
        }

        System.out.println(lines + " lines," +wordCount + " words," + chars + " chars in " + f.getName());

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



    public static boolean isPath(String str) {
        try {
            // Attempt to create a Path object from the string
            Path path = Paths.get(str);

            // If the above line doesn't throw an exception, the string is a valid path
            return true;
        } catch (Exception e) {
            // If an exception is thrown, the string is not a valid path
            return false;
        }
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
                    if(p.getCmd_args().size() == 2)
                    {
                        cp(p.getCmd_args().get(0), p.getCmd_args().get(1));
                        executeCommand("cp");
                    }
                    else {
                        System.out.println("Invalid parameters for cp");

                    }
                }

                else if (p.getCmd_name().equals("cp -r")) {
                    if (p.getCmd_args().size() == 2) {
                        cpR(p.getCmd_args().get(0), p.getCmd_args().get(1));
                    } else {
                        System.out.println("Invalid parameters for cp -r");
                    }
                }

                else if (p.getCmd_name().equals("cd")) {
                    if (p.getCmd_args().size() <= 1) {
                        if (p.getCmd_args().size() == 0)
                        {
                            cd("");
                        }
                        else
                            cd(p.getCmd_args().get(0));
                    }
                    else {
                        System.out.println("Invalid parameters for cd");
                    }
                }

                else if (p.getCmd_name().equals("history")) {
                    history();
                    executeCommand("history");
                }

                else if (p.getCmd_name().equals("cat"))
                {
                    if (p.getCmd_args().size() == 1)
                    {
                        String fileName = p.getCmd_args().get(0);
                        cat(fileName);
                    }
                    if (p.getCmd_args().size() == 2) {
                        String fileName1 = p.getCmd_args().get(0);
                        String fileName2 = p.getCmd_args().get(1);
                        cat(fileName1);
                        cat(fileName2);
                    }
                    else {
                        System.out.println("Invalid parameters for cat");
                    }
                }

                else if (p.getCmd_name().equals("wc"))
                {
                    if (p.getCmd_args().size() == 1)
                    {
                        String fileName = p.getCmd_args().get(0);
                        wc(fileName);
                    }
                    else
                    {
                        System.out.println("Invalid parameters for wc");
                    }
                }
            } else
                System.out.println("invalid command or parameters");
        }

    }
}
