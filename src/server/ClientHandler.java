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
                System.out.println("Invalid identifier sent, closing connection");
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
                switch (new String(mesg)) {
                    case "gpsd":
                        this.Out.write('1');
                        char[] LatLong = new char[30];  // Increase size to accommodate both latitude and longitude

                        int n = this.Input.read(LatLong, 0, 30); 

                        String[] both = new String(LatLong).split(",");  // Split values using comma as separator

                        if (both.length == 2) {  // Ensure that both values were received
                            GpsData.Latitude = Double.parseDouble(both[0]);  // First value is latitude
                            GpsData.Longitude = Double.parseDouble(both[1]);  // Second value is longitude
                        } else {
                            System.out.println("Invalid GPS data received");
                        }

                        break;

                    
                    case "getd":
                        System.out.println("Current Longitude: " + GpsData.Latitude);

                    case "exit":
                        running = false;
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }
}