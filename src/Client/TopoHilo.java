package Client;

import SerializableObjects.UDPMessage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.util.Random;

import static javax.swing.JOptionPane.showMessageDialog;

public class TopoHilo extends Thread {

    MulticastSocket socketUDP;
    Random r = new Random();

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
                        try {//no ha habido ganador
                            topoID = Integer.parseInt(msjRecibido);
                            //System.out.println("hoyo:" + topoID);
                            if (topoID > 15) {
                                topoID = r.nextInt(16);
                            }
                            Juego.creaTopo(topoID);
                        }catch(Exception e){//truena el parseInt -- hay ganador
                            Juego.score = 0;
                            showMessageDialog(null, "Gana " + msjRecibido);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    //} catch (ClassNotFoundException e) {
                       // e.printStackTrace();
                        }
                }
        }
    }
}
