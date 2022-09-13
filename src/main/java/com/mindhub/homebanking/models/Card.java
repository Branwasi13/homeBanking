package com.mindhub.homebanking.models;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "clientCard_id")
    private Client client;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="account_id")
    private Account account;
    private String cardHolder;
    private CardType cardType;
    private CardColor cardColor;
    private String number;
    private Integer cvv;
    private LocalDateTime thruDate;
    private LocalDateTime fromDate;

    private boolean stateOfCards;

    public Card() {
    }

    public Card(Client client, String cardHolder, CardType cardType, CardColor cardColor, String number, Integer cvv, LocalDateTime thruDate, LocalDateTime fromDate, boolean stateOfCards) {
        this.client = client;
        this.cardHolder = cardHolder;
        this.cardType = cardType;
        this.cardColor = cardColor;
        this.number = number;
        this.cvv = cvv;
        this.thruDate = thruDate;
        this.fromDate = fromDate;
        this.stateOfCards = stateOfCards;
    }

    public Card(Client client, String cardHolder, CardType cardType, CardColor cardColor, String number, Integer cvv, LocalDateTime thruDate, LocalDateTime fromDate, boolean stateOfCards, Account account) {
        this.client = client;
        this.cardHolder = cardHolder;
        this.cardType = cardType;
        this.cardColor = cardColor;
        this.number = number;
        this.cvv = cvv;
        this.thruDate = thruDate;
        this.fromDate = fromDate;
        this.stateOfCards = stateOfCards;
        this.account = account;
    }

    public Long getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public CardType getCardType() {
        return cardType;
    }

    public CardColor getCardColor() {
        return cardColor;
    }

    public String getNumber() {
        return number;
    }

    public Integer getCvv() {
        return cvv;
    }

    public LocalDateTime getThruDate() {
        return thruDate;
    }

    public boolean isStateOfCards() {
        return stateOfCards;
    }

    public void setStateOfCards(boolean stateOfCards) {
        this.stateOfCards = stateOfCards;
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public void setCardColor(CardColor cardColor) {
        this.cardColor = cardColor;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setCvv(Integer cvv) {
        this.cvv = cvv;
    }

    public void setThruDate(LocalDateTime thruDate) {
        this.thruDate = thruDate;
    }

    public void setFromDate(LocalDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}