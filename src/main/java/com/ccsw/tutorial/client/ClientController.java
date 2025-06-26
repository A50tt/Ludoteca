package com.ccsw.tutorial.client;

import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.client.model.ClientDto;
import com.ccsw.tutorial.dto.StatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Client", description = "API of Client")
@RequestMapping(value = "/clients")
@RestController
@CrossOrigin(origins = "*")
public class ClientController {
    private final ClientService clientService;

    private final ModelMapper mapper;

    public ClientController(ClientService clientService, ModelMapper mapper) {
        this.clientService = clientService;
        this.mapper = mapper;
    }

    /**
     * Método para recuperar todos los {@link Client}
     *
     * @return {@link List} de {@link ClientDto}
     */
    @Operation(summary = "Find", description = "Method that return a list of Clients")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<ClientDto> findAll() {

        List<Client> clients = this.clientService.findAll();

        return clients.stream().map(e -> mapper.map(e, ClientDto.class)).collect(Collectors.toList());
    }

    /**
     * Método para crear o actualizar un {@link Client}
     *
     * @param id PK de la entidad
     * @param dto datos de la entidad
     */
    @Operation(summary = "Save", description = "Method that saves or updates a Client")
    @RequestMapping(path = { "", "/{id}" }, method = RequestMethod.PUT)
    public ResponseEntity<StatusResponse> save(@PathVariable(name = "id", required = false) Long id, @RequestBody ClientDto dto) {
        return clientService.save(id, dto);
    }

    /**
     * Método para borrar un {@link Client}
     *
     * @param id PK de la entidad
     */
    @Operation(summary = "Delete", description = "Method that deletes a Category")
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<StatusResponse> delete(@PathVariable("id") Long id) throws Exception {
        return this.clientService.delete(id);
    }
}
