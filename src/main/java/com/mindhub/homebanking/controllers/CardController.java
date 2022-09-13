package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.CardService;
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
public class CardController {

    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private ClientService clientService;

    @Autowired
    private CardService cardService;

    @PostMapping("/api/clients/current/cards")
    public ResponseEntity<Object> createCards(@RequestParam CardColor cardColor, @RequestParam CardType cardType, Authentication authentication){

        Client clientCard = clientService.getClientByEmail(authentication.getName());

        int cardCvv = CardUtils.getCvv();

        String cardNumber = CardUtils.getCardNumber();

        if(cardType == null || cardColor == null) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if(clientCard.getCards().stream().filter(card -> card.getCardType().equals(cardType) && card.isStateOfCards()).count() >= 3){
            return new ResponseEntity<>("You can only create 3 cards per type (credit and debit)", HttpStatus.FORBIDDEN);
        }
        if(clientCard.getCards().stream().anyMatch(card -> card.getCardColor().equals(cardColor) && card.getCardType().equals(cardType) && card.isStateOfCards())){
            return new ResponseEntity<>("for each card you can only select one color", HttpStatus.FORBIDDEN);
        }


        cardService.saveCard(new Card(clientCard, clientCard.toString(), cardType, cardColor, cardNumber,cardCvv,LocalDateTime.now(), LocalDateTime.now().plusYears(5),true));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/api/clients/current/cards/state")
    public ResponseEntity<Object> deleteCards(@RequestParam String number, Authentication authentication){

        Client clientCard = clientService.getClientByEmail(authentication.getName());
        Card cardNumber =cardRepository.findByNumber(number);
        if(clientCard == null){
            return new ResponseEntity<>("missing client", HttpStatus.FORBIDDEN);
        }

        if(cardNumber == null){
            return new ResponseEntity<>("missing card number", HttpStatus.FORBIDDEN);
        }

        if(!clientCard.getCards().contains(cardNumber)){
            return new ResponseEntity<>("the requested card is not found", HttpStatus.FORBIDDEN);
        }

        cardNumber.setStateOfCards(false);

        cardService.saveCard(cardNumber);

        return new ResponseEntity<>("your card is delete",HttpStatus.CREATED);
    }

    @GetMapping("/api/cards")
    public List<CardDTO> getLoans(){
        return cardService.getAllCards().stream().map(CardDTO::new).collect(toList());
    }

}
