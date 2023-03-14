public class Main {
    public static void main(String[] args) {
        TCPServer server = new TCPServer(26500);
        server.start();
        System.out.println("hmmmm");
    }
}