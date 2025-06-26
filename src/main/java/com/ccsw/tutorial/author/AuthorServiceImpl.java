package com.ccsw.tutorial.author;

import com.ccsw.tutorial.author.model.Author;
import com.ccsw.tutorial.author.model.AuthorDto;
import com.ccsw.tutorial.author.model.AuthorSearchDto;
import com.ccsw.tutorial.common.exception.CommonException;
import com.ccsw.tutorial.dto.StatusResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final String CREATION_SUCCESSFUL_EXT_MSG = "El autor se ha creado correctamente";
    private final String EDIT_SUCCESSFUL_EXT_MSG = "El autor se ha modificado correctamente";

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    AuthorGameHelperService helper;

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
    public ResponseEntity<StatusResponse> save(Long id, AuthorDto data) {
        // Tenemos en cuenta si es edición o creación de Autor para devolver el mensaje correspondiente.
        Author author;
        boolean isUpdate = false;

        if (id == null) {
            author = new Author();
        } else {
            author = this.get(id);
            if (author != null) {
                isUpdate = true;
            }
        }

        BeanUtils.copyProperties(data, author, "id");

        try {
            this.authorRepository.save(author);
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

        if (this.get(id) == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StatusResponse(AuthorException.BAD_ID_CANNOT_DELETE, AuthorException.BAD_ID_CANNOT_DELETE_EXTENDED));
        }
        try {
            if (helper.findGamesByAuthor(id).isEmpty()) {
                this.authorRepository.deleteById(id);
                return ResponseEntity.status(HttpStatus.OK).body(new StatusResponse(StatusResponse.OK_REQUEST_MSG, CREATION_SUCCESSFUL_EXT_MSG));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new StatusResponse(AuthorException.AUTHOR_HAS_GAMES, AuthorException.AUTHOR_HAS_GAMES_EXTENDED));
        } catch (Exception ex2) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StatusResponse(CommonException.DEFAULT_ERROR, CommonException.DEFAULT_ERROR_EXTENDED));
        }
    }

}