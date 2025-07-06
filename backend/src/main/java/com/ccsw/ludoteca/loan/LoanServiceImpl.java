package com.ccsw.ludoteca.loan;

import com.ccsw.ludoteca.common.criteria.SearchCriteria;
import com.ccsw.ludoteca.common.exception.CommonException;
import com.ccsw.ludoteca.dto.StatusResponse;
import com.ccsw.ludoteca.loan.model.Loan;
import com.ccsw.ludoteca.loan.model.LoanDto;
import com.ccsw.ludoteca.loan.model.LoanSearchDto;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanServiceImpl implements LoanService {

    private final Long MAX_LEND_DAYS = 14L;

    private final String VALIDATED_REQUEST = "Request validada.";
    private final int MAX_GAMES_PER_CLIENT = 2;
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
    public Page<Loan> findPageWithFilters(LoanSearchDto dto) {
        Specification<Loan> spec = Specification.where(null);

        // Agregar las 'Specifications' según si se han especificado o no.
        if (dto.getDate() != null) {
            spec = spec.and(new LoanSpecification(new SearchCriteria("startDate", "<=", dto.getDate())));
            spec = spec.and(new LoanSpecification(new SearchCriteria("endDate", ">=", dto.getDate())));
        }

        if (dto.getGameTitle() != null) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("game").get("title")), "%" + dto.getGameTitle().toLowerCase() + "%"));
        }

        if (dto.getClientName() != null) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("client").get("name")), "%" + dto.getClientName().toLowerCase() + "%"));
        }

        // Aplicar 'Specification' junto con al 'Pageable.
        return this.loanRepository.findAll(spec, dto.getPageable().getPageable());
    }

    @Override
    public StatusResponse save(LoanDto dto) {
        // Se ha introducido un Loan sin 'Client', 'Game', 'startDate' o 'endDate'
        if (dto.getStartDate() == null || dto.getEndDate() == null || dto.getClient() == null || dto.getGame() == null) {
            return new StatusResponse(CommonException.MISSING_REQUIRED_FIELDS, CommonException.MISSING_REQUIRED_FIELDS_EXTENDED);
        }
        // Debido a la cantidad de validaciones, se hacen en un método externo para tener un código más modularizable.
        switch (validate(dto)) {
        case VALIDATED_REQUEST:
            try {
                Loan loan = new Loan();
                BeanUtils.copyProperties(dto, loan);
                this.loanRepository.save(loan);
                return new StatusResponse(StatusResponse.OK_REQUEST_MSG, SAVED_SUCCESSFUL_EXTENDED_MSG);
            } catch (InvalidDataAccessApiUsageException ex1) {
                return new StatusResponse(CommonException.MISSING_REQUIRED_FIELDS, CommonException.MISSING_REQUIRED_FIELDS_EXTENDED);
            } catch (Exception ex2) {
                return new StatusResponse(CommonException.DEFAULT_ERROR, CommonException.DEFAULT_ERROR_EXTENDED);
            }
        case LoanException.INVALID_END_DATE:
            return new StatusResponse(LoanException.INVALID_END_DATE, LoanException.INVALID_END_DATE_EXTENDED);
        case LoanException.INVALID_PERIOD:
            return new StatusResponse(LoanException.INVALID_PERIOD, LoanException.INVALID_PERIOD_EXTENDED);
        case LoanException.GAME_ALREADY_LENT:
            return new StatusResponse(LoanException.GAME_ALREADY_LENT, LoanException.GAME_ALREADY_LENT_EXTENDED);
        case LoanException.LOAN_LIMIT_EXCEEDED:
            return new StatusResponse(LoanException.LOAN_LIMIT_EXCEEDED, LoanException.LOAN_LIMIT_EXCEEDED_EXTENDED);
        default:
            return new StatusResponse(CommonException.DEFAULT_ERROR, CommonException.DEFAULT_ERROR_EXTENDED);
        }
    }

    @Override
    public StatusResponse delete(Long id) {
        if (this.get(id) == null) {
            return new StatusResponse(LoanException.ID_NOT_EXIST, LoanException.ID_NOT_EXIST_EXTENDED);
        } else {
            try {
                loanRepository.deleteById(id);
                return new StatusResponse(StatusResponse.OK_REQUEST_MSG, DELETE_SUCCESSFUL_EXTENDED_MSG);
            } catch (Exception ex) {
                return new StatusResponse(CommonException.DEFAULT_ERROR, CommonException.DEFAULT_ERROR_EXTENDED);
            }
        }
    }

    private String validate(LoanDto loanDto) {
        // DATE VALIDATIONS
        // Si 'endDate' es menor que 'startDate' => ERROR
        if (loanDto.getEndDate().isBefore(loanDto.getStartDate())) {
            return LoanException.INVALID_END_DATE;
        }
        // Si préstamo es mayor de 14 días => ERROR
        if (loanDto.getEndDate().isAfter(loanDto.getStartDate().plusDays(MAX_LEND_DAYS))) {
            return LoanException.INVALID_PERIOD;
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
            return LoanException.GAME_ALREADY_LENT;
        }

        // El 'Client' tiene prestados más de MAX_GAMES_PER_CLIENT 'Games' en un mismo día del período => ERROR
        List<Loan> loansSamePeriodSameClient = this.loanRepository.findAll(startDateSpec.and(endDateSpec).and(clientSpec));
        if (loansSamePeriodSameClient.size() >= MAX_GAMES_PER_CLIENT) {
            return LoanException.LOAN_LIMIT_EXCEEDED;
        }
        return VALIDATED_REQUEST;
    }
}