package ru.ilka.logic;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ilka.dao.GameDao;
import ru.ilka.entity.Account;
import ru.ilka.entity.Deal;
import ru.ilka.entity.Game;
import ru.ilka.exception.DBException;
import ru.ilka.exception.LogicException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Here could be your advertisement +375 29 3880490
 */
public class GameLogic {
    static Logger logger = LogManager.getLogger(GameLogic.class);

    private static final int CARDS_QUANTITY = 52;
    private static final int DECKS_QUANTITY = 1;
    private static final int BJ_POINTS = 21;
    private static final int DEALER_LOWER_LIMIT_POINTS = 17;
    private static final String DATE_TIME_REGEX = "yyyy-MM-dd HH:mm:ss";
    private ArrayList<Integer> alreadyUsed;

    public GameLogic() {
        this.alreadyUsed = new ArrayList<>();
    }

    public GameLogic(ArrayList<Integer> alreadyUsed) {
        this.alreadyUsed = alreadyUsed;
        logger.debug("GameLogic alreadyUsed : " + alreadyUsed);
    }

    public ArrayList<ArrayList<LogicResult>> dealCards(boolean[] hands){

        ArrayList<ArrayList<LogicResult>> cards = new ArrayList<>(hands.length);
        for (int i = 0; i < hands.length; i++) {
            ArrayList<LogicResult> hand = new ArrayList<>(4);
            if(hands[i]) {
                if(i == 0){
                    int dealerPoints = 0;
                    while (dealerPoints < DEALER_LOWER_LIMIT_POINTS){
                        try {
                            hand.add(dealCard(alreadyUsed));
                            dealerPoints = countPointsInHand(hand);
                        } catch (LogicException e) {
                            logger.error("Error while dealing cards " + e);
                        }
                    }
                } else {
                    for (int j = 0; j < 2; j++) {
                        try {
                            hand.add(dealCard(alreadyUsed));
                        } catch (LogicException e) {
                            logger.error("Error while dealing cards " + e);
                        }
                    }
                }
            }
            cards.add(hand);
        }
        logger.debug("used cards " + alreadyUsed);
        logger.debug("cards " + cards);
        return cards;
    }

    public LogicResult dealCard(ArrayList<Integer> usedCards) throws LogicException {
        int card = ThreadLocalRandom.current().nextInt(CARDS_QUANTITY * DECKS_QUANTITY);
        if (!usedCards.isEmpty()) {
            for (int k = 0; k < usedCards.size(); k++) {
                if (card == usedCards.get(k)) {
                    card = ThreadLocalRandom.current().nextInt(CARDS_QUANTITY * DECKS_QUANTITY);
                    k = 0;
                }
            }
        }
        usedCards.add(card);
        if (card > CARDS_QUANTITY) {
            card %= CARDS_QUANTITY;
        }
        try {
            return getCard(card);
        } catch (LogicException e) {
            throw new LogicException("Error while generating new random card" + e);
        }
    }

    public void hitCard(int betPlace, Deal deal, Account account, StringBuilder writer){
        try {
            LogicResult card = dealCard(alreadyUsed);
            deal.getCards().get(betPlace).add(card);
            deal.setPoints(countPoints(deal.getCards()));
            writer.append("<div class=\"card" + betPlace + deal.getCards().get(betPlace).size() + "\">\n");
            writeCard(card,writer);
            writePoints(deal.getPoints().get(betPlace),betPlace,writer);
            writer.append("</div>");
            checkForBust(betPlace,deal,account);
        } catch (LogicException e) {
            logger.error("Error while hitting new card " + e);
        }
    }

    public ArrayList<Integer> countPoints(ArrayList<ArrayList<LogicResult>> cards){
        ArrayList<Integer> points = new ArrayList<>(cards.size());
        for (int i = 0; i < cards.size(); i++) {
            int handPoints = countPointsInHand(cards.get(i));
            points.add(handPoints);
        }
        return points;
    }

    public int countPointsInHand(ArrayList<LogicResult> hand){
        int handPoints = 0;
        int aces = 0;
        for (int j = 0; j < hand.size() ; j++) {
            try {
                int cardRank = findCardRank(hand.get(j));
                if(cardRank == 11){
                    aces++;
                }
                handPoints += cardRank;
            } catch (LogicException e) {
                logger.error("Error while counting points " + e);
            }
        }

        while (handPoints > BJ_POINTS && aces > 0){
            handPoints -= 10;
            --aces;
        }

        return handPoints;
    }

