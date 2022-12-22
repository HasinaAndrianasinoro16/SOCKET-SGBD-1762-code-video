package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import controller.Server;
public class Updater
{
    public static boolean syntaxeFormat(String[] commands)
    {
        boolean baseSyntaxe = false;
        if(commands[0].compareToIgnoreCase("update")==0 && commands[2].compareToIgnoreCase("set")==0) baseSyntaxe = true;
        return baseSyntaxe;
    }
    public static String traitement(Server server, String[] instructions)
    {
        String serverResponse = new String();
        String tableName = new String();
        boolean condition = false;
        if(instructions[instructions.length-2].compareToIgnoreCase("where")==0) {
            condition = true;
        }
        if(isExist(instructions)) {
            tableName = instructions[1];
            if(!condition)
            {
                serverResponse = conditionnel(server, instructions, serverResponse, tableName, false);
            }
            else{
                serverResponse = conditionnel(server, instructions, serverResponse, tableName, true);
            }
        }
        else {
            serverResponse = "table inexistante";
        }
        return serverResponse;
    }
    public static boolean isExist(String[] instructions)
    {
        boolean isExist = false;
        if (new File("file/"+instructions[1]+".txt").exists()) {
            isExist = true;
        }
        return isExist;
    }
    public static String conditionnel (Server server, String[] instructions, String serverResponse, String tableName, boolean condition)
    {
        String[][] data = null;
        int d1 = 0;
        int d2 = 0;
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

            String[] columnsSet=null;
            int linesUpdated=0;
            int colSet = -1;
            int col=-1;
            String[] columnsCondition=null;
            if(!condition) {
                columnsSet = instructions[instructions.length-1].split("=");
                for(int k=0; k<data[0].length; k++) {
                    if (columnsSet[0].compareToIgnoreCase(data[0][k])==0) {
                        colSet = k;
                    }
                }
            }
            else {
                columnsSet = instructions[instructions.length-3].split("=");
                columnsCondition = instructions[instructions.length-1].split("=");
                for(int k=0; k<data[0].length; k++) {
                    if (columnsSet[0].compareToIgnoreCase(data[0][k])==0) {
                        colSet = k;
                    }
                }
                for(int k=0; k<data[0].length; k++) {
                    if (columnsCondition[0].compareToIgnoreCase(data[0][k])==0) {
                        col = k;
                    }
                }
            }
            int[] lines = null;
            if(!condition) col = 0;
            if(colSet!=-1 && col!=-1) {
                if(condition) {
                    ArrayList<Integer> linesNumbers = new ArrayList<Integer>();
                    for(int q=1; q<data.length;q++) {
                        if(data[q][col].contains(columnsCondition[1])) {
                            linesNumbers.add(q);
                        }
                    }
                    lines = new int[linesNumbers.size()];
                    linesUpdated=linesNumbers.size();
                    for(int l=0; l<lines.length; l++) {
                        lines[l] = linesNumbers.get(l);
                    }
                    for(int q=1; q<data.length;q++) {
                        for(int l=0; l<lines.length; l++) {
                            if(q==lines[l]) {
                                data[q][colSet] = columnsSet[1];
                            }
                        }
                    }
                }
                else{
                    for(int q=1; q<data.length;q++) {
                        data[q][colSet] = columnsSet[1];
                    }
                    linesUpdated=data.length-1;
                }
                try {
                    BufferedWriter ecrivain=null;
                    try
                    {
                        ecrivain=new BufferedWriter(new FileWriter(server.getFile(tableName)));
                        ecrivain.write(new String());
                        ecrivain=new BufferedWriter(new FileWriter(server.getFile(tableName),true));
                        for(int e=0; e<data.length; e++){
                            String line = new String();
                            for(int k=0; k<data[e].length; k++) {
                                if(k==data[e].length-1){
                                    line += data[e][k];
                                }
                                else{
                                    line += data[e][k]+",";
                                }
                            }
                            ecrivain.write(line);
                            ecrivain.newLine();
                        }
                    }
                    catch(IOException ex){ex.printStackTrace();}
                    finally{
                        if(ecrivain != null){
                            ecrivain.close();
                        }
                        serverResponse = linesUpdated + "e ligne(s) modifiée(s)"; 
                    }
                } catch (IOException e) {e.getMessage();}
            }
            else {
                serverResponse = "colonne introuvable";
            }
        return serverResponse;
    }
}
