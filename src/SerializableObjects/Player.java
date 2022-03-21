package SerializableObjects;

import java.io.Serializable;

public class Player implements Serializable {
    private String playerId;
    private int playerScore;

    public Player(String playerId, int playerScore) {
        this.playerId = playerId;
        this.playerScore = playerScore;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }
}
