package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;


@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner initData(ClientService clientService, AccountService accountService, TransactionService transactionService, LoanService loanService, ClientLoanService clientLoanService, CardService cardService) {
		return (args) -> {
			Client client1 = new Client("Melba", "Morel","melba@mindhub.com",passwordEncoder.encode("melba"));
			clientService.saveClient(client1);

			Client client2 = new Client("Brandon", "Wasilewicz","bradon@mindhub.com",passwordEncoder.encode("456"));
			clientService.saveClient(client2);

			Client admin1 = new Client("lionel","messi","lionel@admin.com",passwordEncoder.encode("10"));
			clientService.saveClient(admin1);

			Account account1 = new Account("VIN-00000001", 5000.0, LocalDateTime.now(),client1,AccountType.SAVING,true);
			accountService.saveAccount(account1);

			Account account2 = new Account("VIN-00000002", 7500.0, LocalDateTime.now(),client1,AccountType.CURRENT,true);
			client1.addAccount(account2);
			accountService.saveAccount(account2);

			Account account3 = new Account("VIN-00000003", 5000.0, LocalDateTime.now(),client2,AccountType.CURRENT,true);
			accountService.saveAccount(account3);

			Transaction transaction1 = new Transaction(TransactionType.CREDIT, 5000.0, "Credit transaction", LocalDateTime.now().plusHours(3), account1);
			transactionService.saveTransaction(transaction1);

			Transaction transaction2 = new Transaction(TransactionType.DEBIT, -2500.0, "Debit transaction", LocalDateTime.now().plusDays(20).plusHours(7).plusMinutes(29), account1);
			transactionService.saveTransaction(transaction2);

			Transaction transaction3 = new Transaction(TransactionType.CREDIT, 2500.0, "Credit transaction", LocalDateTime.now().plusDays(40).plusHours(13).plusMinutes(43), account1);
			transactionService.saveTransaction(transaction3);

			Transaction transaction4 = new Transaction(TransactionType.CREDIT, 15000.0, "Credit transaction", LocalDateTime.now(), account2);
			transactionService.saveTransaction(transaction4);

			Transaction transaction5 = new Transaction(TransactionType.DEBIT, -5537.0, "Credit transaction", LocalDateTime.now(), account2);
			transactionService.saveTransaction(transaction5);

			Transaction transaction6 = new Transaction(TransactionType.DEBIT, -1963.0, "Credit transaction", LocalDateTime.now(), account2);
			transactionService.saveTransaction(transaction6);

			Loan Hipotecario = new Loan("Hipotecario" , 500000.00, List.of(12,24,36,48,60));
			loanService.saveLoan(Hipotecario);

			Loan Personal = new Loan("Personal" , 100000.00, List.of(6,12,24));
			loanService.saveLoan(Personal);

			Loan Automotriz = new Loan("Automotriz" , 300000.00, List.of(6,12,24,36));
			loanService.saveLoan(Automotriz);

			ClientLoan loan1 = new ClientLoan(400000.00,60,client1,Hipotecario);
			clientLoanService.saveClientLoan(loan1);

			ClientLoan loan2 = new ClientLoan(50000.00,12,client1,Personal);
			clientLoanService.saveClientLoan(loan2);

			ClientLoan loan3 = new ClientLoan(100000.00,24,client2,Personal);
			clientLoanService.saveClientLoan(loan3);

			ClientLoan loan4 = new ClientLoan(200000.00,36,client2,Automotriz);
			clientLoanService.saveClientLoan(loan4);

			Card card1 = new Card(client1,(client1.toString()),CardType.DEBIT, CardColor.GOLD,"5030-3695-5876-6231",982,LocalDateTime.now(),LocalDateTime.now().plusYears(5),true,account1);
			cardService.saveCard(card1);

			Card card2 = new Card(client1,(client1.toString()),CardType.CREDIT, CardColor.TITANIUM,"3103-5785-7892-4537",582,LocalDateTime.now(),LocalDateTime.now().plusYears(7),true,account2);
			cardService.saveCard(card2);

			Card card3 = new Card(client2,(client2.toString()),CardType.DEBIT, CardColor.SILVER,"4566-7869-4532-2305",369,LocalDateTime.now(),LocalDateTime.now(),true,account3);
			cardService.saveCard(card3);


		};
	}
}
