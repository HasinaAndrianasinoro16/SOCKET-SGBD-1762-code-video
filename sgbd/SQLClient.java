package view;

import java.io.IOException;

import controller.Client;

public class SQLClient {
    public static void main(String[] args) throws IOException {
        Client client = null;
        try {
            client = new Client("localhost",1590);
            client.setCommand();
        } catch (IOException e) {
           e.printStackTrace();
        }
        
    }
}
