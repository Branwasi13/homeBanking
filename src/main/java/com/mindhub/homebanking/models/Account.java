package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

        import javax.persistence.*;
        import java.time.LocalDateTime;
        import java.util.HashSet;
        import java.util.Set;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;

    @OneToMany(mappedBy="account", fetch=FetchType.EAGER)
    private Set<Transaction> transactions = new HashSet<>();

    @OneToMany(mappedBy="account", fetch=FetchType.EAGER)
    private Set<Card> cards = new HashSet<>();

    private String number;
    private LocalDateTime creationDate;
    private double balance;
    private AccountType accountType;
    private Boolean accountState;

    public Account(){ }

    public Account(String number, Double balance, LocalDateTime creationDate, Client client,AccountType accountType){
        this.number = number;
        this.balance = balance;
        this.creationDate = creationDate;
        this.client = client;
        this.accountType = accountType;
    }
    public Account(String number, Double balance, LocalDateTime creationDate, Client client,AccountType accountType,Boolean accountState){
        this.number = number;
        this.balance = balance;
        this.creationDate = creationDate;
        this.client = client;
        this.accountType = accountType;
        this.accountState = accountState;
    }
    public Account(String number, Double balance, LocalDateTime creationDate){
        this.number = number;
        this.balance = balance;
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Client getClient(){
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public Boolean getAccountState() {
        return accountState;
    }

    public void setAccountState(Boolean accountState) {
        this.accountState = accountState;
    }
}
