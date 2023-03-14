import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer extends Thread{

    private Socket Socket = null;
    private ServerSocket Server = null;

    public TCPServer(int port) {

        //Process may fail so its safe to throw exception
        try {
            Server = new ServerSocket(port);
        }
    } catch (IOException e) {}

    public void run() {


    }
}
