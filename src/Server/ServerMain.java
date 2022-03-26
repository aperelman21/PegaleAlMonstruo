package Server;
import Interfaces.LoginRMI;
import SerializableObjects.Player;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServerMain {



    public static void main(String[] args) throws IOException, InterruptedException {

        System.setProperty("java.security.policy", "src/Server/server.policy");

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }//if
        //Desplegar servicio RMI
        try {
            String serverAddress = "192.168.100.87";
            System.setProperty("java.rmi.server.hostname", serverAddress);

            // start the rmi registry
            LocateRegistry.createRegistry(1099);   /// default port
            SlaveNode slave = new SlaveNode();
            slave.deploy();
        } catch (Exception e) {
            System.err.println("ComputeEngine exception");
            e.printStackTrace();
        }//catch

        //Instanciar servidor TCP y empezar el jeugo
        TCPServer servTCP = new TCPServer();
        servTCP.start();
        Game game = servTCP.getGame();
        //Instanciar grupo multicast
        game.initialize();
        boolean flag = true;
        Player maxPlayer;
        int maxScore = game.getMaxScore();
        //mientras nadie haya ganado, enviamos monstruos por multicast
        while(flag){
            game.sendMonster();
            Thread.sleep(1000);
            //Checar si alguien ya gano
            maxPlayer = game.getPlayerMaxScore();
            if(maxPlayer.getPlayerScore()==maxScore) {
                flag = false;
            }
        }
        //Cuando alguien ya gano enviamos al ganador
        game.GameOver();
    }//main
}//class
