package ru.ilka.entity;

/**
 * Here could be your advertisement +375 29 3880490
 */
public class GameSettings {
    private int minBet;
    private int maxBet;
    private int numberOfDecks;

    public GameSettings() {
    }

    public GameSettings(int minBet, int maxBet) {
        this.minBet = minBet;
        this.maxBet = maxBet;
    }

    public GameSettings(int minBet, int maxBet, int numberOfDecks) {
        this.minBet = minBet;
        this.maxBet = maxBet;
        this.numberOfDecks = numberOfDecks;
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

    public int getNumberOfDecks() {
        return numberOfDecks;
    }

    public void setNumberOfDecks(int numberOfDecks) {
        this.numberOfDecks = numberOfDecks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameSettings settings = (GameSettings) o;

        if (minBet != settings.minBet) return false;
        if (maxBet != settings.maxBet) return false;
        return numberOfDecks == settings.numberOfDecks;
    }

    @Override
    public int hashCode() {
        int result = minBet;
        result = 31 * result + maxBet;
        result = 31 * result + numberOfDecks;
        return result;
    }
}
