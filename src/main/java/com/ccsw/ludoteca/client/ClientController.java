package com.ccsw.ludoteca.client;

import com.ccsw.ludoteca.client.model.Client;
import com.ccsw.ludoteca.client.model.ClientDto;
import com.ccsw.ludoteca.common.exception.CommonException;
import com.ccsw.ludoteca.dto.StatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Client", description = "API of Client")
@RequestMapping(value = "/client")
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
    public List<ClientDto> findAll(@RequestParam(value = "name", required = false) String clientName) {
//      List<Client> clients = clientService.findAll();
        List<Client> clients = clientService.findByName(clientName);

        return clients.stream().map(e -> mapper.map(e, ClientDto.class)).collect(Collectors.toList());
    }

    /**
     * Método para crear o actualizar un {@link Client}
     *
     * @param id PK de la entidad
     * @param dto datos de la entidad
     * @return ResponseEntity respuesta del servidor.
     */
    @Operation(summary = "Save", description = "Method that saves or updates a Client")
    @RequestMapping(path = { "", "/{id}" }, method = RequestMethod.PUT)
    public ResponseEntity<StatusResponse> save(@PathVariable(name = "id", required = false) Long id, @RequestBody ClientDto dto) {
        try {
            StatusResponse response = clientService.save(id, dto);
            if (response.getMessage().equals(StatusResponse.OK_REQUEST_MSG)) {
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (NullPointerException | DataIntegrityViolationException ex1) { // No se ha introducido algún campo obligatorio.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StatusResponse(CommonException.MISSING_REQUIRED_FIELDS, CommonException.MISSING_REQUIRED_FIELDS_EXTENDED));
        } catch (Exception ex2) { // Exception catch por defecto.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StatusResponse(CommonException.DEFAULT_ERROR, CommonException.DEFAULT_ERROR_EXTENDED));
        }
    }

    /**
     * Método para borrar un {@link Client}
     *
     * @param id PK de la entidad
     * @return ResponseEntity respuesta del servidor.
     */
    @Operation(summary = "Delete", description = "Method that deletes a Category")
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<StatusResponse> delete(@PathVariable("id") Long id) throws Exception {
        try {
            StatusResponse response = this.clientService.delete(id);
            if (response.getMessage().equals(StatusResponse.OK_REQUEST_MSG)) {
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else if (response.getMessage().equals(ClientException.CLIENT_ID_NOT_FOUND)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (NullPointerException ex1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StatusResponse(CommonException.MISSING_REQUIRED_FIELDS, CommonException.MISSING_REQUIRED_FIELDS_EXTENDED));
        } catch (Exception ex2) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StatusResponse(CommonException.DEFAULT_ERROR, CommonException.DEFAULT_ERROR_EXTENDED));
        }
    }
}
