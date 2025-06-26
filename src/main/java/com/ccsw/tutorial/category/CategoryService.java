package com.ccsw.tutorial.category;

import com.ccsw.tutorial.category.model.Category;
import com.ccsw.tutorial.category.model.CategoryDto;
import com.ccsw.tutorial.dto.StatusResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @author ccsw
 *
 */
public interface CategoryService {

    /**
     * Recupera una {@link Category} a partir de su ID
     *
     * @param id PK de la entidad
     * @return {@link Category}
     */
    Category get(Long id);

    /**
     * Método para recuperar todas las {@link Category}
     *
     * @return {@link List} de {@link Category}
     */
    List<Category> findAll();

    /**
     * Método para crear o actualizar una {@link Category}
     *
     * @param id PK de la entidad
     * @param dto datos de la entidad
     */
    ResponseEntity<StatusResponse> save(Long id, CategoryDto dto);

    /**
     * Método para borrar una {@link Category}
     *
     * @param id PK de la entidad
     */
    ResponseEntity<StatusResponse> delete(Long id) throws Exception;

}