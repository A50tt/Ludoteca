package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.dto.StatusResponse;
import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface LoanService {

    public Loan get(Long id);

    public List<Loan> findAll();

    public ResponseEntity<StatusResponse> save(LoanDto loanDto);
}
