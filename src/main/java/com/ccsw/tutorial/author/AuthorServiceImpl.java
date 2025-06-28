package com.ccsw.tutorial.author;

import com.ccsw.tutorial.author.model.Author;
import com.ccsw.tutorial.author.model.AuthorDto;
import com.ccsw.tutorial.author.model.AuthorSearchDto;
import com.ccsw.tutorial.common.exception.CommonException;
import com.ccsw.tutorial.dto.StatusResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ccsw
 *
 */
@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {

    private final String CREATION_SUCCESSFUL_EXT_MSG = "El autor se ha creado.";
    private final String EDIT_SUCCESSFUL_EXT_MSG = "El autor se ha modificado.";
    private final String DELETE_SUCCESSFUL_EXT_MSG = "El autor se ha eliminado.";

    private final AuthorRepository authorRepository;
    private final AuthorGameHelperService helper;

    public AuthorServiceImpl(AuthorRepository authorRepository, AuthorGameHelperService helper) {
        this.authorRepository = authorRepository;
        this.helper = helper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Author get(Long id) {
        return this.authorRepository.findById(id).orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Author> findAll() {

        return (List<Author>) this.authorRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Author> findPage(AuthorSearchDto dto) {

        return this.authorRepository.findAll(dto.getPageable().getPageable());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<StatusResponse> save(Long id, AuthorDto dto) {
        // Se ha introducido un 'Author' sin 'name' o 'nationality'
        if (dto.getName().isEmpty() || dto.getNationality().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StatusResponse(CommonException.MISSING_REQUIRED_FIELDS, CommonException.MISSING_REQUIRED_FIELDS_EXTENDED));
        }

        // Tenemos en cuenta si es edición o creación de 'Autor' para devolver el mensaje correspondiente.
        Author author;
        boolean isUpdate = false;

        // Recuperamos un 'Author' con ese 'id' en la BBDD si existe y definimos la operación como UPDATE.
        // Si no existe, lo inicializamos sin valores.
        if (id == null) {
            author = new Author(); // Inicializamos el objeto (sin valores).
        } else {
            author = this.get(id); // Si se ha encontrado un 'Author' con ese 'id' en la BBDD, es UPDATE.
            if (author != null) {
                isUpdate = true;
            }
        }

        BeanUtils.copyProperties(dto, author, "id");

        try {
            this.authorRepository.save(author); // Si da Exception o no y según UPDATE, devuelve un body u otro.
            if (isUpdate) {
                return ResponseEntity.status(HttpStatus.OK).body(new StatusResponse(StatusResponse.OK_REQUEST_MSG, EDIT_SUCCESSFUL_EXT_MSG));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new StatusResponse(StatusResponse.OK_REQUEST_MSG, CREATION_SUCCESSFUL_EXT_MSG));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StatusResponse(CommonException.DEFAULT_ERROR, CommonException.DEFAULT_ERROR_EXTENDED));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<StatusResponse> delete(Long id) throws Exception {

        // Check if author exists
        if (authorRepository.findById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StatusResponse(AuthorException.AUTHOR_ID_NOT_FOUND, AuthorException.AUTHOR_ID_NOT_FOUND_EXTENDED));
        }
        if (helper.findGamesByAuthor(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StatusResponse(AuthorException.AUTHOR_HAS_GAMES, AuthorException.AUTHOR_HAS_GAMES_EXTENDED));

        }
        try {
            authorRepository.deleteById(id);
            return ResponseEntity.ok().body(new StatusResponse(StatusResponse.OK_REQUEST_MSG, DELETE_SUCCESSFUL_EXT_MSG));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StatusResponse(CommonException.DEFAULT_ERROR, CommonException.DEFAULT_ERROR_EXTENDED));
        }
    }
}