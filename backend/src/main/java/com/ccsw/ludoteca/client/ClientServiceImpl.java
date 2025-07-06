package com.ccsw.ludoteca.client;

import com.ccsw.ludoteca.client.model.Client;
import com.ccsw.ludoteca.client.model.ClientDto;
import com.ccsw.ludoteca.common.criteria.SearchCriteria;
import com.ccsw.ludoteca.exception.CommonErrorResponse;
import com.ccsw.ludoteca.dto.StatusResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.domain.Specification;
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

    @Override
    public List<Client> findByName(String name) {
        Specification<Client> nameSpec = new ClientSpecification(new SearchCriteria("name", "%", name));
        List<Client> listaClientes = clientRepository.findAll(nameSpec.and(nameSpec));
        return listaClientes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StatusResponse save(Long id, ClientDto dto) throws ClientException {
        // Se ha introducido un 'Client' sin 'name'
        try {
            if (dto.getName().isEmpty()) {
                throw new NullPointerException();
            }
        } catch (NullPointerException ex) {
            throw new ClientException(CommonErrorResponse.MISSING_REQUIRED_FIELDS, CommonErrorResponse.MISSING_REQUIRED_FIELDS_EXTENDED);
        }


        // Tenemos en cuenta si es edición o creación de 'Client' para devolver el mensaje correspondiente.
        Client client;
        boolean isUpdate = false;

        // Busca si existe otro 'Client' con ese nombre registrado en la BBDD.
        // Si la colisión de mismo nombre se produce en un registro de mismo 'id' (sobre él mismo), no da error y devuelve éxito sin hacer operación de modificación.
        // Esta decisión la he tomado porque en la mayoría de sistemas una modificación sobre un mismo registro no da error como tal.
        Specification<Client> nameSpec = new ClientSpecification(new SearchCriteria("name", ":", dto.getName()));
        List<Client> listaClientes = clientRepository.findAll(nameSpec.and(nameSpec));
        if (!listaClientes.isEmpty()) {
            if (listaClientes.get(0).getId().equals(dto.getId())) {
                return new StatusResponse(StatusResponse.OK_REQUEST_MSG, EDIT_SUCCESSFUL_EXT_MSG);
            }
            throw new ClientException(ClientException.NAME_ALREADY_EXISTS, ClientException.NAME_ALREADY_EXISTS_EXTENDED);
        }

        // Recuperamos un 'Client' con ese 'id' en la BBDD si existe y definimos la operación como UPDATE.
        // Si no existe, lo inicializamos sin valores.
        if (id == null) {
            client = new Client(); // Inicializamos el objeto (sin valores).
        } else {
            client = this.get(id);
            if (client != null) { // Si se ha encontrado un 'Client' con ese 'id' en la BBDD, es UPDATE.
                isUpdate = true;
            }
        }

        BeanUtils.copyProperties(dto, client, "id");
        client.setName(dto.getName());

        this.clientRepository.save(client); // Si da Exception o no y según UPDATE, devuelve un body u otro.
        if (isUpdate) {
            return new StatusResponse(StatusResponse.OK_REQUEST_MSG, EDIT_SUCCESSFUL_EXT_MSG);
        }
        return new StatusResponse(StatusResponse.OK_REQUEST_MSG, CREATION_SUCCESSFUL_EXT_MSG);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StatusResponse delete(Long id) throws ClientException {
        // Busca si el 'Client' existe realmente en la BBDD.
        if (clientRepository.findById(id).isEmpty()) {
            throw new ClientException(ClientException.CLIENT_ID_NOT_FOUND, ClientException.CLIENT_ID_NOT_FOUND_EXTENDED);
        }
        // Busca si este 'Client' tiene 'Loan's registrados a su nombre antes de hacer delete.
        if (helper.findLoansByClient(id)) {
            throw new ClientException(ClientException.CLIENT_HAS_GAMES, ClientException.CLIENT_HAS_GAMES_EXTENDED);
        }
        try {
            clientRepository.deleteById(id);
            return new StatusResponse(StatusResponse.OK_REQUEST_MSG, DELETE_SUCCESSFUL_EXT_MSG);
        } catch (Exception e) {
            throw new ClientException(CommonErrorResponse.DEFAULT_ERROR, CommonErrorResponse.DEFAULT_ERROR_EXTENDED);
        }
    }
}