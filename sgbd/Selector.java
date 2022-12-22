package model;

import java.io.*;
import java.util.ArrayList;

import controller.Server;

public class Selector {
    public static boolean columnNature(String column) {
        boolean result = false;
        if (column.compareTo("*") == 0) {
            result = false;
        } else {
            result = true;
        }
        return result;
    }

    public static String traitement(Server server, String[] instructions) {
        String serverResponse = new String();
        String tableName = new String();
        String[] conditions = instructions[instructions.length - 1].split("=");
        boolean condition = false;
        if (conditions.length == 2) {
            condition = true;
            tableName = instructions[instructions.length - 3];
        } else {
            tableName = instructions[instructions.length - 1];
        }
        BufferedReader BW = null;
        try {
            BW = new BufferedReader(new FileReader(server.getFile(tableName)));
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            condition = true;
        }
        if (!condition) {
            String[][] data = null;
            int d1 = 0;
            int d2 = 0;
            serverResponse = conditionnel(server, instructions, serverResponse, BW, data, d1, d2, false, new String());
        } else {
            boolean realcondition = true;
            try {
                BW = new BufferedReader(new FileReader(server.getFile(instructions[instructions.length - 3])));
                realcondition = true;
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
                realcondition = false;
            }
            if (realcondition) {
                String[][] data = null;
                int d1 = 0;
                int d2 = 0;
                serverResponse = conditionnel(server, instructions, serverResponse, BW, data, d1, d2, true,
                        instructions[instructions.length - 1]);
            }
        }
        return serverResponse;
    }

    public static int[] filterColumns(String[][] data, String columns) {
        String[] columnsName = columns.split(",");
        ArrayList<Integer> columnNumbers = new ArrayList<Integer>();
        for (int i = 0; i < columnsName.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                if (columnsName[i].compareToIgnoreCase(data[0][j]) == 0) {
                    columnNumbers.add(j);
                }
            }
        }
        int[] col = new int[columnNumbers.size()];
        for (int i = 0; i < col.length; i++) {
            col[i] = columnNumbers.get(i);
        }
        return col;
    }

    public static String conditionnel(Server server, String[] instructions, String serverResponse, BufferedReader BW,
            String[][] data, int d1, int d2, boolean condition, String cond) {
        String tableName = new String();
        int[] lines = null;
        String[] colOfCondition = null;
        int numberColOfCondition = 0;
        if (condition) {
            tableName = instructions[instructions.length - 3];
            colOfCondition = instructions[instructions.length - 1].split("=");
        } else {
            tableName = instructions[instructions.length - 1];
        }
        try {
            String S = new String();
            while ((S = BW.readLine()) != null) {
                String[] line = S.split(",");
                d2 = line.length;
                d1++;
            }
            data = new String[d1][d2];
            try {
                BW = new BufferedReader(new FileReader(server.getFile(tableName)));
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            }
            int i = 0;
            while ((S = BW.readLine()) != null) {
                String[] line = S.split(",");
                for (int j = 0; j < line.length; j++) {
                    data[i][j] = line[j];
                }
                i++;
            }
            if (condition) {
                for (int c = 0; c < data[0].length; c++) {
                    if (data[0][c].compareToIgnoreCase(colOfCondition[0]) == 0) {
                        numberColOfCondition = c;
                    }
                }
                ArrayList<Integer> linesNumbers = new ArrayList<Integer>();
                for (int q = 0; q < data.length; q++) {
                    if (data[q][numberColOfCondition].contains(colOfCondition[1])) {
                        linesNumbers.add(q);
                    }
                }
                lines = new int[linesNumbers.size()];
                for (int l = 0; l < lines.length; l++) {
                    lines[l] = linesNumbers.get(l);
                }
                String[][] newData = new String[lines.length + 1][data[0].length];
                for (int l = 0; l < newData[0].length; l++) {
                    newData[0][l] = data[0][l];
                }
                int g = 1;
                for (int l = 1; l < data.length; l++) {
                    for (int li = 0; li < lines.length; li++) {
                        if (lines[li] == l) {
                            for (int ll = 0; ll < data[l].length; ll++) {
                                newData[g][ll] = data[l][ll];
                            }
                            g++;
                        }
                    }
                }
                data = newData;
            }
        } catch (IOException e) {
            System.out.print(e.getMessage());
        }
        if (columnNature(instructions[1])) {
            int[] columnsNumber = filterColumns(data, instructions[1]);
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < columnsNumber.length; j++) {
                    if (j == columnsNumber.length - 1) {
                        serverResponse += data[i][columnsNumber[j]] + "\n";
                    } else {
                        serverResponse += data[i][columnsNumber[j]] + "\t| ";
                    }
                }
            }
        } else {
            serverResponse = new String();
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[i].length; j++) {
                    if (j == data[i].length - 1) {

                        serverResponse += data[i][j] + "\n";
                    } else {
                        serverResponse += data[i][j] + "\t| ";
                    }
                }
            }
        }
        serverResponse += (data.length - 1) + " ligne(s) selectionnée(s)\n";
        return serverResponse;
    }
}