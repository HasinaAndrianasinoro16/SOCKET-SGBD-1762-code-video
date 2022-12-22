package model;

import java.io.File;

import controller.Server;

public class Droper {
    public static boolean syntaxeFormat(String[] commands) {
        boolean baseSyntaxe = false;
        if (commands[0].compareToIgnoreCase("drop") == 0 && commands[1].compareToIgnoreCase("table") == 0 )
            baseSyntaxe = true;
        return baseSyntaxe;
    }

    public static String traitement(Server server, String[] instuctions) {
        String serverResponse = new String();
        // File file = server.getFile(instuctions[2]);
        File file=new File("file/"+instuctions[2]+".txt");
    
        if (!syntaxeFormat(instuctions))
            serverResponse = "Erreur de syntaxe";
        else if (file.exists()) {
            file.delete();
            serverResponse = "table supprimée avec succès";
            
        } else {
            serverResponse = "table inexistante";
        }
        return serverResponse;
    }
}
