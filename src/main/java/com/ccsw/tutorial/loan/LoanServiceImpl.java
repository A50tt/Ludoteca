package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.common.criteria.SearchCriteria;
import com.ccsw.tutorial.common.exception.CommonException;
import com.ccsw.tutorial.dto.StatusResponse;
import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import com.ccsw.tutorial.loan.model.LoanSearchDto;
import org.hibernate.TransientObjectException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Period;
import java.util.List;

@Service
public class LoanServiceImpl implements LoanService {

    private final Long MAX_LEND_DAYS = 14L;
    private final String SAVED_SUCCESSFUL_EXTENDED_MSG = "Préstamo guardado.";
    private final String DELETE_SUCCESSFUL_EXTENDED_MSG = "Préstamo borrado.";

    private final LoanRepository loanRepository;

    public LoanServiceImpl(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    @Override
    public Loan get(Long id) {
        return loanRepository.findById(id).orElse(null);
    }

    @Override
    public List<Loan> findAll() {
        return (List<Loan>) loanRepository.findAll();
    }

    @Override
    public Page<Loan> findPage(LoanSearchDto dto) {

        return this.loanRepository.findAll(dto.getPageable().getPageable());
    }

    @Override
    public ResponseEntity<StatusResponse> save(LoanDto dto) {
        // Se ha introducido un Loan sin 'Client', 'Game', 'startDate' o 'endDate'
        if (dto.getStartDate() == null || dto.getEndDate() == null || dto.getClient().getId() == null || dto.getGame().getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StatusResponse(CommonException.MISSING_REQUIRED_FIELDS, CommonException.MISSING_REQUIRED_FIELDS_EXTENDED));
        }
        // Debido a la cantidad de validaciones, se hacen en un método externo.
        StatusResponse response = validate(dto);
        if (response.getMessage().equals(StatusResponse.SUITABLE_REQUEST)) {
            try {
                Loan loan = new Loan();
                BeanUtils.copyProperties(dto, loan);
                this.loanRepository.save(loan);
                return ResponseEntity.status(HttpStatus.OK).body(new StatusResponse(StatusResponse.OK_REQUEST_MSG, SAVED_SUCCESSFUL_EXTENDED_MSG));
            } catch (InvalidDataAccessApiUsageException ex1) {
                throw new InvalidDataAccessApiUsageException(null);
            } catch (Exception ex2) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StatusResponse(CommonException.DEFAULT_ERROR, CommonException.DEFAULT_ERROR_EXTENDED));
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @Override
    public ResponseEntity<StatusResponse> delete(Long id) {
        if (this.get(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StatusResponse(LoanException.ID_NOT_EXIST, LoanException.ID_NOT_EXIST_EXTENDED));
        } else {
            try {
                loanRepository.deleteById(id);
                return ResponseEntity.status(HttpStatus.OK).body(new StatusResponse(StatusResponse.OK_REQUEST_MSG, DELETE_SUCCESSFUL_EXTENDED_MSG));
            } catch (Exception ex) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StatusResponse(CommonException.DEFAULT_ERROR, CommonException.DEFAULT_ERROR_EXTENDED));
            }
        }
    }

    private StatusResponse validate(LoanDto loanDto) {
        // DATE VALIDATIONS
        // Si 'endDate' es menor que 'startDate' => ERROR
        System.out.println(loanDto.getEndDate().compareTo(loanDto.getStartDate()));
        if (loanDto.getEndDate().isBefore(loanDto.getStartDate())) {
            return new StatusResponse(LoanException.INVALID_END_DATE, LoanException.INVALID_END_DATE_EXTENDED);
        }
        // Si préstamo es mayor de 14 días => ERROR
        if (Period.between(loanDto.getStartDate(), loanDto.getEndDate()).getDays() > MAX_LEND_DAYS) {
            return new StatusResponse(LoanException.INVALID_PERIOD, LoanException.INVALID_PERIOD_EXTENDED);
        }

        // DB VALIDATIONS
        // Define 'Specifications' para el solapamiento de 'dates' y búsqueda de 'Game' y 'Cliente'.
        Specification<Loan> startDateSpec = new LoanSpecification(new SearchCriteria("endDate", ">=", loanDto.getStartDate()));
        Specification<Loan> endDateSpec = new LoanSpecification(new SearchCriteria("startDate", "<=", loanDto.getEndDate()));
        Specification<Loan> gameSpec = new LoanSpecification(new SearchCriteria("game.id", ":", loanDto.getGame().getId()));
        Specification<Loan> clientSpec = new LoanSpecification(new SearchCriteria("client.id", ":", loanDto.getClient().getId()));

        // El 'Game' está prestado en cualquiera de los días del período seleccionado => ERROR
        List<Loan> loansSamePeriodSameGame = this.loanRepository.findAll(startDateSpec.and(endDateSpec).and(gameSpec));
        if (!loansSamePeriodSameGame.isEmpty()) {
            return new StatusResponse(LoanException.GAME_ALREADY_LENT, LoanException.GAME_ALREADY_LENT_EXTENDED);
        }

        // El 'Client' tiene prestados más de 2 'Games' en un mismo día del período => ERROR
        List<Loan> loansSamePeriodSameClient = this.loanRepository.findAll(startDateSpec.and(endDateSpec).and(clientSpec));
        if (loansSamePeriodSameClient.size() >= 2) {
            return new StatusResponse(LoanException.LOAN_LIMIT_EXCEEDED, LoanException.LOAN_LIMIT_EXCEEDED_EXTENDED);
        }
        return new StatusResponse(StatusResponse.SUITABLE_REQUEST); // Se espera un 'StatusResponse', por lo que enviamos validación.
    }
}
