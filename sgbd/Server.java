package controller;

import java.io.*;
import java.net.*;

import model.Creator;
import model.Droper;
import model.Inserter;
import model.Selector;
import model.Updater;

public class Server {
    
    private int port;
    private File file;
    private ServerSocket serverSocket;
    private Socket socket;
    private DataInputStream dataInpout;
    private DataOutputStream dataOutpout;

    public Server(int port) throws IOException {
        this.setPort(port);
        try {
            this.setServerSocket(new ServerSocket(port));
            this.setSocket(this.getServerSocket().accept());
            this.setDataInput(new DataInputStream(this.getSocket().getInputStream()));
            this.setDataOutput(new DataOutputStream(this.getSocket().getOutputStream()));
        } catch (IOException e) {
           throw e;
        }
    }
    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public File getFile(String file) {
        setFile("file/"+file+".txt");
        return this.file;
    }

    public void setFile(String path) {
        this.file = new File(path);
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public DataInputStream getDataInput() {
        return dataInpout;
    }

    public void setDataInput(DataInputStream dataInpout) {
        this.dataInpout = dataInpout;
    }

    public DataOutputStream getDataOutput() {
        return dataOutpout;
    }

    public void setDataOutput(DataOutputStream dataOutpout) {
        this.dataOutpout = dataOutpout;
    }
    
    public void getCommand() throws IOException {
        String commande=new String(),serverResponse=new String();
        try{
            while(commande.compareToIgnoreCase("exit")!=0){
                commande=this.getDataInput().readUTF();
                System.out.println("Commande: "+commande);
                String[] instuctions=commande.split(" ");
                if(instuctions[0].compareToIgnoreCase("create")==0){
                    serverResponse=Creator.traitement(instuctions);
                }
                else if(instuctions[0].compareToIgnoreCase("insert")==0){
                    serverResponse=Inserter.traitement(this, instuctions);
                }
                else if(instuctions[0].compareToIgnoreCase("select")==0){
                    serverResponse=Selector.traitement(this, instuctions);
                }
                else if(instuctions[0].compareToIgnoreCase("update")==0){
                    serverResponse=Updater.traitement(this, instuctions);
                }
                else if(instuctions[0].compareToIgnoreCase("drop")==0){
                    serverResponse=Droper.traitement(this, instuctions);
                }
                else if(instuctions[0].compareToIgnoreCase("exit")==0){
                    serverResponse="exited";
                }
                else{
                    serverResponse="commande introuvable";
                }
                this.getDataOutput().writeUTF(serverResponse);
            }
        }
        catch (IOException e) {
            throw e;
        }
        finally {
            if(this.getDataInput() != null) {
                this.getDataInput().close();
            }
            if(this.getSocket() != null) {
                this.getSocket().close();  
            }
            if(this.getServerSocket() != null) {
                this.getServerSocket().close();
            }
        }
    }
}
