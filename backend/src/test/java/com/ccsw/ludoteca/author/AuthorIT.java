package com.ccsw.ludoteca.author;

import com.ccsw.ludoteca.author.model.AuthorDto;
import com.ccsw.ludoteca.author.model.AuthorSearchDto;
import com.ccsw.ludoteca.common.pagination.PageableRequest;
import com.ccsw.ludoteca.config.ResponsePage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthorIT {

    public static final String LOCALHOST = "http://localhost:";
    public static final String SERVICE_PATH = "/author";

    public static final Long DELETE_AUTHOR_ID = 6L;
    public static final Long MODIFY_AUTHOR_ID = 3L;
    public static final String NEW_AUTHOR_NAME = "Nuevo Autor";
    public static final String NEW_NATIONALITY = "Nueva Nacionalidad";

    private static final int TOTAL_AUTHORS = 6;
    private static final int PAGE_SIZE = 5;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    ParameterizedTypeReference<ResponsePage<AuthorDto>> responseTypePage = new ParameterizedTypeReference<ResponsePage<AuthorDto>>() {
    };

    @Test
    public void findFirstPageWithFiveSizeShouldReturnFirstFiveResults() {

        AuthorSearchDto searchDto = new AuthorSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<AuthorDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(TOTAL_AUTHORS, response.getBody().getTotalElements());
        assertEquals(PAGE_SIZE, response.getBody().getContent().size());
    }

    @Test
    public void findSecondPageWithFiveSizeShouldReturnLastResult() {

        int elementsCount = TOTAL_AUTHORS - PAGE_SIZE;

        AuthorSearchDto searchDto = new AuthorSearchDto();
        searchDto.setPageable(new PageableRequest(1, PAGE_SIZE));

        ResponseEntity<ResponsePage<AuthorDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(TOTAL_AUTHORS, response.getBody().getTotalElements());
        assertEquals(elementsCount, response.getBody().getContent().size());
    }

    @Test
    public void saveWithoutIdShouldCreateNewAuthor() {

        long newAuthorId = TOTAL_AUTHORS + 1;
        long newAuthorSize = TOTAL_AUTHORS + 1;

        AuthorDto dto = new AuthorDto();
        dto.setName(NEW_AUTHOR_NAME);
        dto.setNationality(NEW_NATIONALITY);

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        AuthorSearchDto searchDto = new AuthorSearchDto();
        searchDto.setPageable(new PageableRequest(0, (int) newAuthorSize));

        ResponseEntity<ResponsePage<AuthorDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(newAuthorSize, response.getBody().getTotalElements());

        AuthorDto author = response.getBody().getContent().stream().filter(item -> item.getId().equals(newAuthorId)).findFirst().orElse(null);
        assertNotNull(author);
        assertEquals(NEW_AUTHOR_NAME, author.getName());
    }

    @Test
    public void modifyWithExistIdShouldModifyAuthor() {

        AuthorDto dto = new AuthorDto();
        dto.setName(NEW_AUTHOR_NAME);
        dto.setNationality(NEW_NATIONALITY);

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + MODIFY_AUTHOR_ID, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        AuthorSearchDto searchDto = new AuthorSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<AuthorDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(TOTAL_AUTHORS, response.getBody().getTotalElements());

        AuthorDto author = response.getBody().getContent().stream().filter(item -> item.getId().equals(MODIFY_AUTHOR_ID)).findFirst().orElse(null);
        assertNotNull(author);
        assertEquals(NEW_AUTHOR_NAME, author.getName());
        assertEquals(NEW_NATIONALITY, author.getNationality());
    }

    @Test
    public void modifyWithNotExistIdShouldReturnBadRequestError() {

        long authorId = TOTAL_AUTHORS + 1;

        AuthorDto dto = new AuthorDto();
        dto.setName(NEW_AUTHOR_NAME);

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + authorId, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void deleteWithExistsIdShouldDeleteCategory() {

        long newAuthorsSize = TOTAL_AUTHORS - 1;

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + DELETE_AUTHOR_ID, HttpMethod.DELETE, null, Void.class);

        AuthorSearchDto searchDto = new AuthorSearchDto();
        searchDto.setPageable(new PageableRequest(0, TOTAL_AUTHORS));

        ResponseEntity<ResponsePage<AuthorDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(newAuthorsSize, response.getBody().getTotalElements());
    }

    @Test
    public void deleteWithNotExistsIdShouldReturnNotFoundError() {

        long deleteAuthorId = TOTAL_AUTHORS + 1;

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + deleteAuthorId, HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    ParameterizedTypeReference<List<AuthorDto>> responseTypeList = new ParameterizedTypeReference<List<AuthorDto>>(){};

    @Test
    public void findAllShouldReturnAllAuthor() {

        ResponseEntity<List<AuthorDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.GET, null, responseTypeList);

        assertNotNull(response);
        assertEquals(TOTAL_AUTHORS, response.getBody().size());
    }

}