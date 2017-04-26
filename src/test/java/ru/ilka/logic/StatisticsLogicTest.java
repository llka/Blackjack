package ru.ilka.logic;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.ilka.entity.Account;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;

/**
 * Here could be your advertisement +375 29 3880490
 */
@RunWith(Parameterized.class)
public class StatisticsLogicTest {

    private Account account;
    private int handsPlayed;
    private int handsWon;
    private double won;
    private double spent;
    private int expectedRating;

    public StatisticsLogicTest(int handsPlayed, int handsWon, double won, double spent, int rating) {
        this.handsPlayed = handsPlayed;
        this.handsWon = handsWon;
        this.won = won;
        this.spent = spent;
        this.expectedRating = rating;
    }

    @Parameterized.Parameters(name = "{index}: hands won - {1}, rating - {4}")
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][] {
                {0,0,0,0,0},
                {100,0,0,100,0},
                {100,5,5,95,2},
                {100,50,50,50,539},
                {100,95,95,5,16545}
        });
    }

    @Before
    public void initAccount(){
        account = new Account();
        account.setHandsPlayed(handsPlayed);
        account.setHandsWon(handsWon);
        account.setMoneyWon(new BigDecimal(won));
        account.setMoneySpend(new BigDecimal(spent));
    }

    @Test
    public void testAccountRating(){
        StatisticsLogic statisticsLogic = new StatisticsLogic();
        Assert.assertEquals(expectedRating,statisticsLogic.calculateRating(account));
    }
}
