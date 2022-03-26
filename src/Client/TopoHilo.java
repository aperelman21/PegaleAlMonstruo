package Client;

import SerializableObjects.UDPMessage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

public class TopoHilo extends Thread {

    MulticastSocket socketUDP;

    public TopoHilo(MulticastSocket socketUDP) {
        this.socketUDP = socketUDP;
    }

    @Override
    public void run() {
        while (true) {
            int topoID = -1;
            byte[] buffer = new byte[1000];
                while(true){
                    try {
                        DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
                        socketUDP.receive(messageIn);
                        System.out.println("Message: " + new String(messageIn.getData()).trim());
                        //ByteArrayInputStream b = new ByteArrayInputStream(messageIn.getData());
                        //ObjectInputStream stream = new ObjectInputStream(b);
                        //UDPMessage message = (UDPMessage) stream.readObject();
                        //topoID = message.getHole();
                        //System.out.println("hoyo:" + topoID);
                        //Juego.creaTopo(topoID);
                    } catch (Exception e) {
                        e.printStackTrace();
                    //} catch (ClassNotFoundException e) {
                       // e.printStackTrace();
                        }
                }
        }
    }
}
