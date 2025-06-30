package com.ccsw.tutorial.category;

import com.ccsw.tutorial.category.model.Category;
import com.ccsw.tutorial.category.model.CategoryDto;
import com.ccsw.tutorial.common.exception.CommonException;
import com.ccsw.tutorial.dto.StatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
    public ResponseEntity<StatusResponse> save(@PathVariable(name = "id", required = false) Long id, @RequestBody CategoryDto dto) {
        try {
            StatusResponse response = this.categoryService.save(id, dto);
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
     * Método para borrar una {@link Category}
     *
     * @param id PK de la entidad
     * @return ResponseEntity respuesta del servidor.
     */
    @Operation(summary = "Delete", description = "Method that deletes a Category")
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<StatusResponse> delete(@PathVariable("id") Long id) throws Exception {
        try {
            StatusResponse response = this.categoryService.delete(id);
            if (response.getMessage().equals(StatusResponse.OK_REQUEST_MSG)) {
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else if (response.getMessage().equals(CategoryException.CATEGORY_ID_NOT_FOUND)) {
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