    public void checkForBust(int betPlace, Deal deal, Account account){
        int handPoints = deal.getPoints().get(betPlace);
        int dealerPoints = deal.getPoints().get(0);
        ArrayList<Double> bets = deal.getBets();
        try {
            if(handPoints > BJ_POINTS){
                logger.info("Bust in " + betPlace);
                concludeGame(betPlace, LogicResult.BUST, deal, account);
            }else if(handPoints == BJ_POINTS && dealerPoints != BJ_POINTS){
                logger.info("Win in " + betPlace);
                if(deal.getInsuredBets()[betPlace-1]){
                    bets.set(betPlace-1,bets.get(betPlace-1)/2);
                    deal.setBets(bets);
                }
                concludeGame(betPlace, LogicResult.WIN, deal, account);
            }else if(handPoints == BJ_POINTS && dealerPoints == BJ_POINTS){
                logger.info("Draw in " + betPlace);
                if(!deal.getInsuredBets()[betPlace-1]){
                    bets.set(betPlace-1,0.0);
                    deal.setBets(bets);
                }
                concludeGame(betPlace, LogicResult.DRAW, deal, account);
            }
        } catch (LogicException e) {
            logger.error("Error while checking for Bust "+ e);
        }
    }

    public void calculateGameResult(int betPlace, Deal deal, Account account){
        int handPoints = deal.getPoints().get(betPlace);
        int dealerPoints = deal.getPoints().get(0);
        ArrayList<Double> bets = deal.getBets();
        double bet = bets.get(betPlace-1);
        double winCoefficient;
        boolean insuranceSuggested = false;
        boolean insured = deal.getInsuredBets()[betPlace-1];
        boolean bjAvailable = deal.getCards().get(betPlace).size() == 2 ? true : false;
        try {
            insuranceSuggested = findCardRank(deal.getCards().get(0).get(1)) == 11 ? true : false;
        } catch (LogicException e) {
            logger.error("Error while finding card rank in dealer's first card " + e);
        }

        try {
            if(dealerPoints == BJ_POINTS && insuranceSuggested ){
                if(handPoints == BJ_POINTS){
                    if(insured) {
                        concludeGame(betPlace, LogicResult.WIN, deal, account);
                    }else{
                        concludeGame(betPlace, LogicResult.DRAW, deal, account);
                    }
                }else {
                    if (insured) {
                        concludeGame(betPlace, LogicResult.DRAW, deal, account);
                    } else {
                        concludeGame(betPlace, LogicResult.LOOSE, deal, account);
                    }
                }
            }else if(dealerPoints < BJ_POINTS && insuranceSuggested){
                if(handPoints < dealerPoints || handPoints > BJ_POINTS){
                    if(insured) {
                        bets.set(betPlace-1,bet * 1.5);
                        deal.setBets(bets);
                        concludeGame(betPlace, LogicResult.LOOSE, deal, account);
                    }else {
                        concludeGame(betPlace, LogicResult.LOOSE, deal, account);
                    }
                }else if (handPoints == dealerPoints){
                    if(insured) {
                        bets.set(betPlace-1,bet * 0.5);
                        deal.setBets(bets);
                        concludeGame(betPlace, LogicResult.LOOSE, deal, account);
                    }else {
                        concludeGame(betPlace, LogicResult.DRAW, deal, account);
                    }
                }else if(handPoints == BJ_POINTS && bjAvailable){
                    if(insured){
                        concludeGame(betPlace, LogicResult.WIN, deal, account);
                    }else {
                        concludeGame(betPlace, LogicResult.BJ_WIN, deal, account);
                    }
                }else if(handPoints > dealerPoints && handPoints <= BJ_POINTS){
                    if (insured){
                        bets.set(betPlace-1,bet * 0.5);
                        deal.setBets(bets);
                        concludeGame(betPlace, LogicResult.WIN, deal, account);
                    }else {
                        concludeGame(betPlace, LogicResult.WIN, deal, account);
                    }
                }
            }else if(dealerPoints > BJ_POINTS && insuranceSuggested){
                if(handPoints == BJ_POINTS && bjAvailable){
                    if(insured){
                        concludeGame(betPlace, LogicResult.WIN, deal, account);
                    }else {
                        concludeGame(betPlace, LogicResult.BJ_WIN, deal, account);
                    }
                }else if(handPoints <= BJ_POINTS){
                    if(insured){
                        bets.set(betPlace-1,bet * 0.5);
                        deal.setBets(bets);
                        concludeGame(betPlace, LogicResult.WIN, deal, account);
                    }else {
                        concludeGame(betPlace, LogicResult.WIN, deal, account);
                    }
                }else{
                    if(insured){
                        bets.set(betPlace-1,bet * 1.5);
                        deal.setBets(bets);
                        concludeGame(betPlace, LogicResult.LOOSE, deal, account);
                    }else {
                        concludeGame(betPlace, LogicResult.LOOSE, deal, account);
                    }
                }
            }else if(dealerPoints <= BJ_POINTS && !insuranceSuggested){
                if (handPoints < dealerPoints || handPoints > BJ_POINTS){
                    concludeGame(betPlace, LogicResult.LOOSE, deal, account);
                }else if(handPoints == dealerPoints) {
                    concludeGame(betPlace, LogicResult.DRAW, deal, account);
                }else if(handPoints == BJ_POINTS && bjAvailable){
                    concludeGame(betPlace,LogicResult.BJ_WIN,deal,account);
                }else if(handPoints > dealerPoints && handPoints <= BJ_POINTS) {
                    concludeGame(betPlace, LogicResult.WIN, deal, account);
                }
            }else if(dealerPoints > BJ_POINTS && !insuranceSuggested){
                if (handPoints > BJ_POINTS){
                    concludeGame(betPlace, LogicResult.LOOSE, deal, account);
                }else if(handPoints == BJ_POINTS && bjAvailable){
                    concludeGame(betPlace, LogicResult.BJ_WIN, deal, account);
                }else {
                    concludeGame(betPlace, LogicResult.WIN, deal, account);
                }
            }
        }catch (LogicException e) {
            logger.error("Error while calculating game result "+ e);
        }
    }

