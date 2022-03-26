package SerializableObjects;

import java.io.Serializable;

public class UDPMessage implements Serializable {
    private int hole;
    private boolean isWinner;
    private Player player;

    public UDPMessage(int hole, boolean isWinner, Player player) {
        this.hole = hole;
        this.isWinner = isWinner;
        this.player = player;
    }

    public int getHole() {
        return hole;
    }

    public boolean isWinner() {
        return isWinner;
    }

    public Player getPlayer() {
        return player;
    }
}
