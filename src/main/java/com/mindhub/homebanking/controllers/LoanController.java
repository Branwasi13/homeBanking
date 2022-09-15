package com.mindhub.homebanking.controllers;



import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.models.TransactionType.CREDIT;
import static java.util.stream.Collectors.toList;


@RestController
public class LoanController {
    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientLoanService clientLoanService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private LoanService loanService;

    @Transactional
    @PostMapping("/api/loans")
    public ResponseEntity<Object> addLoan(@RequestBody LoanApplicationDTO loanApplicationDTO ,Authentication authentication) {

        Client currentClient = clientService.getClientByEmail(authentication.getName());

        Account destinyAccount = accountService.findByNumber(loanApplicationDTO.getDestinyAccount());

        Loan loanId = loanService.findLoanById(loanApplicationDTO.getId());

        if(loanApplicationDTO.getAmount().isNaN() || loanApplicationDTO.getPayments() == null || loanApplicationDTO.getDestinyAccount().isEmpty()){
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (loanApplicationDTO.getAmount() <= 0) {
            return new ResponseEntity<>("Missing data Amount", HttpStatus.FORBIDDEN);
        }

        if(destinyAccount == null){
            return new ResponseEntity<>("account dosen't exist", HttpStatus.FORBIDDEN);
        }

        if (loanId == null) {
            return new ResponseEntity<>("The type of loan does not exist", HttpStatus.FORBIDDEN);
        }

        if(loanApplicationDTO.getAmount() > loanId.getMaxAmount()){
            return new ResponseEntity<>("Amount limit exceeded", HttpStatus.FORBIDDEN);
        }

        if(!loanId.getPayments().contains(loanApplicationDTO.getPayments())){
            return new ResponseEntity<>("Payments doesnt allowed", HttpStatus.FORBIDDEN);
        }

        if(!currentClient.getAccounts().contains(destinyAccount)){
            return new ResponseEntity<>("Destiny account dosent match with client user", HttpStatus.FORBIDDEN);
        }

        if (currentClient.getClientLoans().stream().anyMatch(clientLoan -> clientLoan.getLoan() == loanId)) {
            return new ResponseEntity<>("You already have an active "+ loanId.getName(), HttpStatus.FORBIDDEN);
        }

        Double balanceCredit = destinyAccount.getBalance() + loanApplicationDTO.getAmount();

        Transaction creditTransaction = new Transaction(CREDIT,loanApplicationDTO.getAmount(),loanId.getName()+ " loan approved", LocalDateTime.now(),destinyAccount,balanceCredit);

        transactionService.saveTransaction(creditTransaction);

        destinyAccount.setBalance(destinyAccount.getBalance() + loanApplicationDTO.getAmount());

        ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.getAmount(), loanApplicationDTO.getPayments(),currentClient,loanId);

        switch (loanId.getName()){
            case "Personal":
                switch (clientLoan.getPayments()){
                    case 6: clientLoan.setAmount(clientLoan.getAmount() * 1.20);
                        break;
                    case 12: clientLoan.setAmount(clientLoan.getAmount() * 1.30);
                        break;
                    case 24: clientLoan.setAmount(clientLoan.getAmount() * 1.40);
                        break;
                }
                break;
            case "Hipotecario":
                switch (clientLoan.getPayments()){
                    case 12: clientLoan.setAmount(clientLoan.getAmount() * 1.30);
                        break;
                    case 24: clientLoan.setAmount(clientLoan.getAmount() * 1.40);
                        break;
                    case 36: clientLoan.setAmount(clientLoan.getAmount() * 1.45);
                        break;
                    case 48: clientLoan.setAmount(clientLoan.getAmount() * 1.50);
                        break;
                    case 60: clientLoan.setAmount(clientLoan.getAmount() * 1.55);
                        break;
                }
                break;
            case "Automotriz":
                switch (clientLoan.getPayments()) {
                    case 6:
                        clientLoan.setAmount(clientLoan.getAmount() * 1.20);
                        break;
                    case 12:
                        clientLoan.setAmount(clientLoan.getAmount() * 1.30);
                        break;
                    case 24:
                        clientLoan.setAmount(clientLoan.getAmount() * 1.40);
                        break;
                    case 36:
                        clientLoan.setAmount(clientLoan.getAmount() * 1.45);
                        break;
                }
                break;
            default:
                break;

        }
        clientLoanService.saveClientLoan(clientLoan);

        return new ResponseEntity<>("Loan approved",HttpStatus.CREATED);
    }

    @GetMapping("/api/loans")
    public List<LoanDTO> getLoans(){
        return loanService.getAllLoans().stream().map(LoanDTO::new).collect(toList());
    }

    @PostMapping("/api/admin/loans")
    public ResponseEntity<String> addAdminLoan (@RequestParam String name,@RequestParam double maxAmount, @RequestParam List<Integer> payments, Authentication authentication){

        Client adminAuthentication = clientService.getClientByEmail(authentication.getName());
        if(adminAuthentication == null){
            return new ResponseEntity<>("missing admin authentication", HttpStatus.FORBIDDEN);
        }

        if(!adminAuthentication.getEmail().contains("@admin.com")){
            return new ResponseEntity<>("you do not have the admin role", HttpStatus.FORBIDDEN);
        }

        if(name.isEmpty() || maxAmount <= 0 || payments.isEmpty()){
            return new ResponseEntity<>("Missing Data",HttpStatus.FORBIDDEN);
        }

        if(loanService.getAllLoans().stream().map(x -> x.getName()).collect(Collectors.toSet()).contains(name)){
            return new ResponseEntity<>("same name of previous loan", HttpStatus.FORBIDDEN);
        }

        loanService.saveLoan(new Loan(name, maxAmount,payments));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
