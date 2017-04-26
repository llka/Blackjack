package ru.ilka.logic.game;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.ilka.entity.Deal;
import ru.ilka.logic.GameLogic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Here could be your advertisement +375 29 3880490
 */
@RunWith(Parameterized.class)
public class InGameTest {

    private Deal deal = new Deal();
    private boolean expectedResult;

    public InGameTest(Double[] bets, boolean expectedResult) {
        ArrayList<Double> betsList = new ArrayList<>();
        betsList.addAll(Arrays.asList(bets));
        deal.setBets(betsList);
        this.expectedResult = expectedResult;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {new Double[]{1.0,1.0,1.0}, true},
                {new Double[]{0.0,0.0,1.0}, true},
                {new Double[]{0.0,0.0,0.1}, false},
                {new Double[]{0.0,0.0,0.0}, false}
        });
    }

    @Test
    public void isUserInGameTest(){
        GameLogic gameLogic = new GameLogic(1);
        Assert.assertEquals(expectedResult,gameLogic.isUserInGame(deal));
    }
}
