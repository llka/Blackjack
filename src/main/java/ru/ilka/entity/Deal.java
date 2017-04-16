package ru.ilka.entity;

import ru.ilka.logic.LogicResult;

import java.util.ArrayList;

/**
 * Here could be your advertisement +375 29 3880490
 */
/* 1 Раздача */
public class Deal {
    private static final int BETS_QNT = 3;
    private ArrayList<ArrayList<LogicResult>> cards;
    private ArrayList<Integer> bets;
    private ArrayList<Integer> points;
    private boolean[] insuredBets;

    public Deal(){
        this.insuredBets = new boolean[BETS_QNT];
    }

    public Deal(ArrayList<ArrayList<LogicResult>> cards, ArrayList<Integer> bets, ArrayList<Integer> points) {
        this.cards = cards;
        this.bets = bets;
        this.points = points;
        this.insuredBets = new boolean[BETS_QNT];
    }

    public ArrayList<ArrayList<LogicResult>> getCards() {
        return cards;
    }

    public void setCards(ArrayList<ArrayList<LogicResult>> cards) {
        this.cards = cards;
    }

    public ArrayList<Integer> getBets() {
        return bets;
    }

    public void setBets(ArrayList<Integer> bets) {
        this.bets = bets;
    }

    public ArrayList<Integer> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<Integer> points) {
        this.points = points;
    }

    public boolean[] getInsuredBets() {
        return insuredBets;
    }

    public void setInsuredBets(boolean[] insuredBets) {
        this.insuredBets = insuredBets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Deal deal = (Deal) o;

        if (!cards.equals(deal.cards)) return false;
        if (!bets.equals(deal.bets)) return false;
        return points.equals(deal.points);
    }

    @Override
    public int hashCode() {
        int result = cards.hashCode();
        result = 31 * result + bets.hashCode();
        result = 31 * result + points.hashCode();
        return result;
    }
}
