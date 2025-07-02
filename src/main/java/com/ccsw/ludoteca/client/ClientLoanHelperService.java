package com.ccsw.ludoteca.client;

import com.ccsw.ludoteca.common.criteria.SearchCriteria;
import com.ccsw.ludoteca.loan.LoanRepository;
import com.ccsw.ludoteca.loan.LoanSpecification;
import org.springframework.stereotype.Service;

@Service
public class ClientLoanHelperService {
    private final ClientRepository clientRepository;
    private final LoanRepository loanRepository;

    public ClientLoanHelperService(ClientRepository clientRepository, LoanRepository loanRepository) {
        this.clientRepository = clientRepository;
        this.loanRepository = loanRepository;
    }

    /**
     * Recupera los Loan filtrando por Client
     *
     * @param idClient PK de la entidad Client
     */
    public boolean findLoansByClient(Long idClient) {
        LoanSpecification ClientSpec = new LoanSpecification(new SearchCriteria("client.id", ":", idClient));
        return !loanRepository.findAll(ClientSpec).isEmpty();
    }
}
