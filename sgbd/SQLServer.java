package view;

import java.io.*;

import controller.Server;

public class SQLServer {
    private static int getServerPort() {
        int port = 0;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("../config/config.xml"));
            String string = new String();
            try {
                string = new String(reader.readLine());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            String[] ports = string.split(":");
            port = Integer.valueOf(ports[1]);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return port;
    }

    public static void main(String[] args) throws IOException {
        Server server = null;
        try {
            server = new Server(getServerPort());
            server.getCommand();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
