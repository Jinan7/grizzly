package com.undefinedbehaviourgames.grizzly;

import java.util.ArrayList;
import java.util.List;

public class AccountLab {

    private List<Account> mAccounts;
    private static AccountLab sAccountLab;
    private AccountLab () {
        mAccounts = new ArrayList<>();
    }

    public static AccountLab get() {

        if (sAccountLab == null) {
            sAccountLab = new AccountLab();
        }

        return sAccountLab;
    }

    public List<Account> getAccounts() {
        return mAccounts;
    }

    public void add(Account account) {
        mAccounts.add(account);
    }

}
