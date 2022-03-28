package Server;

import SerializableObjects.Player;

import java.net.*;
import java.io.*;
import java.util.ArrayList;


public class TCPServer extends Thread{

    public TCPServer(){
        super();
    }//builder

    private Game game = new Game();

    public Game getGame() {
        return game;
    }

    @Override
    public void run(){
        try {
            System.out.println("entra a tcpServer");
            int serverPort = 49152;
            ServerSocket listenSocket = new ServerSocket(serverPort);
            while (true) {
                System.out.println("antes accept");
                Socket clientSocket = listenSocket.accept();  // Listens for a connection to be made to this socket and accepts it. The method blocks until a connection is made.
                System.out.println("despues accept");
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
        System.out.println("Entra al Run del TCPServer");
        try{
            player = (Player) in.readObject(); // recibimos el objeto de jugador que envia el cliente
            if(game.isNewPlayer(player)){
                game.addPlayer(player);
            }
            Player player;
            while(true){
                player = (Player) in.readObject();
                System.out.println("jugador le pego al monstruo: "+ player.getPlayerId() +" score: "+player.getPlayerScore());
                game.updateScore(player);
            }//while
        }//try
        catch(Exception e){
            e.printStackTrace();
        }
    }//run
}//Connection
