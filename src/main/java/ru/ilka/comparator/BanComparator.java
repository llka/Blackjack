package ru.ilka.comparator;

import ru.ilka.entity.Account;

import java.util.Comparator;

/**
 * Here could be your advertisement +375 29 3880490
 */
public class BanComparator implements Comparator<Account>{
    @Override
    public int compare(Account o1, Account o2) {
        boolean ban1 = o1.isBan();
        boolean ban2 = o2.isBan();
        if(ban1 && !ban2){
            return -1;
        }else if(!ban1 && ban2){
            return 1;
        }
        else {
            return 0;
        }
    }
}
