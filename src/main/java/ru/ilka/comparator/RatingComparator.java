package ru.ilka.comparator;

import ru.ilka.entity.Account;

import java.util.Comparator;

/**
 * Here could be your advertisement +375 29 3880490
 */
public class RatingComparator implements Comparator<Account> {
    @Override
    public int compare(Account o1, Account o2) {
        int rating1 = o1.getRating();
        int rating2 = o2.getRating();
        if(rating1 > rating2){
            return -1;
        }else if(rating1 < rating2){
            return 1;
        }else {
            return 0;
        }
    }
}
