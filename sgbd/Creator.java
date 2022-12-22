package model;
import java.io.*;
public class Creator
{
    public static boolean syntaxeFormat(String[] commands)
    {
        boolean result = false;
        boolean baseSyntaxe = false;
        boolean values = false;
        if(commands[0].compareToIgnoreCase("create")==0 && commands[1].compareToIgnoreCase("table")==0) baseSyntaxe = true;
        if((commands[3].substring(0, 1).compareTo("(")==0) && (commands[commands.length-1].substring(commands[commands.length-1].length()-1, commands[commands.length-1].length()).compareTo(")")==0) ) values = true;
        if(baseSyntaxe && values) result = true;
        return result;
    }
    public static String traitement(String[] instuctions)
    {
        String serverResponse=new String();
        BufferedWriter ecrivain = null;
        File myFile = null;
        boolean success = Creator.syntaxeFormat(instuctions) ;
        try{
            if(success) {
                myFile = new File("file/"+instuctions[2]+".txt");
                if (myFile.createNewFile()) {
                    serverResponse = "table crée";
                }
                else if (myFile.exists()) {
                    serverResponse = "table existante";
                    success = false;
                }
                try
                {
                    ecrivain=new BufferedWriter(new FileWriter(myFile,true));
                    if(success){
                        for(int i=3; i<instuctions.length; i++){
                            if(i==3){
                                instuctions[i]=instuctions[i].substring(1, instuctions[i].length());
                            }
                            if(i==instuctions.length-1){
                                instuctions[i]=instuctions[i].substring(0, instuctions[i].length()-1);
                            }
                            ecrivain.write(instuctions[i]);
                        }
                        ecrivain.newLine();
                    }
                }
                catch(IOException ex){ex.printStackTrace();}
                finally{
                    if(ecrivain != null){
                        ecrivain.close();
                    }   
                }
            }
        }
        catch(IOException ex){ex.getMessage();}
        return serverResponse;
    }
}
