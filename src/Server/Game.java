package Server;

import SerializableObjects.Player;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.Random;

public class Game {
    private int maxScore;
    private InetAddress group;
    private MulticastSocket socket;
    private ArrayList<Player> players;
    private MulticastSocket msocket = null;
    Random rand;

    public InetAddress getGroup() {
        return group;
    }

    public void setGroup(InetAddress group) {
        this.group = group;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public MulticastSocket getMsocket() {
        return msocket;
    }

    public void setMsocket(MulticastSocket msocket) {
        this.msocket = msocket;
    }

    public void initialize(){
        try {
            this.group = InetAddress.getByName("228.5.6.7"); //destination multicast group
            this.socket = new MulticastSocket(49155);
            this.socket.joinGroup(group);

            this.maxScore = 5;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void endGame(){
        try {
            this.msocket.leaveGroup(group);

        }catch (IOException e){
            e.printStackTrace();
        }
        this.msocket.close();
    }

    private Player getPlayerMaxScore(){
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
        String message = String.valueOf(hole);
        byte[] m = message.getBytes();
        DatagramPacket messageOut =
                new DatagramPacket(m, m.length, group, 49155);
        try {
            socket.send(messageOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//sendMonster



    public void sendWinner() throws IOException{
        Player winner = getPlayerMaxScore();
        String victoryMsg = "Gana " + winner.getPlayerId();
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        ObjectOutput oo = new ObjectOutputStream(bStream);
        oo.writeObject(victoryMsg);
        oo.close();

        byte[] m = bStream.toByteArray(); //enviamos la jugada a todos los jugadores activos
        DatagramPacket messageOut = new DatagramPacket(m, m.length, this.getGroup(), 6789);
        this.msocket.send(messageOut);

    }

}//Game
