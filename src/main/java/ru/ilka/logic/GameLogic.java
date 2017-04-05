package ru.ilka.logic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Here could be your advertisement +375 29 3880490
 */
public class GameLogic {
    static Logger logger = LogManager.getLogger(GameLogic.class);
    private static final int CARDS_QUANTITY = 52;

    public GameLogic() {}

    public List<LogicResult> dealFirstCards(int hands, int decks){
        int FIRST_CARDS_QNT = hands*2 + 2;
        ArrayList<LogicResult> result = new ArrayList<>(FIRST_CARDS_QNT);
        for (int i = 0; i < FIRST_CARDS_QNT ; i++) {
            int card = ThreadLocalRandom.current().nextInt(CARDS_QUANTITY*decks);
            if(decks > 1 && card > CARDS_QUANTITY){
                card %= CARDS_QUANTITY;
            }
            result.add(getCard(card));
        }
        logger.debug(result);
        return Collections.unmodifiableList(result);
    }

    private LogicResult getCard(int number){
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
                return LogicResult.CARD_A_S;
            default:
                return LogicResult.FAILED;
        }
    }
}
