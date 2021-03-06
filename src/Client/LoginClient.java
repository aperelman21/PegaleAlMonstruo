package Client;

import Interfaces.LoginRMI;
import SerializableObjects.InfoPorts;
import SerializableObjects.Player;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class LoginClient extends JFrame{
    private JTextField textField1;
    private JButton button1;
    private JPanel mainPanel;
    static private LoginRMI login;

    public LoginClient() {

        setContentPane(mainPanel);
        setTitle("login");
        setSize(450,300);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    String idPlayer = textField1.getText();
                    InfoPorts info = login.getInfo(idPlayer);
                    System.out.println("Me llego por RMI: direccion IP: "+info.getDirIP() + " puertoUDP: "+ info.getPortUDP()+ " puertoTCP: "+ info.getPortTCP());
                    Player player = new Player(idPlayer,0);
                    Juego juego = new Juego(player,info);
                    System.out.println("si se creo el jeugo");
                    juego.setVisible(true);
                    mainPanel.setVisible(false);

                } catch (IOException exception){
                    System.out.println("IO:" + exception.getMessage());
                }

            }
        });
    }//login client


    public static void main(String[] args) {
        LoginClient myFrame = new LoginClient();

        System.setProperty("java.security.policy", "src/client/client.policy");

        //if (System.getSecurityManager() == null) {
          //  System.setSecurityManager(new SecurityManager());
        //}
        try {
            //String serverAddress = "?.?.?.?";
            String serverAddress = "localhost";
            String serviceName = "LoginRMI";
            Registry registry = LocateRegistry.getRegistry(serverAddress); // server's ip address args[0]
            login = (LoginRMI) registry.lookup(serviceName);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }//main
}
