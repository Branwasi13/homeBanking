package com.mindhub.homebanking.models;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class ClientLoan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private Double amount;

    private Integer payments;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
    @ManyToOne
    @JoinColumn(name = "loan_id")
    private Loan loan;

    public ClientLoan(){}

    public ClientLoan(Double amount, Integer payments, Client client, Loan loan) {
        this.amount = amount;
        this.payments = payments;
        this.client = client;
        this.loan = loan;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Long getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getPayments() {
        return payments;
    }

    public void setPayments(Integer payments) {
        this.payments = payments;
    }

    public static void interestLoan(Loan id, ClientLoan clientLoan){
        switch (id.getName()){
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
    }
}
