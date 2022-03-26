package Client;
import SerializableObjects.InfoPorts;
import SerializableObjects.Player;
import SerializableObjects.UDPMessage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.net.*;

public class Juego extends JFrame {
    public static JButton btnTopos[] = new JButton[16];
    public static boolean tablero[] = new boolean[16];
    private JLabel lblScore;
    private JLabel lblTimeLeft;
    private ImageIcon topoInImg = new ImageIcon(getClass().getResource("moleIn.png"));
    private ImageIcon topoOutImg = new ImageIcon(getClass().getResource("moleOut.png"));
    private Icon topoInImgRedo;
    private static Icon topoOutImgRedo;
    public int score;
    private Timer timer;
    private final int duracion = 30;
    private final int topoWidth = 132;
    private final int topoHeight = 132;
    private int contadorTiempo;
    private ScheduledExecutorService executor;
    private Player player;
    private InfoPorts info;
    private Socket socketTCP;
    private MulticastSocket socketUDP;
    private ObjectOutputStream out;
    private boolean juegoIniciado = true;
    private TopoHilo topoHilo;

    public Juego() {
        score = 0;
        contadorTiempo = duracion;
        init();
        iniciaJuego();
        iniciaTimer();
    }

    public Juego(String idPlayer){
        this.player = new Player(idPlayer,0);
        score = 0;
        contadorTiempo = duracion;
        init();
        initConnection();
        iniciaJuego();
        iniciaTimer();
        topoHilo = new TopoHilo(socketUDP);
        topoHilo.start();
    }

    public Juego(Player player,InfoPorts info) throws IOException {
        this.player = player;
        this.info = info;
        score = 0;
        contadorTiempo = duracion;
        joinMultiCast(info.getPortUDP());
        init();
        //initConnection();
        iniciaJuego();
        // iniciaTimer();
        topoHilo = new TopoHilo(this.socketUDP);
        topoHilo.start();
    }

    public void joinMultiCast(int portUDP) throws IOException {
        try {
            InetAddress group = InetAddress.getByName("228.5.6.7"); // destination multicast group
            this.socketUDP = new MulticastSocket(portUDP);
            socketUDP.joinGroup(group);
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (socketTCP != null) socketTCP.close();
        }
    }

    public void init() {

        setTitle("Whack A Mole");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(500, 100, 608, 720);

        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(new Color(0, 51, 0));
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPanel. setLayout(null);

        JPanel panel = new JPanel();
        panel.setBounds(35, 105, 528, 529);
        panel.setLayout(null);
        contentPanel.add(panel);

        lblScore = new JLabel("Score: 0");
        lblScore.setForeground(new Color(135, 206, 250));
        lblScore.setHorizontalAlignment(SwingConstants.TRAILING);
        lblScore.setFont(new Font("Cambria", Font.BOLD, 14));
        lblScore.setBounds(423, 54, 144, 33);
        contentPanel.add(lblScore);

        lblTimeLeft = new JLabel("30");
        lblTimeLeft.setHorizontalAlignment(SwingConstants.CENTER);
        lblTimeLeft.setForeground(new Color(240, 128, 128));
        lblTimeLeft.setFont(new Font("Cambria Math", Font.BOLD, 24));
        lblTimeLeft.setBounds(232, 54, 144, 33);
        contentPanel.add(lblTimeLeft);

        topoInImgRedo = resizeIcon(topoInImg, topoWidth, topoHeight);
        topoOutImgRedo = resizeIcon(topoOutImg, topoWidth, topoHeight);

        for (int i = 0, x = 0, y = 396; i < 16; i++) {
            btnTopos[i] = new JButton();
            btnTopos[i].setName(String.valueOf(i));
            btnTopos[i].setBounds(x, y, topoWidth, topoHeight);
            panel.add(btnTopos[i]);
            btnTopos[i].setIcon(topoInImgRedo);
            x = (x + topoWidth) % 528;
            y = x == 0 ? y - topoHeight : y;
            tablero[i] = false;
        }

        setContentPane(contentPanel);
    }

    private void initConnection() {
        try {
            String serverIP = this.info.getDirIP();
            int serverPort = this.info.getPortTCP();
            socketTCP = new Socket(serverIP,serverPort);
            out = new ObjectOutputStream(socketTCP.getOutputStream());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static Icon resizeIcon(ImageIcon icon, int resizedWidth, int resizedHeight) {
        Image img = icon.getImage();
        Image resizedImage = img.getScaledInstance(resizedWidth, resizedHeight,  java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    public int creaRandTopo() {
        int topoID = new Random(System.currentTimeMillis()).nextInt(16);
        tablero[topoID] = true;
        btnTopos[topoID].setIcon(topoOutImgRedo);
        return topoID;
    }

    public static int creaTopo(int topoID) {
        tablero[topoID] = true;
        btnTopos[topoID].setIcon(topoOutImgRedo);
        return topoID;
    }

    private void clickTopo(int topoID) {
        if (juegoIniciado) {
            if (tablero[topoID]) {
                score++;
                this.player.setPlayerScore(score);
                btnTopos[topoID].setIcon(topoInImgRedo);
                tablero[topoID] = false;
                lblScore.setText(String.valueOf(score));
                try {
                    out.writeObject(this.player);
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    private void iniciaJuego() {
        for (JButton topo : btnTopos) {
            topo.addMouseListener(new MouseListener() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void mousePressed(MouseEvent e) {
                    JButton btn = (JButton) e.getSource();
                    int topoID = Integer.parseInt(btn.getName());
                    clickTopo(topoID);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void mouseExited(MouseEvent e) {
                    // TODO Auto-generated method stub

                }

            });
        }
    }

    private void gameOver() {
        contadorTiempo = duracion;
        executor.shutdown();
        timer.cancel();
        for (int i=0; i<btnTopos.length; i++) {
            btnTopos[i].setIcon(topoInImgRedo);
        }
        juegoIniciado = false;
    }

    private void limpiaTopo(int topoID) {
        tablero[topoID] = false;
        btnTopos[topoID].setIcon(topoInImgRedo);
    }

    private void iniciaTimer() {
        Runnable topoTask = new Runnable() {
            int topoID = -1;
            public void run() {
                if (topoID != -1) {
                    limpiaTopo(topoID);
                }
                topoID = creaRandTopo();
            }
        };
        executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(topoTask, 0, 1000, TimeUnit.MILLISECONDS);

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                --contadorTiempo;
                lblTimeLeft.setText(String.valueOf(contadorTiempo));
                if (contadorTiempo == 0) {
                    gameOver();
                }
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 0,1000);

    }

    public static void main(String[] args) {
        Juego juego = new Juego("Jugador1");
        juego.setVisible(true);
    }
}