package ru.ilka.logic;

import ru.ilka.entity.Account;

import java.math.BigDecimal;

/**
 * Here could be your advertisement +375 29 3880490
 */
public class StatisticsLogic {

    public StatisticsLogic() {
    }

    public int calculateRating(Account account){
        int rating = 0;
        if(account.getHandsPlayed() > 0) {
            double moneyWonCoeff = (account.getMoneyWon().divide((account.getMoneySpend()).add(BigDecimal.ONE))).intValue();
            //int moneyWonCoeff = account.getMoneyWon().subtract(account.getMoneySpend()).intValue();
            rating +=  moneyWonCoeff * 100 * account.getHandsWon()/account.getHandsPlayed();
        }
        return rating;
    }
}
