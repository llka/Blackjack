package ru.ilka.logic.game;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.ilka.logic.GameLogic;
import ru.ilka.logic.LogicResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Here could be your advertisement +375 29 3880490
 */
@RunWith(Parameterized.class)
public class CountPointsTest {
    private List<LogicResult> hand = new ArrayList<>();
    private int expectedPoints;

    public CountPointsTest(LogicResult[] hand, int expectedPoints) {
        for (LogicResult card:hand) {
            this.hand.add(card);
        }
        this.expectedPoints = expectedPoints;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {new LogicResult[]{LogicResult.CARD_A_C, LogicResult.CARD_A_D}, 12},
                {new LogicResult[]{LogicResult.CARD_A_C,LogicResult.CARD_10_D,LogicResult.CARD_9_D, LogicResult.CARD_A_D}, 21},
                {new LogicResult[]{LogicResult.CARD_10_C, LogicResult.CARD_A_D}, 21},
                {new LogicResult[]{LogicResult.CARD_10_C, LogicResult.CARD_A_D, LogicResult.CARD_6_C}, 17}
        });
    }

    @Test
    public void countPointsInHandTest(){
        GameLogic gameLogic = new GameLogic(8);
        Assert.assertEquals(expectedPoints,gameLogic.countPointsInHand((ArrayList<LogicResult>) hand));
    }
}
