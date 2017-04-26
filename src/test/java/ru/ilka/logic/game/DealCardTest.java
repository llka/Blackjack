package ru.ilka.logic.game;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.ilka.exception.LogicException;
import ru.ilka.logic.GameLogic;
import ru.ilka.logic.LogicResult;

import java.util.ArrayList;


/**
 * Here could be your advertisement +375 29 3880490
 */

public class DealCardTest {

    private ArrayList<Integer> alreadyUsedCards;

    @Before
    public void initAlreadyUsed(){
        alreadyUsedCards = new ArrayList<>();
        for (int i = 1; i < 52; i++) {
            alreadyUsedCards.add(i);
        }
    }

    @Test
    public void dealCardTest() throws LogicException {
        GameLogic gameLogic = new GameLogic(1);
        Assert.assertEquals(LogicResult.CARD_A_S, gameLogic.dealCard(alreadyUsedCards));
    }
}
