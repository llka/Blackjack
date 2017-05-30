package ru.ilka.logic;

import ru.ilka.entity.Account;

/**
 * StatisticsLogic class provides functionality for calculating static's params.
 * @since %G%
 * @version %I%
 */
public class StatisticsLogic {

    private static final int MONEY_COEFF = 11;
    private static final int PERCENTS = 100;

    public StatisticsLogic() {
    }

    /**
     * Calculates player's rating according to his achievements.
     * @param account player's account.
     * @return rating number.
     */
    public int calculateRating(Account account){
        int rating = 0;
        if(account.getHandsPlayed() > 0) {
            double moneyWonCoeff = MONEY_COEFF * account.getMoneyWon().doubleValue() / (account.getMoneySpend().doubleValue() + 1);
            rating +=  moneyWonCoeff * PERCENTS * account.getHandsWon()/account.getHandsPlayed();
        }
        return rating;
    }
}
