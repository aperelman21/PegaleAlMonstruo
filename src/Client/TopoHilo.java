package Client;

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
                        if(msjRecibido.charAt(0) != '_') {//No ha habido ganador
                            topoID = Integer.parseInt(msjRecibido);
                            //System.out.println("hoyo:" + topoID);
                            if (topoID > 15) {
                                topoID = r.nextInt(16);
                            }
                            Juego.creaTopo(topoID);
                        }
                        else {//Hubo ganador
                            Juego.score = 0;
                            showMessageDialog(null, "Gana " + msjRecibido);
                            break;//esta línea salvó el semestre
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