    public void concludeGame(int betPlace, LogicResult result, Deal deal, Account account) throws LogicException {
        GameDao gameDao = new GameDao();
        AccountLogic accountLogic = new AccountLogic();
        StatisticsLogic statisticsLogic = new StatisticsLogic();
        LogicResult[] dealReport = deal.getDealReport();
        Game game = new Game();

        int accountId = account.getAccountId();
        int handsPlayed = account.getHandsPlayed() + 1;
        int handsWon = account.getHandsWon();
        BigDecimal moneySpend = account.getMoneySpend();
        BigDecimal moneyWon = account.getMoneyWon();
        BigDecimal balance = account.getBalance();
        int rating;

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern(DATE_TIME_REGEX);
        String currentTime = now.format(format);
        game.setTime(currentTime);
        game.setBet(deal.getBets().get(betPlace - 1));
        game.setPoints(deal.getPoints().get(betPlace));
        game.setPlayerAccountId(account.getAccountId());
        game.setPlayerIsDealer(false);
        switch (result) {
            case BUST:
                game.setWinCoefficient(-1);
                game.setPlayerWin(false);
                moneySpend = moneySpend.add(new BigDecimal(game.getBet()));
                balance = balance.subtract(new BigDecimal(game.getBet()));
                dealReport[betPlace] = LogicResult.BUST;
                logger.debug("deal report " + dealReport[betPlace]);
                break;
            case LOOSE:
                game.setWinCoefficient(-1);
                game.setPlayerWin(false);
                moneySpend = moneySpend.add(new BigDecimal(game.getBet()));
                balance = balance.subtract(new BigDecimal(game.getBet()));
                dealReport[betPlace] = LogicResult.LOOSE;
                logger.debug("deal report " + dealReport[betPlace]);
                break;
            case WIN:
                game.setWinCoefficient(1);
                game.setPlayerWin(true);
                moneyWon = moneyWon.add(new BigDecimal(game.getBet()));
                balance = balance.add(new BigDecimal(game.getBet()));
                handsWon++;
                dealReport[betPlace] = LogicResult.WIN;
                logger.debug("deal report " + dealReport[betPlace]);
                break;
            case BJ_WIN:
                game.setWinCoefficient(1.5);
                game.setPlayerWin(true);
                moneyWon = moneyWon.add(new BigDecimal(game.getBet() * 1.5));
                balance = balance.add(new BigDecimal(game.getBet() * 1.5));
                handsWon++;
                dealReport[betPlace] = LogicResult.BJ_WIN;
                logger.debug("deal report " + dealReport[betPlace]);
                break;
            case DRAW:
                game.setWinCoefficient(0);
                game.setPlayerWin(false);
                dealReport[betPlace] = LogicResult.DRAW;
                logger.debug("deal report " + dealReport[betPlace]);
                break;
            default:
                throw new LogicException("Unknown game result");
        }
        account.setHandsPlayed(handsPlayed);
        account.setMoneySpend(moneySpend);
        account.setMoneyWon(moneyWon);
        account.setHandsWon(handsWon);
        account.setBalance(balance);
        rating = statisticsLogic.calculateRating(account);
        account.setRating(rating);

        deal.getBets().set(betPlace - 1, 0.0);
        deal.getCards().set(betPlace, new ArrayList<>());
        deal.getPoints().set(betPlace, 0);
        deal.getInsuredBets()[betPlace - 1] = false;
        deal.setDealReport(dealReport);

        try {
            gameDao.registerGame(account.getAccountId(), game);
        } catch (DBException e) {
            throw new LogicException("Error in game registration" + e);
        }
        try {
            accountLogic.changeStatistics(accountId, handsPlayed, handsWon, moneySpend, moneyWon, rating);
        } catch (LogicException e) {
            throw new LogicException("Error in changing statistics" + e);
        }
        try {
            accountLogic.changeBalance(accountId, balance);
        } catch (LogicException e) {
            throw new LogicException("Error in changing balance" + e);
        }
    }

