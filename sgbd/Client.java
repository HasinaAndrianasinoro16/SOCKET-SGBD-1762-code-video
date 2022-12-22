package controller;

import java.io.*;
import java.net.*;

public class Client {
    
    private String ip;
    private int port;
    private Socket socket;
    private DataInputStream dataInpout;
    private DataOutputStream dataOutpout;

    public Client(String ip, int port) throws IOException {
        this.setIp(ip);
        this.setPort(port);
        try {
            this.setSocket(new Socket(ip, port));
            this.setDataInput(new DataInputStream(this.getSocket().getInputStream()));
            this.setDataOutput(new DataOutputStream(this.getSocket().getOutputStream()));
         } catch (IOException e) {
             throw e;
         }

    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
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

    public DataOutputStream getDataOuput() {
        return dataOutpout;
    }

    public void setDataOutput(DataOutputStream dataOutpout) {
        this.dataOutpout = dataOutpout;
    }

    public void setCommand() throws IOException {
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
        String commande=new String();
        String serverResponse=new String();
        try{
            while(commande.compareToIgnoreCase("exit")!=0){
                System.out.print("SQL Hasina> ");
                commande=br.readLine();
                this.getDataOuput().writeUTF(commande);  
                this.getDataOuput().flush();
                serverResponse=this.getDataInput().readUTF();
                System.out.println(serverResponse);
            }
        }
        catch (IOException e) {
            throw e;
        }  
         finally {
            if(this.getDataOuput() != null) {
                this.getDataOuput().close();
            }
            if(this.getSocket() != null) {
                this.getSocket().close();  
            }
        }
    }
}
