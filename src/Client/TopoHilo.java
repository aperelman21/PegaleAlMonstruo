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
                        if (topoID>=0) {
                            Juego.limpiaTopo(topoID);
                        }
                        String msjRecibido = new String(messageIn.getData()).trim();
                        System.out.println("Message: " + msjRecibido);
                        //ByteArrayInputStream b = new ByteArrayInputStream(messageIn.getData());
                        //ObjectInputStream stream = new ObjectInputStream(b);
                        //UDPMessage message = (UDPMessage) stream.readObject();
                        topoID = Integer.parseInt(msjRecibido);
                        //System.out.println("hoyo:" + topoID);
                        if (topoID > 15) {
                            topoID = 15;
                        }
                        Juego.creaTopo(topoID);
                    } catch (Exception e) {
                        e.printStackTrace();
                    //} catch (ClassNotFoundException e) {
                       // e.printStackTrace();
                        }
                }
        }
    }
}
