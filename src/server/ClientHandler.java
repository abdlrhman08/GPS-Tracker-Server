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
                GpsData.TivaConn = true;
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
                
                if (f == -1) {
                	
                	if (this.ID == 0)
                		GpsData.TivaConn = false;
                	else if (this.ID == 1)
                		GpsData.firstAppConnection = true;
                	
                	running = false;
                }
                
                
                switch (mesgStr) {
                    case "gpsd":
                        char[] LatLong = new char[60];  // Increase size to accommodate both latitude and longitude

                        int n = this.Input.read(LatLong, 0, 60); 

                        String[] both = new String(LatLong).trim().split(",");  // Split values using comma as separator

                        if (both.length == 4) {  // Ensure that both values were received
                            GpsData.Latitude = Double.parseDouble(both[0]);  // First value is latitude
                            GpsData.Longitude = Double.parseDouble(both[1]);// Second value is longitude
                            GpsData.movedDistance = Double.parseDouble(both[2]);
                            GpsData.Speed = Integer.parseInt(both[3]);
                        } else {
                            System.out.println("Invalid GPS data received: " + new String(LatLong).trim());
                            break;
                        }
                        
                        System.out.println("Data received: " + new String(LatLong).trim());
                        break;
                    
                    case "getd":
                    	int tivaConn = 0;
                    	
                    	if (GpsData.TivaConn)	
                    		tivaConn = 1;
                    		
                    	String data = Double.toString(GpsData.movedDistance) + "," + GpsData.Time + "," + GpsData.Speed + "," + Double.toString(GpsData.Latitude) + "," + Double.toString(GpsData.Longitude) + "," + Double.toString(GpsData.linearDistance) + "," + tivaConn;
                    	
                    	if (GpsData.firstAppConnection && this.ID == 1) {
                    		data += Double.toString(GpsData.linearDistance);
                    		GpsData.firstAppConnection = false;
                    	}
                    		
                    	this.Out.write(data.getBytes());
                    	
                    	System.out.println("Current data sent: " + data);
                        break;
                        
                    case "dest":
                    	if (this.ID == 1) {
                    		this.Out.write('1');
                    		char[] recvData = new char[30];
                    		
                    		int x = this.Input.read(recvData, 0, 30);
                    		String[] recvLatLong = new String(recvData).trim().split(",");
                    		
                    		GpsData.destinationLat = Double.parseDouble(recvLatLong[0]);
                    		GpsData.destinationLong = Double.parseDouble(recvLatLong[1]);
                    		
                    		System.out.println("Destination: " + new String(recvData).trim());
                    		
                    	} else if (this.ID == 0) {
                    		System.out.println("Tiva is requesting destination");
                    		char[] recvData = new char[512];
                    		
                    		int x = this.Input.read(recvData, 0, 30);
                    		System.out.println("Received");
                    		String[] recvLatLong = new String(recvData).trim().split(",");
                    	
                    		GpsData.StartLatitude = Double.parseDouble(recvLatLong[0]);
                    		GpsData.StartLongitude = Double.parseDouble(recvLatLong[1]);
                    		
                    		while (GpsData.destinationLat == 0 || GpsData.destinationLong == 0) {
                    			System.out.println("Waiting for application connection first");
                    		}
                    		
                    		GpsData.linearDistance = GpsData.calculateDistance(GpsData.StartLatitude, GpsData.destinationLat, GpsData.StartLongitude, GpsData.destinationLong);
                    	
                    		System.out.println("Starting point received" + new String(recvData).trim());	
                    		String Latitude = Double.toString(GpsData.destinationLat);
                    		String Longitude = Double.toString(GpsData.destinationLong);
                    		
                    		String total = Latitude + "," + Longitude + "X";
                    	} 
                    	break;
                    case "exit":
                        running = false;
                        GpsData.firstAppConnection = true;
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