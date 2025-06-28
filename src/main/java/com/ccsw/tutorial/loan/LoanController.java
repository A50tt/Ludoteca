package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.common.exception.CommonException;
import com.ccsw.tutorial.dto.StatusResponse;
import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import com.ccsw.tutorial.loan.model.LoanSearchDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.hibernate.TransientObjectException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Loan", description = "API of Loan")
@RequestMapping(value = "/loan")
@RestController
@CrossOrigin(origins = "*")
public class LoanController {

    private final LoanService loanService;

    private final ModelMapper mapper;

    public LoanController(LoanService loanService, ModelMapper mapper) {
        this.loanService = loanService;
        this.mapper = mapper;
    }

    /**
     * Método para recuperar una {@link List} de {@link Loan}
     *
     * @return {@link List} de {@link LoanDto}
     */
    @Operation(summary = "Find all", description = "Method that returns a list of every Loan")
    @RequestMapping(path = { "" }, method = RequestMethod.GET)
    public List<LoanDto> findAll() {
        List<Loan> loans = loanService.findAll();
        return loans.stream().map(e -> mapper.map(e, LoanDto.class)).collect(Collectors.toList());
    }

    /**
     * Método para recuperar un {@link Loan}
     *
     * @param id PK del préstamo
     * @return {@link LoanDto}
     */
    @Operation(summary = "Find", description = "Method that returns a Loan")
    @RequestMapping(path = { "/{id}" }, method = RequestMethod.GET)
    public LoanDto find(@PathVariable(value = "id") Long id) {
        Loan loan = loanService.get(id);
        return mapper.map(loan, LoanDto.class);
    }

    /**
     * Método para recuperar un listado paginado de {@link Loan}
     *
     * @param dto dto de búsqueda
     * @return {@link Page} de {@link LoanDto}
     */
    @Operation(summary = "Find Page", description = "Method that return a page of Loans")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public Page<LoanDto> findPage(@RequestBody LoanSearchDto dto) {

        Page<Loan> page = this.loanService.findPage(dto);

        return new PageImpl<>(page.getContent().stream().map(e -> mapper.map(e, LoanDto.class)).collect(Collectors.toList()), page.getPageable(), page.getTotalElements());
    }

    /**
     * Método para crear un {@link Loan}
     *
     * @param dto datos de la entidad
     * @return ResponseEntity respuesta del servidor.
     */
    @Operation(summary = "Save", description = "Method that saves a Loan")
    @RequestMapping(path = { "" }, method = RequestMethod.PUT)
    public ResponseEntity<StatusResponse> save(@RequestBody LoanDto dto) {
        try {
            return loanService.save(dto);
        } catch (NullPointerException | DataIntegrityViolationException | HttpMessageNotReadableException | InvalidDataAccessApiUsageException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StatusResponse(CommonException.MISSING_REQUIRED_FIELDS, CommonException.MISSING_REQUIRED_FIELDS_EXTENDED));
        } catch (Exception ex2) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StatusResponse(CommonException.DEFAULT_ERROR, CommonException.DEFAULT_ERROR_EXTENDED));
        }
    }

    /**
     * Método para borrar un {@link Loan}
     *
     * @param id PK de la entidad
     * @return ResponseEntity respuesta del servidor.
     */
    @Operation(summary = "Delete", description = "Method that deletes a Loan")
    @RequestMapping(path = { "/{id}" }, method = RequestMethod.DELETE)
    public ResponseEntity<StatusResponse> delete(@PathVariable Long id) {
        try {
            return loanService.delete(id);
        } catch (NullPointerException ex1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StatusResponse(CommonException.MISSING_REQUIRED_FIELDS, CommonException.MISSING_REQUIRED_FIELDS_EXTENDED));
        } catch (Exception ex2) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StatusResponse(CommonException.DEFAULT_ERROR, CommonException.DEFAULT_ERROR_EXTENDED));
        }
    }
}