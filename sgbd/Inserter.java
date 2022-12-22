package model;

import java.io.*;

import controller.Server;
public class Inserter
{
    public static boolean syntaxeFormat(String[] commands, String[][] data)
    {
        boolean result = false;
        boolean baseSyntaxe = false;
        boolean values = false;
        boolean col = false;
        if(commands[0].compareToIgnoreCase("insert")==0 && commands[1].compareToIgnoreCase("into")==0 && commands[3].compareToIgnoreCase("values")==0)
        {
            baseSyntaxe = true;
        }
        if((commands[4].substring(0, 1).compareTo("(")==0) && (commands[commands.length-1].substring(commands[commands.length-1].length()-1, commands[commands.length-1].length()).compareTo(")")==0) ) {
            values = true;
            String cols = commands[4];
            cols = cols.substring(1, cols.length()-1);
            String[] colonnes = cols.split(",");
            if(colonnes.length == data[0].length) {
                col = true;
            }
        }
        if(baseSyntaxe && values && col) result = true;
        return result;
    }
    public static String traitement(Server server, String[] instructions)
    {
        String serverResponse=new String();
        boolean isDataGet = false;
        String[][] data = null;
        int d1 = 0;
        int d2 = 0;
        if(new File("file/"+instructions[2]+".txt").exists()) {
            String tableName = instructions[2];
            String S=new String();
            BufferedReader BW = null;
            try {
                BW = new BufferedReader(new FileReader(server.getFile(tableName)));
                while((S=BW.readLine()) != null)
                {
                    String[] line=S.split(",");
                    d2 = line.length;
                    d1++;
                }
            } catch (IOException e1) {{System.out.println(e1.getMessage());}}
            data = new String[d1][d2];
            try {
                BW = new BufferedReader(new FileReader(server.getFile(tableName)));
            }
            catch (FileNotFoundException e) {System.out.println(e.getMessage());}
            int i=0;
            try {
                while((S=BW.readLine()) != null)
                {
                    String[] line=S.split(",");
                    for(int j=0;j<line.length;j++)
                    {
                        data[i][j] = line[j];
                    }
                    i++;
                }
            } catch (IOException e) {System.out.println(e.getMessage());}
            isDataGet = true;
        }
        if(isDataGet) {
            try {
                BufferedWriter ecrivain=null;
                boolean success = Inserter.syntaxeFormat(instructions, data);
                try
                {
                    if(new File("file/"+instructions[2]+".txt").exists())
                    {
                        ecrivain=new BufferedWriter(new FileWriter(server.getFile(instructions[2]),true));
                        if(success){
                            for(int i=4; i<instructions.length; i++){
                                if(i==4){
                                    instructions[i]=instructions[i].substring(1, instructions[i].length());
                                }
                                if(i==instructions.length-1){
                                    instructions[i]=instructions[i].substring(0, instructions[i].length()-1);
                                }
                                ecrivain.write(instructions[i]);
                            }
                            ecrivain.newLine();
                        }
                    }
                }
                catch(IOException ex){ex.printStackTrace();}
                finally{
                    if(ecrivain != null){
                        ecrivain.close();
                    }
                    if(!new File("file/"+instructions[2]+".txt").exists())
                    {
                        serverResponse = "table inexistante";
                    }
                    else if(success){
                        serverResponse = "1 ligne inserée";
                    }
                    else{
                        serverResponse = "erreur de syntaxe";
                    }
                }
            } catch (IOException e) {e.getMessage();}
        }
        return serverResponse;
    }
}
