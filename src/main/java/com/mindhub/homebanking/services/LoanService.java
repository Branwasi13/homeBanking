package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Loan;

import java.util.List;

public interface LoanService {
    public void saveLoan(Loan loan);

    public Loan findLoanById(long id);

    public Loan findLoanByName(String name);

    public List<Loan> getAllLoans();

}
