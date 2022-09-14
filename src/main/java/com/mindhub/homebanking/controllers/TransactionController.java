package com.mindhub.homebanking.controllers;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mindhub.homebanking.dtos.FilteredTransationDTO;
import com.mindhub.homebanking.dtos.PaymentsDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import static com.mindhub.homebanking.models.TransactionType.CREDIT;
import static com.mindhub.homebanking.models.TransactionType.DEBIT;


@RestController
public class TransactionController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private CardService cardService;


    @Transactional
    @PostMapping("/api/clients/current/accounts/transactions")
    public ResponseEntity<Object> newTransaction(@RequestParam Double amount, @RequestParam String description, @RequestParam String originAccount, @RequestParam String destinyAccount, Authentication authentication) {


        Account newOriginAccount = accountService.findByNumber(originAccount);
        Account newDestinyAccount = accountService.findByNumber(destinyAccount);

        if (amount <= 0) {
            return new ResponseEntity<>("Invalid amount", HttpStatus.FORBIDDEN);
        }
        if ( description.isEmpty() || originAccount.isEmpty() || destinyAccount.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (originAccount.equals(destinyAccount)) {
            return new ResponseEntity<>("Same account´s", HttpStatus.FORBIDDEN);
        }
        if (newOriginAccount == null) {
            return new ResponseEntity<>("Origin account doesn´t exist", HttpStatus.FORBIDDEN);
        }
        if (newDestinyAccount == null) {
            return new ResponseEntity<>("Destiny account doesn´t exist", HttpStatus.FORBIDDEN);
        }
        if(newOriginAccount.getBalance() < amount){
            return new ResponseEntity<>("Not enough money", HttpStatus.FORBIDDEN);
        }


        Double balanceCredit = newDestinyAccount.getBalance() + amount;
        Double balanceDebit = newOriginAccount.getBalance() - amount;

        Transaction debitTransaction = new Transaction(DEBIT,-amount,description + " go to " + destinyAccount,LocalDateTime.now(),newOriginAccount,balanceDebit);
        Transaction creditTransaction = new Transaction(CREDIT,amount,description + " from " + originAccount,LocalDateTime.now(),newDestinyAccount, balanceCredit);

        transactionService.saveTransaction (debitTransaction);
        transactionService.saveTransaction (creditTransaction);
        newOriginAccount.setBalance(newOriginAccount.getBalance() - amount);
        newDestinyAccount.setBalance(newDestinyAccount.getBalance() + amount);
        accountService.saveAccount(newOriginAccount);
        accountService.saveAccount(newDestinyAccount);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @Transactional
    @PostMapping("/api/clients/current/transactions/payments")
    public ResponseEntity<Object> newPayments(@RequestBody PaymentsDTO paymentsDTO){

        Card cardNumber = cardService.getCardByNumber(paymentsDTO.getNumber());

        if (paymentsDTO.getNumber().isEmpty()) {
            return new ResponseEntity<>("the card number field is empty", HttpStatus.FORBIDDEN);
        }
        if(cardNumber == null){
            return new ResponseEntity<>("The card number entered is not valid", HttpStatus.FORBIDDEN);
        }
        if(paymentsDTO.getCardHolder().isEmpty()){
            return new ResponseEntity<>("missing card name", HttpStatus.FORBIDDEN);
        }
        if(!Objects.equals(paymentsDTO.getCardHolder(), cardNumber.getCardHolder()) ){
            return new ResponseEntity<>("the card holder and the card number not match", HttpStatus.FORBIDDEN);
        }
        if(!Objects.equals(paymentsDTO.getCvv(), cardNumber.getCvv())){
            return new ResponseEntity<>("the card number and the cvv do not match", HttpStatus.FORBIDDEN);
        }
        if (paymentsDTO.getAmount() <= 0 ){
            return new ResponseEntity<>("invalid amount", HttpStatus.FORBIDDEN);
        }





        LocalDateTime myDate = cardNumber.getFromDate();
        String cardDate = myDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
        if (!Objects.equals(paymentsDTO.getThruDate(), cardDate)){
            return new ResponseEntity<>("The date does not match an associated card", HttpStatus.FORBIDDEN);
        }

        Account accountOrigin = cardNumber.getAccount();
        if(accountOrigin == null){
            return new ResponseEntity<>("account does not exist", HttpStatus.FORBIDDEN);
        }
        if (paymentsDTO.getAmount() > accountOrigin.getBalance()){
            return new ResponseEntity<>("you have no money in the account", HttpStatus.FORBIDDEN);
        }

        transactionService.saveTransaction(new Transaction(TransactionType.DEBIT,- paymentsDTO.getAmount(),paymentsDTO.getDescription(),LocalDateTime.now(),accountOrigin));
        accountOrigin.setBalance(accountOrigin.getBalance() - paymentsDTO.getAmount());
        return new ResponseEntity<>("success",HttpStatus.CREATED);
    }

    @PostMapping("/api/transactions/filtered")
    public ResponseEntity<Object> getFilteredTransaction(
            @RequestBody FilteredTransationDTO filteredTransationDTO, Authentication authentication) throws DocumentException, FileNotFoundException {
        Client client = clientService.getClientByEmail(authentication.getName());
        Account account = accountService.findByNumber(filteredTransationDTO.getClientAccount());
        if(!client.getAccounts().contains(account)){
            return new ResponseEntity<>("You cannot request data from an account that isn't yours.", HttpStatus.FORBIDDEN);
        }
        if (account.getTransactions()==null){
            return new ResponseEntity<>("You don't have any transactions in this account.", HttpStatus.FORBIDDEN);
        }

        Set<Transaction> transactions = transactionService.filterTransactionsWithDate(filteredTransationDTO.getFromDate(),filteredTransationDTO.getToDate(),account);
        createPdf( transactions);
        return new ResponseEntity<>("transactions",HttpStatus.CREATED);
    }

    public static void createPdf(Set<Transaction> transactions) throws DocumentException, FileNotFoundException {
        var doc = new Document();
        String route = System.getProperty("user.home");
        PdfWriter.getInstance(doc, new FileOutputStream(route + "/Downloads/TransactionInfo.pdf"));
        doc.open();

        var bold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
        var paragraph = new Paragraph("Hello World");
        var table = new PdfPTable(4);
        Stream.of("Amount", "Description","Date","Type").forEach(table::addCell);

        transactions.forEach(transaction -> {
            table.addCell("$" +transaction.getAmount());
            table.addCell(transaction.getDescription());
            table.addCell(transaction.getDate().toString());
            table.addCell(transaction.getType().toString());
        });

        paragraph.add(table);
        doc.add(paragraph);
        doc.close();
    }
}