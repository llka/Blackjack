package ru.ilka.entity;
import java.util.ArrayList;

/**
 * Here could be your advertisement +375 29 3880490
 */
public class Game {
    private int gameId;
    private int bet;
    private int playerAccountId;
    private boolean playerWin;
    private double winCoefficient;
    private boolean playerIsDealer;
    private String time;
    private int points;

    public Game() {
    }

    public Game(int gameId, int bet, int playerAccountId, boolean playerWin, double winCoefficient, boolean playerIsDealer, String time, int points) {
        this.gameId = gameId;
        this.bet = bet;
        this.playerAccountId = playerAccountId;
        this.playerWin = playerWin;
        this.winCoefficient = winCoefficient;
        this.playerIsDealer = playerIsDealer;
        this.time = time;
        this.points = points;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getBet() {
        return bet;
    }

    public void setBet(int bet) {
        this.bet = bet;
    }

    public int getPlayerAccountId() {
        return playerAccountId;
    }

    public void setPlayerAccountId(int playerAccountId) {
        this.playerAccountId = playerAccountId;
    }

    public boolean isPlayerWin() {
        return playerWin;
    }

    public void setPlayerWin(boolean playerWin) {
        this.playerWin = playerWin;
    }

    public double getWinCoefficient() {
        return winCoefficient;
    }

    public void setWinCoefficient(double winCoefficient) {
        this.winCoefficient = winCoefficient;
    }

    public boolean isPlayerIsDealer() {
        return playerIsDealer;
    }

    public void setPlayerIsDealer(boolean playerIsDealer) {
        this.playerIsDealer = playerIsDealer;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Game game = (Game) o;

        if (gameId != game.gameId) return false;
        if (bet != game.bet) return false;
        if (playerAccountId != game.playerAccountId) return false;
        if (playerWin != game.playerWin) return false;
        if (Double.compare(game.winCoefficient, winCoefficient) != 0) return false;
        if (playerIsDealer != game.playerIsDealer) return false;
        return time.equals(game.time);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = gameId;
        result = 31 * result + bet;
        result = 31 * result + playerAccountId;
        result = 31 * result + (playerWin ? 1 : 0);
        temp = Double.doubleToLongBits(winCoefficient);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (playerIsDealer ? 1 : 0);
        result = 31 * result + time.hashCode();
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Game{");
        sb.append("bet=").append(bet);
        sb.append(", playerAccountId=").append(playerAccountId);
        sb.append(", playerWin=").append(playerWin);
        sb.append(", time=").append(time);
        sb.append('}');
        return sb.toString();
    }
}
