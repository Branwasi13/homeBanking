package com.mindhub.homebanking;


import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase(replace = NONE)

public class RepositoriesTest {

    @Autowired
    LoanRepository loanRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    AccountRepository accountRepository;

    @Test
    public void existLoans(){

        List<Loan> loans = loanRepository.findAll();
        assertThat(loans,is(not(empty())));

    }

    @Test
    public void existPersonalLoan(){

        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));

    }

    @Test
    public void existLoanTypes(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("Hipotecario"))));
        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));
        assertThat(loans, hasItem(hasProperty("name", is("Automotriz"))));
    }
    @Test
    public void existAccountNumber() {
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts, hasItem(hasProperty("number", is("VIN-00000001"))));
        assertThat(accounts, hasItem(hasProperty("number", is("VIN-00000002"))));
    }
    @Test
    public void existCards() {
        List<Card> cards = cardRepository.findAll();
        assertThat(cards,is(not(empty())));
    }
    @Test
    public void existClients() {
        List<Client> clients = clientRepository.findAll();
        assertThat(clients,is(not(empty())));
    }
    @Test
    public void existClientName() {
        List<Client> clients = clientRepository.findAll();
        assertThat(clients, hasItem(hasProperty("firstName", is("Melba"))));
    }
    @Test
    public void existTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions,is(not(empty())));
    }



}
