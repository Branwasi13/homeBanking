package com.mindhub.homebanking.services.implementation;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImplementation implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void  saveAccount(Account account) {
        accountRepository.save(account);

    }

    @Override
    public List<Account> getAllAccounts(){
        return accountRepository.findAll();
    }

    @Override
    public Account getAccountById(long id){
        return accountRepository.findById(id).get();
    }

    @Override
    public Account findByNumber(String number){
       return accountRepository.findByNumber(number);
    }
}