    public boolean isUserInGame(Deal deal){
        boolean inGame = false;
        ArrayList<Double> bets = deal.getBets();
        for (int i = 0; i < bets.size(); i++) {

            if(bets.get(i) > 0){
                inGame = true;
            }
        }
        logger.debug("isUserInGame " + inGame);
        return inGame;
    }

    public void checkForInsurance(Deal deal, StringBuilder writer){
        ArrayList<ArrayList<LogicResult>> cards = deal.getCards();
        ArrayList<Double> bets = deal.getBets();
        try {
            if(findCardRank(cards.get(0).get(1)) == 11) {
                suggestInsurance(bets,writer);
            }else {
                suggestActionButtons(deal,writer);
            }
        } catch (LogicException e) {
            logger.error("Error while checking if dealer has black jack " + e);
        }
    }

    public void suggestInsurance(ArrayList<Double> bets, StringBuilder writer){
        writer.append("<div class=\"insuranceButtons\" id = \"insuranceButtons\">\n");
        for (int i = 1; i < bets.size() + 1 ; i++) {
            if(bets.get(i-1) > 0){
                writer.append("<div class=\"actionButtons\">\n");
                writer.append("<button class=\"gameButton\" onclick=\"insure("+ i +")\" id=\"insure" + i + "\">Insure</button>\n");
                writer.append("<button class=\"gameButton\" onclick=\"insureNot("+ i +")\" id=\"insureNot" + i + "\">Do not</button>\n");
            }else {
                writer.append("<div class=\"actionButtons\" style=\"visibility: hidden\">\n");
                writer.append("<button class=\"gameButton\" style=\"visibility: hidden\" id=\"insure" + i + "\">Insure</button>\n");
                writer.append("<button class=\"gameButton\" style=\"visibility: hidden\" id=\"insureNot" + i + "\">Do not</button>\n");
            }
            writer.append("</div>\n");
        }
        writer.append("</div>\n");
    }

    public void suggestActionButtons(Deal deal, StringBuilder writer){
        ArrayList<Integer> points = deal.getPoints();
        ArrayList<ArrayList<LogicResult>> cards = deal.getCards();
        writer.append("<div class=\"actionButtonsRow\">\n");
        for (int i = 1; i < points.size(); i++) {
            if(points.get(i) > 0 && points.get(i) < BJ_POINTS){
                writer.append("<div class=\"actionButtons\"  id=\"actionButtons"+ i +"\">\n");
                writer.append("<button class=\"gameButton\" onclick=\"hit(" + i + ")\" id=\"hit"+ i +"\">Hit</button>\n");
                writer.append("<button class=\"gameButton\" onclick=\"stand(" + i + ")\" id=\"stand"+ i +"\">Stand</button>\n");
                writer.append("</div>\n");
            }else if(points.get(i) == BJ_POINTS && cards.get(i).size() == 2) {
                writer.append("<div class=\"actionButtons\" id=\"actionButtons" + i + "\">\n");
                writer.append("<button class=\"gameButton\" onclick=\"immediateBjWin(" + i + ")\" id=\"insuredBjWin" + i + "\">Insured Win 1/1</button>\n");
                writer.append("<button class=\"gameButton\" onclick=\"waitBjWin(" + i + ")\" id=\"waitBjWin" + i + "\">Wait for Win 3/2</button>\n");
                writer.append("</div>\n");
            }else {
                writer.append("<div class=\"actionButtons\" style=\"visibility: hidden\" id=\"actionButtons"+ i +"\">\n");
                writer.append("</div>\n");
            }
        }
        writer.append("</div>\n");
    }

