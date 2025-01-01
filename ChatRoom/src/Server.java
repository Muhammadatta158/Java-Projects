import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable{

    private ArrayList<ConnectionHandler> users;
    private ServerSocket server = null;
    private ExecutorService pool;
    private boolean done;

    public Server(){
        users = new ArrayList<ConnectionHandler>();
        done = false;
    }

    @Override
    public void run() {
        try{
            server = new ServerSocket(9999);
            pool = Executors.newCachedThreadPool();
            System.out.println("Server started, waiting for clients to connect...");

            while(!done){
            Socket client = server.accept();
            ConnectionHandler handler = new ConnectionHandler(client);
            users.add(handler);
            pool.execute(handler);
            }

        }catch(Exception e){
            e.printStackTrace();
            shutdown();
        }
    }

    public void broadcast(String message){
        for(ConnectionHandler c: users){
            if(c != null){
                c.sendMessage(message);
            }
        }
    }

    public void shutdown(){
        done = true;
        try{
            for(ConnectionHandler c: users){
                c.shutdown();
            }

            if(!server.isClosed()){
                server.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        
    }

    public class ConnectionHandler implements Runnable {
    private Socket client;
    private String Nickname;
    private PrintWriter out;
    private BufferedReader in;
    //private Server user;
    

    public ConnectionHandler(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {

        try{
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            out.println("Please enter a Nickname: ");
            Nickname = in.readLine();
            System.out.println(Nickname + " is connected successfully");
            broadcast(Nickname + " has joined the chat"); 

            String message;
            while ((message = in.readLine()) != null){
                if(message.startsWith("/quit")){
                    broadcast(Nickname+ " has left the chat."); 
                    shutdown();
                }else if(message.startsWith("/Nickname")){
                    String[] messageSplit = message.split(" ", 2);
                    if(messageSplit.length == 2){
                        String oldNickname = Nickname;
                        Nickname = messageSplit[1];
                        broadcast(oldNickname + " renamed themselves to: " + Nickname);
                        out.println("Nickname changed successfully to " + Nickname);
                    }else{
                        out.println("Please enter a valid Nickname");
                    }

                }else{
                    broadcast(Nickname + ":"+ " "+ message);
                }
            }
        }catch(IOException e){
            e.printStackTrace();
            shutdown(); 
        }
        
    }

    void sendMessage(String message){
        out.println(message);
    }

    public void shutdown(){
        try{
            in.close();
            out.close();
        if(!client.isClosed()){
            client.close();
        }
    }catch(IOException e){
        //Ignore
    }
        
    }

}

}