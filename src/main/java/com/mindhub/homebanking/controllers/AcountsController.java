package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;


@RestController
@RequestMapping("/api")
public class AcountsController {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @GetMapping("/account")
    public List<AccountDTO> getAccounts(){
       return accountService.getAllAccounts().stream().map(AccountDTO::new).collect(toList());
    }

    @GetMapping("/account/{id}")
    public AccountDTO getAccount(@PathVariable long id){
        return new AccountDTO(accountService.getAccountById(id));
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(@RequestParam AccountType accountType,Authentication authentication){

        String accountNumber = CardUtils.getAccountNumber();

        Client client = clientService.getClientByEmail(authentication.getName());

        if (client.getAccounts().stream().filter(Account::getAccountState).count() >= 3) {
            return new ResponseEntity<>("You can´t create more accounts", HttpStatus.FORBIDDEN);
        }

        accountService.saveAccount(new Account(accountNumber,0.0, LocalDateTime.now(),client, accountType,true));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/clients/current/accounts/delete")
    public ResponseEntity<Object> deleteAccount(@RequestParam long accountId,Authentication authentication){

        Account accountNumber = accountService.getAccountById(accountId);

        Client client = clientService.getClientByEmail(authentication.getName());

        if (accountNumber.getBalance() > 0) {
            return new ResponseEntity<>("you cannot delete this account because it have money", HttpStatus.FORBIDDEN);
        }

        accountNumber.setAccountState(false);

        accountService.saveAccount(accountNumber);

        return new ResponseEntity<>("your account is delete",HttpStatus.CREATED);
    }


}
