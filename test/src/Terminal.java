import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Terminal {
    private static Parser p = new Parser();

//gfd
    public static void ls(){
        File f = new File(pwd());
        File [] list = f.listFiles();
        for (File t: list) System.out.println(t.getName());
    }

    public static void lsr(){
        File f = new File(pwd());
        File [] list = f.listFiles();

        for (int i = list.length-1; i >= 0 ; i--) {
            System.out.println(list[i].getName());
        }
    }
    public static void rmdir(char c,File f)
    {

        if (c == '*'){

            if (f.listFiles().length == 0)
            {
                f.delete();
                return;
            }

            for (File subfile : f.listFiles()) {

                if (subfile.isDirectory()) {
                    rmdir('*',subfile);
                }

            }
        }

        else{
            if (f.listFiles().length > 0){
                System.out.println(f.getName()+" is not empty!");

            }
            else
                f.delete();

            return;
        }

    }

    public static void mkdir(ArrayList<String> args){

        for (String arg : args){
            File f = new File(arg);

            if (f.mkdir())
                System.out.println("done");

            else
                System.out.println("invalid directory");
        }
    }


    public static String pwd(){
        return String.valueOf(System.getProperty("user.dir"));
    }

    public static String echo(ArrayList<String> args){
        String Return = "";
        for (int i = 0; i < args.size(); i++) {
            System.out.print(args.get(i));
            Return += args.get(i);

            if (i != args.size()-1){

                System.out.print(" ");
                Return += " ";
            }
        }
        System.out.println();
        return Return;
    }

    public static void main(String[] args) throws IOException{
        while (true){

            System.out.print('>');
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if (input.equals("exit"))
                System.exit(0);

            if(p.parse(input)){
                /*System.out.println(p.getCmd_name());
                for (String ss : p.getCmd_args()) System.out.println(ss);*/

                if (p.getCmd_name().equals("mkdir"))
                    mkdir(p.getCmd_args());

                else if (p.getCmd_name().equals("pwd"))
                    System.out.println(pwd());

                else if (p.getCmd_name().equals("rmdir")){
                    if (p.getCmd_args().size() > 1)
                        System.out.println("invalid parameters");

                    else{

                        String temp = p.getCmd_args().get(0);
                        if (temp.equals("*"))
                            rmdir('*',new File(pwd()));

                        else
                            rmdir(' ',new File(p.getCmd_args().get(0)));
                    }
                }

                else if (p.getCmd_name().equals("ls"))
                    ls();

                else if(p.getCmd_name().equals("ls -r"))
                    lsr();

                else if (p.getCmd_name().equals("echo"))
                    echo(p.getCmd_args());
            }
            else
                System.out.println("invalid command or parameters");
        }
        /*;

        rmdir(f);

        File [] l = f.listFiles();
        String [] s = f.list();
        for (File ff : l)
            System.out.println(ff);

        System.out.println('\n');

        for(String ss : s )
            System.out.println(ss);*/


    }
}