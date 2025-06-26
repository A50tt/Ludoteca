package com.ccsw.tutorial.client;

import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.client.model.ClientDto;
import com.ccsw.tutorial.dto.StatusResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ClientService {

    /**
     * Recupera un {@link Client} a partir de su ID
     *
     * @param id PK de la entidad
     * @return {@link Client}
     */
    Client get(Long id);

    /**
     * Método para recuperar todos los {@link Client}
     *
     * @return {@link List} de {@link Client}
     */
    List<Client> findAll();

    /**
     * Método para crear o actualizar un {@link Client}
     *
     * @param id PK de la entidad
     * @param dto datos de la entidad
     */
    ResponseEntity<StatusResponse> save(Long id, ClientDto dto);

    /**
     * Método para borrar un {@link Client}
     *
     * @param id PK de la entidad
     */
    ResponseEntity<StatusResponse> delete(Long id) throws Exception;
}
