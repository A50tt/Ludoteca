package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.dto.StatusResponse;
import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import com.ccsw.tutorial.loan.model.LoanSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface LoanService {

    public Loan get(Long id);

    public List<Loan> findAll();

    Page<Loan> findPage(LoanSearchDto dto);

    public ResponseEntity<StatusResponse> save(LoanDto loanDto);

    public ResponseEntity<StatusResponse> delete(Long id);
}
