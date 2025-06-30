package com.ccsw.ludoteca.loan;

import com.ccsw.ludoteca.dto.StatusResponse;
import com.ccsw.ludoteca.loan.model.Loan;
import com.ccsw.ludoteca.loan.model.LoanDto;
import com.ccsw.ludoteca.loan.model.LoanSearchDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface LoanService {

    /**
     * Devuelve todos los {@code loan} de la base de datos.
     * @return {@code loan} que coincide con el {@code id} proporcionado.
     */
    public Loan get(Long id);

    /**
     * Devuelve todos los {@code loan} de la base de datos.
     * @return List de {@code loan} en la base de datos.
     */
    public List<Loan> findAll();

    Page<Loan> findPage(LoanSearchDto dto);

    /**
     * Guarda o actualiza el {@link Loan} dependiendo de si ya existe en la base de datos.
     * Antes de guardar, valida las siguientes condiciones:
     * <ul>
     * <li>{@code endDate} no es menor que {@code startDate}
     * <li>{@code endDate} - {@code startDate} no puede ser mayor que 14 días
     * <li>El {@code game} no puede estar ya prestado en ninguna de los días del período escogido.
     * <li>Un {@code client} no puede tener más de 2 juegos prestados en un mismo día.
     * </ul>
     * Si todas estas validaciones son superadas, el {@code loan} se guarda en la base de datos y se devuelve una respuesta.
     * @param loanDto {@link Loan} a guardar.
     * @return ResponseEntity - Respuesta {@link StatusResponse} del servidor.
     */
    public StatusResponse save(LoanDto loanDto);

    /**
     * Comprueba que el {@link Loan} con el {@code id} proporcionado existe in the database.
     * Si existe en la base de datos, lo borra de la misma.
     * Si no existe, abandona el proceso.
     *
     * @param id - Id del {@link Loan}.
     * @return ResponseEntity - Respuesta {@link StatusResponse} del servidor.
     */
    public StatusResponse delete(Long id);
}
