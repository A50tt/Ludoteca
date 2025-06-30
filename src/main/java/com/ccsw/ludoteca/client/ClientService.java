package com.ccsw.ludoteca.client;

import com.ccsw.ludoteca.client.model.Client;
import com.ccsw.ludoteca.client.model.ClientDto;
import com.ccsw.ludoteca.dto.StatusResponse;

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
     * Devolverá un @{code StatusResponse} con error si falta cualquiera de los cambos.
     *
     * @param id PK de la entidad
     * @param dto datos de la entidad
     * @return ResponseEntity - Respuesta {@link StatusResponse} del servidor.
     */
    StatusResponse save(Long id, ClientDto dto);

    /**
     * Método para borrar un {@link Client}
     *
     * @param id PK de la entidad
     * @return ResponseEntity - Respuesta {@link StatusResponse} del servidor.
     */
    StatusResponse delete(Long id) throws Exception;
}
