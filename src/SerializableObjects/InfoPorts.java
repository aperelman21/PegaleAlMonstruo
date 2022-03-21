package SerializableObjects;

import java.io.Serializable;

public class InfoPorts implements Serializable {

    private int port;
    private String dirIP;

    public InfoPorts(int port, String dirIP) {
        this.port = port;
        this.dirIP = dirIP;
    }

    public int getPort() {
        return port;
    }

    public String getDirIP() {
        return dirIP;
    }
}//class
