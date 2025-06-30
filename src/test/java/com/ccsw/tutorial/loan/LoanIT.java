package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.client.ClientService;
import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.client.model.ClientDto;
import com.ccsw.tutorial.common.pagination.PageableRequest;
import com.ccsw.tutorial.config.ResponsePage;
import com.ccsw.tutorial.dto.StatusResponse;
import com.ccsw.tutorial.game.GameService;
import com.ccsw.tutorial.game.model.Game;
import com.ccsw.tutorial.game.model.GameDto;
import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import com.ccsw.tutorial.loan.model.LoanSearchDto;
import jakarta.validation.constraints.Null;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LoanIT {

    private static final int TOTAL_LOANS = 6;
    private static final int PAGE_SIZE = 5;

    private static final LocalDate NEW_START_DATE = LocalDate.now();
    private static final LocalDate NEW_END_DATE = LocalDate.now().plusDays(6);

    private static final Long EXISTENT_LOAN_ID = 1L;
    private static final Long NON_EXISTENT_LOAN_ID = 99L;

    private static final String EXISTENT_CLIENT_NAME = "Cliente 5";
    private static final Long EXISTENT_CLIENT_ID = 5L;
    private static final String EXISTENT_GAME_TITLE = "Los viajes de Marco Polo";
    private static final Long EXISTENT_GAME_CATEGORY_ID = 1L;

    private static final Long NON_EXISTENT_CLIENT_ID = 99L;
    private static final Long NON_EXISTENT_GAME_ID = 99L;

    public static final String LOCALHOST = "http://localhost:";
    public static final String SERVICE_PATH = "/loan";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    ParameterizedTypeReference<List<LoanDto>> loanListResponse = new ParameterizedTypeReference<List<LoanDto>>() {
    };

    ParameterizedTypeReference<LoanDto> loanResponse = new ParameterizedTypeReference<LoanDto>() {
    };

    ParameterizedTypeReference<ResponsePage<LoanDto>> responsePageResponse = new ParameterizedTypeReference<ResponsePage<LoanDto>>() {
    };

    ParameterizedTypeReference<StatusResponse> statusResponse = new ParameterizedTypeReference<StatusResponse>() {
    };
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private GameService gameService;
    @Autowired
    private ClientService clientService;

    /**
     * {@code findAll()} debería devolver un {@code List<Loan>} de los {@code Loan} guardados en la BBDD.
     */
    @Test
    public void findAllShouldReturnAllLoans() {

        ResponseEntity<List<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.GET, null, loanListResponse);

        assertNotNull(response);
        assertEquals(6, response.getBody().size());
    }

    /**
     * {@code findPage()} debería devolver un {@code List<Loan>} de los {@code Loan} guardados en la BBDD correspondientes a esa {@code Page}.
     */
    @Test
    public void findFirstPageWithFiveSizeShouldReturnFirstFiveResults() {

        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responsePageResponse);

        assertNotNull(response);
        assertEquals(TOTAL_LOANS, response.getBody().getTotalElements());
        assertEquals(PAGE_SIZE, response.getBody().getContent().size());
    }

    /**
     * {@code findPage()} debería devolver un {@code List<Loan>} de los {@code Loan} guardados en la BBDD correspondientes a esa {@code Page}.
     * En el caso de este test, al ser 6 {@code Loan}, con una {@code PageableRequest} de 5 objetos {@code Loan} por
     * {@code Page}, debería recuperar una {@code List<Loan>} con solo 1 {@code Loan}.
     **/
    @Test
    public void findSecondPageWithFiveSizeShouldReturnLastResult() {

        int elementsCount = TOTAL_LOANS - PAGE_SIZE;

        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(1, PAGE_SIZE));

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responsePageResponse);

        assertNotNull(response);
        assertEquals(TOTAL_LOANS, response.getBody().getTotalElements());
        assertEquals(elementsCount, response.getBody().getContent().size());
    }

    /**
     * Si se hace {@code save()} de un {@code Loan} con {@code id = null}, debería crear un nuevo {@code Loan}.
     */
    @Test
    public void saveWithoutIdShouldCreateNewLoan() {

        long newLoanId = TOTAL_LOANS + 1;
        long newLoanSize = TOTAL_LOANS + 1;

        LoanDto dto = new LoanDto();
        Client client = new Client();
        client.setId(EXISTENT_CLIENT_ID);
        Game game = new Game();
        game.setId(EXISTENT_GAME_CATEGORY_ID);
        dto.setStartDate(NEW_START_DATE);
        dto.setEndDate(NEW_END_DATE);
        dto.setClient(client);
        dto.setGame(game);

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, (int) newLoanSize));

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responsePageResponse);

        assertNotNull(response);
        assertEquals(newLoanSize, response.getBody().getTotalElements());

        LoanDto loan = response.getBody().getContent().stream().filter(item -> item.getId().equals(newLoanId)).findFirst().orElse(null);
        assertNotNull(loan);
        assertEquals(NEW_START_DATE, loan.getStartDate());
        assertEquals(NEW_END_DATE, loan.getEndDate());
    }

    /**
     *  Si se hace {@code save()} de un {@code Loan} con {@code id} existente, debería modificar un {@code Loan} de la BBDD.
     */
    @Test
    public void saveWithExistingIdShouldModifyExistingLoan() {
        LoanDto dto = new LoanDto();
        dto.setId(EXISTENT_LOAN_ID);
        dto.setClient(clientService.get(EXISTENT_CLIENT_ID));
        dto.setGame(gameService.find(EXISTENT_GAME_TITLE, EXISTENT_GAME_CATEGORY_ID).getFirst());
        dto.setStartDate(NEW_START_DATE.plusDays(new Random().nextInt(5) + 1));
        dto.setEndDate(NEW_END_DATE.plusDays(new Random().nextInt(5) + 1));

        ResponseEntity<StatusResponse> saveResponse = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), statusResponse);

        assertNotNull(saveResponse);
        assertEquals(HttpStatus.OK, saveResponse.getStatusCode());

        ResponseEntity<LoanDto> findResponse = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + EXISTENT_LOAN_ID, HttpMethod.GET, null, loanResponse);
        assertNotNull(findResponse);
        assertEquals(dto.getStartDate(), findResponse.getBody().getStartDate());
    }

    /**
     *  Si se hace {@code save()} de un {@code Loan} con {@code id} que no existe, debería devolver un {@code HttpStatus.BAD_REQUEST} y guardarlo.
     */
    @Test
    public void saveWithNonExistingIdShouldReturnBadRequestError() {
        LoanDto dto = new LoanDto();
        dto.setId(NON_EXISTENT_LOAN_ID);
        dto.setClient(clientService.get(EXISTENT_CLIENT_ID));
        dto.setGame(gameService.find(EXISTENT_GAME_TITLE, EXISTENT_GAME_CATEGORY_ID).getFirst());
        dto.setStartDate(NEW_START_DATE);
        dto.setEndDate(NEW_END_DATE);

        ResponseEntity<StatusResponse> saveResponse = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), statusResponse);

        assertNotNull(saveResponse);
        assertEquals(HttpStatus.BAD_REQUEST, saveResponse.getStatusCode());
    }

    /**
     *  Si se hace {@code delete()} de un {@code Loan} con {@code id} que existe, debería devolver un {@code HttpStatus.OK} y borrarlo.
     */
    @Test
    public void deleteWithExistingIdShouldReturnOk() {

        ResponseEntity<LoanDto> dto = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + EXISTENT_CLIENT_ID, HttpMethod.GET, null, loanResponse);
        assertNotNull(dto);

        ResponseEntity<StatusResponse> deleteResponse = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + EXISTENT_CLIENT_ID, HttpMethod.DELETE, null, statusResponse);
        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());

        assertNotNull(deleteResponse);
        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());

        ResponseEntity<LoanDto> getResponse = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + EXISTENT_CLIENT_ID, HttpMethod.GET, null, loanResponse);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, getResponse.getStatusCode());
    }

    /**
     *  Si se hace {@code delete()} de un {@code Loan} con {@code id} que no existe, debería devolver un {@code HttpStatus.INTERNAL_SERVER_ERROR}.
     */
    @Test
    public void deleteWithExistingIdShouldReturnBadRequestError() {

        ResponseEntity<LoanDto> dto = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + EXISTENT_CLIENT_ID, HttpMethod.GET, null, loanResponse);
        assertNotNull(dto);

        ResponseEntity<StatusResponse> deleteResponse = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + NON_EXISTENT_CLIENT_ID, HttpMethod.DELETE, null, statusResponse);
        assertNotNull(deleteResponse);
        assertEquals(HttpStatus.BAD_REQUEST, deleteResponse.getStatusCode());
    }
}
