package server;

import java.io.*;
import java.net.*;

public class TCPServer extends Thread{

    private Socket Socket;
    private ServerSocket Server;

    private final int port;

    public TCPServer(int port) {
        this.port = port;
        
        try {
        	Server = new ServerSocket(port);
            System.out.println("Starting TCP Server");
        } catch (IOException e) {
        	e.printStackTrace();
        }
    }

    public void run() {
        try {
        	System.out.println("Waiting for TIVA Connection");
        	
            Socket = Server.accept();
            System.out.println("New connection from " + Socket.getRemoteSocketAddress());
        
            InputStreamReader InputStream = new InputStreamReader(Socket.getInputStream());    
            BufferedReader Reader = new BufferedReader(InputStream);
            	
            while (true) {
            	char[] command = new char[4];
            	
            	int n = Reader.read(command);
            	String commandStr = new String(command);
            	
            	if (commandStr.matches("EXIT") || n == -1)
            		break;
            	
            	System.out.println(commandStr);
            }
            
            InputStream.close();
            Reader.close();
            Socket.close();
            
        } catch (IOException e) {
        	e.printStackTrace();
        }

    }
}
