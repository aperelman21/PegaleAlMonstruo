package Server;

import Interfaces.LoginRMI;
import SerializableObjects.InfoPorts;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class SlaveNode implements LoginRMI {


    public SlaveNode() throws RemoteException {
        super();
    }//Constructor

    public void deploy() {
        try {
            SlaveNode engine = new SlaveNode();
            LoginRMI stub = (LoginRMI) UnicastRemoteObject.exportObject(engine,0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("LoginRMI",stub);
            System.out.println("Servicio RMI desplegado");
        } catch (Exception e) {
            System.err.println("ComputeEngine exception");
            e.printStackTrace();
        }//catch
    }//deploy

    @Override
    public InfoPorts getInfo(String player) throws RemoteException{
        InfoPorts info = new InfoPorts(1,"0.0.0.0");
        return info;
    }

}//class
