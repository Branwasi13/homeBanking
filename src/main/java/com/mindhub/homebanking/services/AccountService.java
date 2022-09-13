package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Account;

import java.util.List;

public interface AccountService {


    public List<Account> getAllAccounts();
    public Account getAccountById(long id);
    public void  saveAccount(Account account);
    public Account findByNumber(String number);
}
