package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.loan.model.Loan;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoanTest {

    public static final Long EXISTING_LOAN_ID = 1L;

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private LoanServiceImpl loanService;

    /**
     * Debería devolver un {@code List<Client>} de los {@code Client} guardados en la BBDD.
     */
    @Test
    public void findAllShouldReturnAllLoans() {
        List<Loan> list = new ArrayList<>();
        list.add(mock(Loan.class));

        when(loanRepository.findAll()).thenReturn(list);

        List<Loan> categories = loanService.findAll();

        assertNotNull(categories);
        assertEquals(1, categories.size());
    }
    //todo sigue haciendo test
}
