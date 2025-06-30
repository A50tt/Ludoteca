package com.ccsw.tutorial.author;

import com.ccsw.tutorial.author.model.Author;
import com.ccsw.tutorial.author.model.AuthorDto;
import com.ccsw.tutorial.author.model.AuthorSearchDto;
import com.ccsw.tutorial.dto.StatusResponse;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author ccsw
 *
 */
public interface AuthorService {

    /**
     * Recupera un {@link Author} a través de su ID
     *
     * @param id PK de la entidad
     * @return {@link Author}
     */
    Author get(Long id);

    /**
     * Recupera un listado de autores {@link Author}
     *
     * @return {@link List} de {@link Author}
     */
    List<Author> findAll();

    /**
     * Método para recuperar un listado paginado de {@link Author}
     *
     * @param dto dto de búsqueda
     * @return {@link Page} de {@link Author}
     */
    Page<Author> findPage(AuthorSearchDto dto);

    /**
     * Método para crear o actualizar un {@link Author}
     * Devolverá un @{code StatusResponse} con error si falta cualquiera de los cambos.
     *
     * @param id PK de la entidad
     * @param dto datos de la entidad
     * @return ResponseEntity - Respuesta {@link StatusResponse} del servidor.
     */
    StatusResponse save(Long id, AuthorDto dto);

    /**
     * Método para crear o actualizar un {@link Author}
     *
     * @param id PK de la entidad
     * @return ResponseEntity - Respuesta {@link StatusResponse} del servidor.
     */
    StatusResponse delete(Long id) throws Exception;

}