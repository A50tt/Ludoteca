package com.ccsw.ludoteca.category;

import com.ccsw.ludoteca.category.model.Category;
import com.ccsw.ludoteca.category.model.CategoryDto;
import com.ccsw.ludoteca.dto.StatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ccsw
 *
 */
@Tag(name = "Category", description = "API of Category")
@RequestMapping(value = "/category")
@RestController
@CrossOrigin(origins = "*")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    ModelMapper mapper;

    /**
     * Método para recuperar todas las {@link Category}
     *
     * @return {@link List} de {@link CategoryDto}
     */
    @Operation(summary = "Find", description = "Method that return a list of Categories")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<CategoryDto> findAll() {

        List<Category> categories = this.categoryService.findAll();

        return categories.stream().map(e -> mapper.map(e, CategoryDto.class)).collect(Collectors.toList());
    }

    /**
     * Método para crear o actualizar una {@link Category}
     *
     * @param id PK de la entidad
     * @param dto datos de la entidad
     * @return ResponseEntity respuesta del servidor.
     */
    @Operation(summary = "Save or Update", description = "Method that saves or updates a Category")
    @RequestMapping(path = { "", "/{id}" }, method = RequestMethod.PUT)
    public ResponseEntity<StatusResponse> save(@PathVariable(name = "id", required = false) Long id, @RequestBody CategoryDto dto) throws CategoryException {
        return ResponseEntity.status(HttpStatus.OK).body(this.categoryService.save(id, dto));
    }

    /**
     * Método para borrar una {@link Category}
     *
     * @param id PK de la entidad
     * @return ResponseEntity respuesta del servidor.
     */
    @Operation(summary = "Delete", description = "Method that deletes a Category")
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<StatusResponse> delete(@PathVariable("id") Long id) throws CategoryException {
        return ResponseEntity.status(HttpStatus.OK).body(this.categoryService.delete(id));
    }
}