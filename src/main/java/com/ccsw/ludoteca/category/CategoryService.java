package com.ccsw.ludoteca.category;

import com.ccsw.ludoteca.category.model.Category;
import com.ccsw.ludoteca.category.model.CategoryDto;
import com.ccsw.ludoteca.dto.StatusResponse;

import java.util.List;

/**
 * @author ccsw
 *
 */
public interface CategoryService {

    /**
     * Recupera una {@link Category} a partir de su {@code id}.
     *
     * @param id PK de la entidad
     * @return {@link Category}
     */
    Category get(Long id);

    /**
     * Método para recuperar todas las {@link Category}.
     *
     * @return {@link List} de {@link Category}
     */
    List<Category> findAll();

    /**
     * Método para crear o actualizar una {@link Category}.
     * Devolverá un @{code StatusResponse} con error si falta cualquiera de los cambos.
     *
     * @param id PK de la entidad
     * @param dto datos de la entidad
     * @return ResponseEntity - Respuesta {@link StatusResponse} del servidor.
     */
    StatusResponse save(Long id, CategoryDto dto);

    /**
     * Método para borrar una {@link Category}
     *
     * @param id PK de la entidad
     * @return ResponseEntity - Respuesta {@link StatusResponse} del servidor.
     */
    StatusResponse delete(Long id) throws Exception;

}