    public void suggestNewGame(StringBuilder writer){
        writer.append("<div class=\"submit\">\n");
        writer.append("<form id=\"newGameForm\" method=\"POST\" action=\"/controller\">\n");
        writer.append("<input type=\"hidden\" name=\"command\" value=\"newGame\"/>");
        writer.append("<input class=\"button\" type=\"submit\" value=\"New Game\">\n");
        writer.append("</form>");
        writer.append("</div>\n");
    }

    public void writeDealerCards(Deal deal, StringBuilder writer) throws LogicException {
        ArrayList<ArrayList<LogicResult>> cards = deal.getCards();
        writer.append("<div class=\"DealerCard1\">\n");
        writeCard(cards.get(0).get(0), writer);
        writePoints(deal.getPoints().get(0),0,writer);
        writer.append("</div>\n");

        if(cards.get(0).size() > 2) {
            for (int i = 3; i < cards.get(0).size() + 1; i++) {
                writer.append("<div class=\"DealerCard" + i + "\">\n");
                writeCard(cards.get(0).get(i-1), writer);
                writer.append("</div>");
            }
        }

        LogicResult[] dealReport = deal.getDealReport();
        writer.append("<div class=\"dealReport\">\n");
        for (int i = 1; i < 4; i++) {
            String betClass;
            String message;
            switch (dealReport[i]){
                case LOOSE:
                    betClass = "betLoose";
                    message = "Loss";
                    break;
                case DRAW:
                    betClass = "betDraw";
                    message = "Draw";
                    break;
                case WIN:
                    betClass = "betWin";
                    message = "Win!";
                    break;
                case BJ_WIN:
                    betClass = "betWin";
                    message = "Black Jack!";
                    break;
                case BUST:
                    betClass = "betLoose";
                    message = "Bust";
                    break;
                case EMPTY_BET:
                    betClass = "betDraw";
                    message = "";
                    break;
                default:
                    throw new LogicException("Unknown game result achieved in report");
            }
            writer.append("<div class=\"" + betClass + "\">");
            writer.append(message);
            writer.append("</div>");
        }
        writer.append("</div>\n");
    }

    public void showFirstCards(Deal deal, StringBuilder writer){
        ArrayList<ArrayList<LogicResult>> cards = deal.getCards();
        ArrayList<Integer> points = deal.getPoints();

        writer.append("<div class=\"DealerCard1\">\n");
        writer.append("<div class=\"cardBack\">");
        writer.append("</div>\n");
        writer.append("</div>\n");

        writer.append("<div class=\"DealerCard2\">\n");
        writeCard(cards.get(0).get(1), writer);
        writer.append("</div>");

        for (int i = 1; i < cards.size(); i++) {
            if (!cards.get(i).isEmpty()) {
                for (int j = 1; j < 3; j++) {
                    writer.append("<div class=\"card" + i + j + "\">\n");
                    writeCard(cards.get(i).get(j - 1), writer);
                    if (j == 2) {
                        writePoints(points.get(i),i,writer);
                    }
                    writer.append("</div>");
                }
            }
        }
    }

    public void writeCard(LogicResult resultCard, StringBuilder writer){
        String card = resultCard.toString();
        char suit = card.charAt(card.length()-1);
        String suitClass;
        String number;
        if(card.length() == 9){
            number = "10";
        }else {
            number = String.valueOf(card.charAt(card.length()-3));
        }
        switch (suit){
            case 'H':
                suitClass = "<div class=\"hearts\"></div>";
                break;
            case 'C':
                suitClass = "<div class=\"clubs\"></div>";
                break;
            case 'D':
                suitClass = "<div class=\"diamonds\"></div>";
                break;
            case 'S':
                suitClass = "<div class=\"spades\"></div>";
                break;
            default:
                suitClass = "<div class=\"spades\"></div>";
        }
        writer.append("<div class=\"cardNumber\">");
        writer.append(number);
        writer.append("</div>\n");
        writer.append("<div class=\"cardSuit\">");
        writer.append(suitClass);
        writer.append("</div>\n");
        writer.append("<div class=\"cardNumberDown\">");
        writer.append(number);
        writer.append("</div>\n");
    }

