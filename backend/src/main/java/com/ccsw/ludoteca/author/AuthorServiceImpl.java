package com.ccsw.ludoteca.author;

import com.ccsw.ludoteca.author.model.Author;
import com.ccsw.ludoteca.author.model.AuthorDto;
import com.ccsw.ludoteca.author.model.AuthorSearchDto;
import com.ccsw.ludoteca.common.exception.CommonErrorResponse;
import com.ccsw.ludoteca.dto.StatusResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
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
    public StatusResponse save(Long id, AuthorDto dto) throws Exception {
        // Se ha introducido un 'Author' sin 'name' o 'nationality'
        try {
            if (dto.getName().isEmpty() || dto.getNationality().isEmpty()) {
                return new StatusResponse(CommonErrorResponse.MISSING_REQUIRED_FIELDS, CommonErrorResponse.MISSING_REQUIRED_FIELDS_EXTENDED);
            }
        } catch (NullPointerException ex1) {
            throw new AuthorException(CommonErrorResponse.MISSING_REQUIRED_FIELDS, CommonErrorResponse.MISSING_REQUIRED_FIELDS_EXTENDED);
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
                return new StatusResponse(StatusResponse.OK_REQUEST_MSG, EDIT_SUCCESSFUL_EXT_MSG);
            }
            return new StatusResponse(StatusResponse.OK_REQUEST_MSG, CREATION_SUCCESSFUL_EXT_MSG);
        } catch (Exception ex) {
            throw new AuthorException(CommonErrorResponse.DEFAULT_ERROR, CommonErrorResponse.DEFAULT_ERROR_EXTENDED);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StatusResponse delete(Long id) throws Exception {
        // Check if 'Author' exists
        if (authorRepository.findById(id).isEmpty()) {
            throw new AuthorException(AuthorException.AUTHOR_ID_NOT_FOUND, AuthorException.AUTHOR_ID_NOT_FOUND_EXTENDED);
        }
        if (helper.findGamesByAuthor(id)) {
            throw new AuthorException(AuthorException.AUTHOR_HAS_GAMES, AuthorException.AUTHOR_HAS_GAMES_EXTENDED);
        }
        try {
            authorRepository.deleteById(id);
            return new StatusResponse(StatusResponse.OK_REQUEST_MSG, DELETE_SUCCESSFUL_EXT_MSG);
        } catch (NullPointerException ex1) {
            throw new AuthorException(CommonErrorResponse.MISSING_REQUIRED_FIELDS, CommonErrorResponse.MISSING_REQUIRED_FIELDS_EXTENDED);
        } catch (Exception e) {
            throw new AuthorException(CommonErrorResponse.DEFAULT_ERROR, CommonErrorResponse.DEFAULT_ERROR_EXTENDED);
        }
    }
}