package com.mindhub.homebanking.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PaymentsDTO {
    private String cardHolder;

    private String number;

    private Double amount;

    private Integer cvv;

    private String thruDate;

    private String description;

    public PaymentsDTO(){};

    public PaymentsDTO(String cardHolder, String number, Double amount, Integer cvv, String thruDate, String description) {
        this.cardHolder = cardHolder;
        this.number = number;
        this.amount = amount;
        this.cvv = cvv;
        this.thruDate = thruDate;
        this.description = description;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public String getNumber() {
        return number;
    }

    public Double getAmount() {
        return amount;
    }

    public Integer getCvv() {
        return cvv;
    }

    public String getThruDate() {
        return thruDate;
    }

    public String getDescription() {
        return description;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setCvv(Integer cvv) {
        this.cvv = cvv;
    }

    public void setThruDate(String thruDate) {
        this.thruDate = thruDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