    public void writePoints(int points,int betPlace, StringBuilder writer){
        writer.append("<div class=\"points\" id=\"points" + betPlace + "\">");
        writer.append(points);
        writer.append("</div>\n");
    }

    public ArrayList<Integer> getAlreadyUsed() {
        return alreadyUsed;
    }

    public void setAlreadyUsed(ArrayList<Integer> alreadyUsed) {
        this.alreadyUsed = alreadyUsed;
    }

    private int findCardRank(LogicResult resultCard) throws LogicException {
        String card = resultCard.toString();
        if(card.length() == 9){
            return 10;
        }else {
            String realRank = String.valueOf(card.charAt(card.length()-3));
            try{
                int rank = Integer.parseInt(realRank);
                return rank;
            }catch(NumberFormatException e){
                switch (realRank){
                    case "J":
                    case "Q":
                    case "K":
                        return 10;
                    case "A":
                        return 11;
                    default:
                        throw new LogicException("Can't find card rank in " + resultCard);
                }
            }
        }
    }

    private LogicResult getCard(int number) throws LogicException {
        switch (number){
            case 1:
                return LogicResult.CARD_2_H;
            case 2:
                return LogicResult.CARD_2_C;
            case 3:
                return LogicResult.CARD_2_D;
            case 4:
                return LogicResult.CARD_2_S;
            case 5:
                return LogicResult.CARD_3_H;
            case 6:
                return LogicResult.CARD_3_C;
            case 7:
                return LogicResult.CARD_3_D;
            case 8:
                return LogicResult.CARD_3_S;
            case 9:
                return LogicResult.CARD_4_H;
            case 10:
                return LogicResult.CARD_4_C;
            case 11:
                return LogicResult.CARD_4_D;
            case 12:
                return LogicResult.CARD_4_S;
            case 13:
                return LogicResult.CARD_5_H;
            case 14:
                return LogicResult.CARD_5_C;
            case 15:
                return LogicResult.CARD_5_D;
            case 16:
                return LogicResult.CARD_5_S;
            case 17:
                return LogicResult.CARD_6_H;
            case 18:
                return LogicResult.CARD_6_C;
            case 19:
                return LogicResult.CARD_6_D;
            case 20:
                return LogicResult.CARD_6_S;
            case 21:
                return LogicResult.CARD_7_H;
            case 22:
                return LogicResult.CARD_7_C;
            case 23:
                return LogicResult.CARD_7_D;
            case 24:
                return LogicResult.CARD_7_S;
            case 25:
                return LogicResult.CARD_8_H;
            case 26:
                return LogicResult.CARD_8_C;
            case 27:
                return LogicResult.CARD_8_D;
            case 28:
                return LogicResult.CARD_8_S;
            case 29:
                return LogicResult.CARD_9_H;
            case 30:
                return LogicResult.CARD_9_C;
            case 31:
                return LogicResult.CARD_9_D;
            case 32:
                return LogicResult.CARD_9_S;
            case 33:
                return LogicResult.CARD_10_H;
            case 34:
                return LogicResult.CARD_10_C;
            case 35:
                return LogicResult.CARD_10_D;
            case 36:
                return LogicResult.CARD_10_S;
            case 37:
                return LogicResult.CARD_J_H;
            case 38:
                return LogicResult.CARD_J_C;
            case 39:
                return LogicResult.CARD_J_D;
            case 40:
                return LogicResult.CARD_J_S;
            case 41:
                return LogicResult.CARD_Q_H;
            case 42:
                return LogicResult.CARD_Q_C;
            case 43:
                return LogicResult.CARD_Q_D;
            case 44:
                return LogicResult.CARD_Q_S;
            case 45:
                return LogicResult.CARD_K_H;
            case 46:
                return LogicResult.CARD_K_C;
            case 47:
                return LogicResult.CARD_K_D;
            case 48:
                return LogicResult.CARD_K_S;
            case 49:
                return LogicResult.CARD_A_H;
            case 50:
                return LogicResult.CARD_A_C;
            case 51:
                return LogicResult.CARD_A_D;
            case 52:
            case 0:
                return LogicResult.CARD_A_S;
            default:
                throw new LogicException("Can't get card " + number);
        }
    }
}
