package ru.ilka.logic;

import ru.ilka.entity.Account;

import java.math.BigDecimal;

/**
 * Here could be your advertisement +375 29 3880490
 */
public class StatisticsLogic {

    private static final int MONEY_COEFF = 11;
    private static final int PERCENTS = 100;

    public StatisticsLogic() {
    }

    public int calculateRating(Account account){
        int rating = 0;
        if(account.getHandsPlayed() > 0) {
            double moneyWonCoeff = MONEY_COEFF * account.getMoneyWon().doubleValue() / (account.getMoneySpend().doubleValue()+1);
            //int moneyWonCoeff = account.getMoneyWon().subtract(account.getMoneySpend()).intValue();
            rating +=  moneyWonCoeff * PERCENTS * account.getHandsWon()/account.getHandsPlayed();
        }
        return rating;
    }
}
