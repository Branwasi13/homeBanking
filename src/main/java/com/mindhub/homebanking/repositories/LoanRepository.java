package com.mindhub.homebanking.repositories;


import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource
public interface LoanRepository extends JpaRepository<Loan, Long> {
    Loan findById(long id);
    Loan findByName(String name);
}
