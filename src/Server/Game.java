package Server;

import SerializableObjects.Player;
import SerializableObjects.UDPMessage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Random;

public class Game {
    private int maxScore;
    private InetAddress group;
    private MulticastSocket socket;
    private ArrayList<Player> players;
    private MulticastSocket msocket = null;
    Random rand = new Random();
    public Game() {
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void initialize(){
        try {
            this.group = InetAddress.getByName("228.5.6.7"); //destination multicast group
            this.socket = new MulticastSocket(49155);
            this.socket.joinGroup(group);

            this.maxScore = 5;
            Player defaultP = new Player("Server",1);
            players = new ArrayList<Player>();
            players.add(defaultP);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GameOver() throws IOException{
        Player winner = getPlayerMaxScore();
        String victoryMsg = "Gana " + winner.getPlayerId();

        byte[] m = victoryMsg.getBytes(); //enviamos la jugada a todos los jugadores activos
        DatagramPacket messageOut = new DatagramPacket(m, m.length, group, 49155);
        this.msocket.send(messageOut);
        try {
            this.msocket.leaveGroup(group);

        }catch (IOException e){
            e.printStackTrace();
        }
        this.msocket.close();
    }

    public Player getPlayerMaxScore(){
        Player maxPlayer = null, currentPlayer = null;
        int maxScore = 0, currentScore;
        for (int i = 0; i < players.size(); i++) {
            currentPlayer = players.get(i);
            currentScore = currentPlayer.getPlayerScore();
            if (currentScore > maxScore) {
                maxScore = currentScore;
                maxPlayer = currentPlayer;
            }
        }
            return maxPlayer;
    }

    public void sendMonster(){
        int hole = rand.nextInt(16);
        UDPMessage message = new UDPMessage(hole,false,null);
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        ObjectOutput oo = null;
        try {
            oo = new ObjectOutputStream(bStream);
            oo.writeObject(message);
            System.out.println("Envio un mosnturo al hoyo: "+ hole);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//sendMonster

    public boolean isNewPlayer(Player newPlayer){
        boolean resp;
        resp = true;
        Player player;
        String newId = newPlayer.getPlayerId();
        for (int i = 0; i < players.size(); i++) {
            player = players.get(i);
            if (player.getPlayerId().equals(newId)) {
                resp = false;
            }
        }
        return resp;
    }

    public void updateScore(Player player1){
        Player player2;
        for (int i = 0; i < players.size(); i++) {
            player2 = players.get(i);
            String player2ID = player2.getPlayerId();
            if (player1.getPlayerId().equals(player2ID)) {
                players.set(i,player1);
            }
        }
    }

    public void addPlayer(Player newPlayer){
        this.players.add(newPlayer);
    }

}//Game
