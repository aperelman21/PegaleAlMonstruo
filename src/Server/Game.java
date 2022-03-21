package Server;

import SerializableObjects.Player;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Random;

public class Game {
    private int maxScore;
    private InetAddress group;
    private MulticastSocket socket;
    private ArrayList<Player> players;
    Random rand;



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

}//Game






}
