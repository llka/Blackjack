package ru.ilka.comparator;

import ru.ilka.entity.Account;

import java.util.Comparator;

/**
 * Compares accounts by hands won quantity
 */
public class HandsWonComparator implements Comparator<Account> {
    @Override
    public int compare(Account o1, Account o2) {
        int handsWon1 = o1.getHandsWon();
        int handsWon2 = o2.getHandsWon();

        if(handsWon1 > handsWon2){
            return -1;
        }else if(handsWon1 < handsWon2){
            return 1;
        }else {
            return 0;
        }
    }
}
