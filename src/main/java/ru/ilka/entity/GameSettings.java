package ru.ilka.entity;

/**
 * Here could be your advertisement +375 29 3880490
 */
public class GameSettings {
    private int minBet;
    private int maxBet;

    public GameSettings(int minBet, int maxBet) {
        this.minBet = minBet;
        this.maxBet = maxBet;
    }

    public GameSettings() {
    }

    public int getMinBet() {
        return minBet;
    }

    public void setMinBet(int minBet) {
        this.minBet = minBet;
    }

    public int getMaxBet() {
        return maxBet;
    }

    public void setMaxBet(int maxBet) {
        this.maxBet = maxBet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameSettings settings = (GameSettings) o;

        if (minBet != settings.minBet) return false;
        return maxBet == settings.maxBet;
    }

    @Override
    public int hashCode() {
        int result = minBet;
        result = 31 * result + maxBet;
        return result;
    }
}