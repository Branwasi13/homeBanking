package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Loan;

import java.util.List;


public interface CardService {

    public void saveCard(Card card);

    public Card getCardById(Long id);

    Card getCardByNumber(String number);

    List<Card> getAllCards();
}
