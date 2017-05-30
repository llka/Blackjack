package ru.ilka.comparator;

import ru.ilka.entity.Account;

import java.math.BigDecimal;
import java.util.Comparator;

/**
 * Compares accounts by lost money amount
 */
public class ProfitComparator implements Comparator<Account> {
    @Override
    public int compare(Account o1, Account o2) {
        BigDecimal benefit1 = o1.getMoneySpend().subtract(o1.getMoneyWon());
        BigDecimal benefit2 = o2.getMoneySpend().subtract(o2.getMoneyWon());
        return benefit1.compareTo(benefit2);
    }
}
