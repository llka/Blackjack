package ru.ilka.entity;

import ru.ilka.logic.LogicResult;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Deal is a class describing each card game details
 * @see Game
 * @since %G%
 * @version %I%
 */
public class Deal {
    private static final int BETS_QNT = 3;
    private ArrayList<ArrayList<LogicResult>> cards;
    private ArrayList<Double> bets;
    private ArrayList<Integer> points;
    private boolean[] insuredBets;
    private LogicResult[] dealReport;
    private ArrayList<Integer> alreadyUsedCards;

    public Deal(){
        this.insuredBets = new boolean[BETS_QNT];
        this.dealReport = new LogicResult[BETS_QNT + 1];
        for (int i = 0; i < BETS_QNT + 1; i++) {
            this.dealReport[i] = LogicResult.EMPTY_BET;
        }
    }

    public Deal(ArrayList<ArrayList<LogicResult>> cards, ArrayList<Double> bets, ArrayList<Integer> points) {
        this.cards = cards;
        this.bets = bets;
        this.points = points;
        this.insuredBets = new boolean[BETS_QNT];
        this.dealReport = new LogicResult[BETS_QNT + 1];
        for (int i = 0; i < BETS_QNT + 1; i++) {
            this.dealReport[i] = LogicResult.EMPTY_BET;
        }
    }

    public ArrayList<ArrayList<LogicResult>> getCards() {
        return cards;
    }

    public void setCards(ArrayList<ArrayList<LogicResult>> cards) {
        this.cards = cards;
    }

    public ArrayList<Double> getBets() {
        return bets;
    }

    public void setBets(ArrayList<Double> bets) {
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

    public LogicResult[] getDealReport() {
        return dealReport;
    }

    public void setDealReport(LogicResult[] dealReport) {
        this.dealReport = dealReport;
    }

    public ArrayList<Integer> getAlreadyUsedCards() {
        return alreadyUsedCards;
    }

    public void setAlreadyUsedCards(ArrayList<Integer> alreadyUsedCards) {
        this.alreadyUsedCards = alreadyUsedCards;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Deal deal = (Deal) o;

        if (!cards.equals(deal.cards)) return false;
        if (!bets.equals(deal.bets)) return false;
        if (!points.equals(deal.points)) return false;
        return Arrays.equals(insuredBets, deal.insuredBets);
    }

    @Override
    public int hashCode() {
        int result = cards.hashCode();
        result = 31 * result + bets.hashCode();
        result = 31 * result + points.hashCode();
        result = 31 * result + Arrays.hashCode(insuredBets);
        return result;
    }
}
