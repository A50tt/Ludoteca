package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.game.model.Game;
import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanTest {

    public static final Long EXISTING_LOAN_ID = 1L;

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private LoanServiceImpl loanService;

    /**
     * Debería devolver un {@code List<Loan>} de los {@code Loan} guardados en la BBDD.
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

    /**
     * {@code save()} un {@code Loan} debería guardar.
     */
    @Test
    public void saveValidLoanShouldSave() {
        //Arrange
        LoanDto loanDto = new LoanDto();
        loanDto.setClient(mock(Client.class));
        loanDto.setGame(mock(Game.class));
        loanDto.setStartDate(LocalDate.now());
        loanDto.setEndDate(LocalDate.now().plusDays(4));

        Loan loan = new Loan();
        loan.setClient(loanDto.getClient());
        loan.setGame(loanDto.getGame());
        loan.setStartDate(loanDto.getStartDate());
        loan.setEndDate(loanDto.getEndDate());

        when(loanRepository.save(any(Loan.class))).thenReturn(loan);

        loanService.save(loanDto);

        verify(loanRepository).save(any(Loan.class));

        assertEquals(loan.getId(), loanDto.getId());
    }
}