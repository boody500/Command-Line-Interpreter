import java.util.ArrayList;
import java.util.List;

public class Parser {
    private String cmd_name;
    private ArrayList<String> cmd_args = new ArrayList<String>();

    public boolean parse(String input){
        cmd_args.clear();
        int temp = input.indexOf(' ');

        if (temp != -1 && temp != input.length()-1){

            cmd_name = input.substring(0,temp);
            int check = 0;
            if (input.charAt(temp+1) == '-' && input.charAt(temp+2) == 'r'){

                cmd_name += " -r";
                check = 2;
            }


            if (cmd_name.equals("mkdir") || cmd_name.equals("rmdir") || cmd_name.equals("cd") || cmd_name.equals("echo") || cmd_name.equals("touch") ||
                    cmd_name.equals("ls -r") || cmd_name.equals("rm") || cmd_name.equals("cat")  || cmd_name.equals("cp") || cmd_name.equals("cp -r"))
            {



                String arg = "";
                for (int i = temp+1+check; i < input.length(); i++) {
                    if (input.charAt(i) == ' '){

                        cmd_args.add(arg);
                        arg = "";
                    }
                    else
                        arg += input.charAt(i);

                }
                if(!arg.isEmpty())
                    cmd_args.add(arg);

                return true;
            }

            return false;
        }

        else if (temp == -1){
            cmd_name = input;

            if (cmd_name.equals("pwd") || cmd_name.equals("ls") || cmd_name.equals("cd") || cmd_name.equals("history"))return true;

            return false;
        }

        else
            return false;

    }

    public String getCmd_name(){
        return cmd_name;
    }

    public ArrayList<String> getCmd_args(){
        return cmd_args;
    }
}
