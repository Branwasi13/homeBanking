package com.mindhub.homebanking.services.implementation;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CardServiceImplementation implements CardService {
    @Autowired
    private CardRepository cardRepository;

    @Override
    public void saveCard(Card card){
        cardRepository.save(card);
    }


    @Override
    public Card getCardById(Long id){
        return cardRepository.findById(id).get();
    }

    @Override
    public Card getCardByNumber(String number){
        return cardRepository.findByNumber(number);
    }

    @Override
    public List<Card> getAllCards (){
        return cardRepository.findAll();
    }
}
