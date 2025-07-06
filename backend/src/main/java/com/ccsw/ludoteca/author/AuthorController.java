package com.ccsw.ludoteca.author;

import com.ccsw.ludoteca.author.model.Author;
import com.ccsw.ludoteca.author.model.AuthorDto;
import com.ccsw.ludoteca.author.model.AuthorSearchDto;
import com.ccsw.ludoteca.dto.StatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ccsw
 *
 */
@Tag(name = "Author", description = "API of Author")
@RequestMapping(value = "/author")
@RestController
@CrossOrigin(origins = "*")
public class AuthorController {

    private final AuthorService authorService;

    private final ModelMapper mapper;

    public AuthorController(AuthorService authorService, ModelMapper mapper) {
        this.authorService = authorService;
        this.mapper = mapper;
    }

    @Operation(summary = "Find", description = "Method that return a list of Authors")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<AuthorDto> findAll() {
        List<Author> authors = this.authorService.findAll();
        return authors.stream().map(e -> mapper.map(e, AuthorDto.class)).collect(Collectors.toList());
    }

    /**
     * Método para recuperar un listado paginado de {@link Author}
     *
     * @param dto dto de búsqueda
     * @return {@link Page} de {@link AuthorDto}
     */
    @Operation(summary = "Find Page", description = "Method that return a page of Authors")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public Page<AuthorDto> findPage(@RequestBody AuthorSearchDto dto) {
        Page<Author> page = this.authorService.findPage(dto);
        return new PageImpl<>(page.getContent().stream().map(e -> mapper.map(e, AuthorDto.class)).collect(Collectors.toList()), page.getPageable(), page.getTotalElements());
    }

    /**
     * Método para crear o actualizar un {@link Author}
     *
     * @param id PK de la entidad
     * @param dto datos de la entidad
     * @return ResponseEntity respuesta del servidor.
     */
    @Operation(summary = "Save or Update", description = "Method that saves or updates a Author")
    @RequestMapping(path = { "", "/{id}" }, method = RequestMethod.PUT)
    public ResponseEntity<StatusResponse> save(@PathVariable(name = "id", required = false) Long id, @RequestBody AuthorDto dto) throws AuthorException, Exception {
        return ResponseEntity.status(HttpStatus.OK).body(this.authorService.save(id, dto));
    }

    /**
     * Método para crear o actualizar un {@link Author}
     *
     * @param id PK de la entidad
     * @return ResponseEntity respuesta del servidor.
     */
    @Operation(summary = "Delete", description = "Method that deletes a Author")
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<StatusResponse> delete(@PathVariable("id") Long id) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(this.authorService.delete(id));
    }
}