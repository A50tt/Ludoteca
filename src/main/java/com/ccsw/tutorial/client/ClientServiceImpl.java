package com.ccsw.tutorial.client;

import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.client.model.ClientDto;
import com.ccsw.tutorial.common.criteria.SearchCriteria;
import com.ccsw.tutorial.common.exception.CommonException;
import com.ccsw.tutorial.dto.StatusResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    private final String CREATION_SUCCESSFUL_EXT_MSG = "El cliente se ha creado.";
    private final String EDIT_SUCCESSFUL_EXT_MSG = "El cliente se ha modificado.";
    private final String DELETE_SUCCESSFUL_EXT_MSG = "El cliente se ha eliminado.";

    private final ClientRepository clientRepository;
    private final ClientLoanHelperService helper;

    public ClientServiceImpl(ClientRepository clientRepository, ClientLoanHelperService helper) {
        this.clientRepository = clientRepository;
        this.helper = helper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Client> findAll() {
        return (List<Client>) clientRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Client get(Long id) {
        return this.clientRepository.findById(id).orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<StatusResponse> save(Long id, ClientDto dto) {
        // Tenemos en cuenta si es edición o creación de Client para devolver el mensaje correspondiente.
        Client client;
        boolean isUpdate = false;

        Specification<Client> nameSpec = new ClientSpecification(new SearchCriteria("name", ":", dto.getName()));
        if (!clientRepository.findAll(nameSpec.and(nameSpec)).isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StatusResponse(ClientException.NAME_ALREADY_EXISTS, ClientException.NAME_ALREADY_EXISTS_EXTENDED));
        }
        if (id == null) {
            client = new Client();
        } else {
            client = this.get(id);
            if (client != null) {
                isUpdate = true;
            }
        }

        // client.setName(dto.getName());
        BeanUtils.copyProperties(dto, client, "id");

        try {
            this.clientRepository.save(client);
            if (isUpdate) {
                return ResponseEntity.status(HttpStatus.OK).body(new StatusResponse(StatusResponse.OK_REQUEST_MSG, EDIT_SUCCESSFUL_EXT_MSG));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new StatusResponse(StatusResponse.OK_REQUEST_MSG, CREATION_SUCCESSFUL_EXT_MSG));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StatusResponse(CommonException.DEFAULT_ERROR, CommonException.DEFAULT_ERROR_EXTENDED));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<StatusResponse> delete(Long id) throws Exception {
        // Check if client exists
        if (clientRepository.findById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StatusResponse(ClientException.CLIENT_ID_NOT_FOUND, ClientException.CLIENT_ID_NOT_FOUND_EXTENDED));

        }
        if (helper.findLoansByClient(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StatusResponse(ClientException.CLIENT_HAS_GAMES, ClientException.CLIENT_HAS_GAMES_EXTENDED));
        }
        try {
            clientRepository.deleteById(id);
            return ResponseEntity.ok().body(new StatusResponse(StatusResponse.OK_REQUEST_MSG, DELETE_SUCCESSFUL_EXT_MSG));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StatusResponse(CommonException.DEFAULT_ERROR, CommonException.DEFAULT_ERROR_EXTENDED));
        }
    }
}