package server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;

public class Server {
	private ServerSocket server;
	
	public Server(int port)	throws IOException {
		this.server = new ServerSocket(port);
	}
	
	public void listen() throws IOException {
		
		while (true) {
            Socket socket = null;
              
            try {
                socket = server.accept();
                  
                System.out.println("A new client is connected : " + socket.getInetAddress());
                  
                InputStreamReader Input = new InputStreamReader(socket.getInputStream());
                BufferedReader reader = new BufferedReader(Input);
                
                DataOutputStream Out = new DataOutputStream(socket.getOutputStream());
                  
                System.out.println("Assigning new thread for this client");
  
                Thread thread = new ClientHandler(socket, reader, Out);
  
                thread.start();
                  
            }
            catch (Exception e){
                socket.close();
                e.printStackTrace();
            }
        }
	}
}
