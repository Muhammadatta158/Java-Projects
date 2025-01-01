public class App {
    public static void main(String[] args) throws Exception {
        Thread serverThread = new Thread(new Server());
        serverThread.start();

        Thread clientThread = new Thread(new Client());
        clientThread.start();
        
        serverThread.join();
        clientThread.join();
    }
}
