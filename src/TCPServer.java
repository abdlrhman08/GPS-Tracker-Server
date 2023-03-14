import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer extends Thread{

    private Socket Socket = null;
    private ServerSocket Server = null;

    private int port;

    public TCPServer(int port) {
        this.port = port;
    }

    public void run() {
        try {
            Server = new ServerSocket(port);
            System.out.println("Starting TCP Server");

            Socket = Server.accept();
            System.out.println("New connection from " + Socket.getRemoteSocketAddress());
        } catch (IOException e) {}

    }
}
