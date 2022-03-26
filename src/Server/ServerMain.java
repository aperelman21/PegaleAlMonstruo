package Server;
import Interfaces.LoginRMI;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServerMain {



    public static void main(String[] args) throws RemoteException {

        System.setProperty("java.security.policy", "src/Server/server.policy");

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }//if
        //Desplegar servicio RMI
        try {
            String serverAddress = "192.168.1.72";
            System.setProperty("java.rmi.server.hostname", serverAddress);

            // start the rmi registry
            LocateRegistry.createRegistry(1099);   /// default port
            SlaveNode slave = new SlaveNode();
            slave.deploy();
        } catch (Exception e) {
            System.err.println("ComputeEngine exception");
            e.printStackTrace();
        }//catch





    }//main
}//class
