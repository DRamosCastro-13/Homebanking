package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dto.LoanDTO;
import com.mindhub.homebanking.models.Loan;

import java.util.List;

public interface LoanService {

    List<Loan> getAllLoans();

    List<LoanDTO> getAllLoansDTO();

    Loan findById(Long id);

    LoanDTO getLoanDTOById(Long id);

    void saveLoan(Loan loan);
}
