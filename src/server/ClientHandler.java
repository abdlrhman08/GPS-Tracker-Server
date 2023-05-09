package server;

import java.io.*;
import java.net.*;

public class ClientHandler extends Thread{
    private Socket socket;

    private BufferedReader Input;
    private DataOutputStream Out;

    private int ID;

    private boolean running = true;

    public ClientHandler(Socket socket, BufferedReader Input, DataOutputStream Out) {
        this.socket = socket;
        this.Input = Input;
        this.Out = Out;
    }

    @Override
    public void run() {
        char[] ID = new char[4];

        try {
            int data = Input.read(ID, 0, 4);
        } catch (IOException e) {
            //TODO: Add exception
            running = false;
            e.printStackTrace();
        }

        switch (new String(ID)) {

            case "tiva":
                this.ID = 0;
                System.out.println("Tiva connected, given id: " + this.ID);
                break;
            case "appl":
                this.ID = 1;
                System.out.println("Application connected, given id: " + this.ID);
                break;
            default:
                System.out.println("Invalid identifier sent, closing connection. Sent data: " + new String(ID));
                try {
                    this.socket.close();
                    this.Input.close();
                    this.Out.close();

                    running = false;
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

        }

        while (running) {
            char[] mesg = new char[4];

            try {
                int f = this.Input.read(mesg, 0, 4);
                String mesgStr = new String(mesg);
                
                if (f == -1) continue;
                
                
                switch (mesgStr) {
                    case "gpsd":
                        this.Out.write('1');
                        char[] LatLong = new char[30];  // Increase size to accommodate both latitude and longitude

                        int n = this.Input.read(LatLong, 0, 30); 

                        String[] both = new String(LatLong).trim().split(",");  // Split values using comma as separator

                        if (both.length == 2) {  // Ensure that both values were received
                            GpsData.Latitude = Double.parseDouble(both[0]);  // First value is latitude
                            GpsData.Longitude = Double.parseDouble(both[1]);  // Second value is longitude
                        } else {
                            System.out.println("Invalid GPS data received");
                            break;
                        }
                        
                        System.out.println("Data received: " + new String(LatLong).trim());
                        break;
                    
                    case "getd":
                    	String data = Double.toString(GpsData.Latitude) + "," + Double.toString(GpsData.Longitude);
                    	this.Out.write(data.getBytes());
                    	
                    	System.out.println("Current data sent: " + data);       
                        break;
                    case "exit":
                        running = false;
                        break;
                    default:
                    	System.out.println("Unknown command sent: " + mesgStr);
                    	break;
                        
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }
}