package Server;

import SerializableObjects.Player;

import java.net.*;
import java.io.*;
import java.util.ArrayList;


public class TCPServer extends Thread{

    private Game game;

    public void run(){
        try {
            int serverPort = 49152;
            ServerSocket listenSocket = new ServerSocket(serverPort);
            game = new Game();
            while (true) {
                Socket clientSocket = listenSocket.accept();  // Listens for a connection to be made to this socket and accepts it. The method blocks until a connection is made.
                Connection c = new Connection(clientSocket,game);
                c.start();
            }
        } catch (IOException e) {
            System.out.println("Listen :" + e.getMessage());
        }
    }
}//TCP Server

class Connection extends Thread {

    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Socket clientSocket;
    private Game game;
    private Player player;

    public Connection(Socket aClientSocket,Game game) {
        try {
            clientSocket = aClientSocket;
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());
            this.game = game;
        } catch (IOException e) {
            System.out.println("Connection:" + e.getMessage());
        }
    }

    @Override
    public void run() {
        try{
            player = (Player) in.readObject(); // recibimos el objeto de jugador que envia el cliente
            if(game.isNewPlayer(player)){
                game.addPlayer(player);
            }
            Player player;
            while(true){
                player = (Player) in.readObject();
                game.updateScore(player);
            }//while
        }//try
        catch(Exception e){
            e.printStackTrace();
        }
    }//run
}//Connection
