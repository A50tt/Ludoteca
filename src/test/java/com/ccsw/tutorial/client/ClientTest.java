package com.ccsw.tutorial.client;

import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.client.model.ClientDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientTest {
    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

    public final String CLIENT_NAME = "CLINAME_TEST";
    public final long EXISTENT_CLIENT_ID = 1L;
    public final long NON_EXISTENT_CLIENT_ID = 90L;

    // findAll() debería devolver todos los resultados de Client en la BD
    @Test
    public void findAllShouldReturnAllClients() {
        List<Client> list = new ArrayList<>();
        list.add(mock(Client.class));

        when(clientRepository.findAll()).thenReturn(list);

        List<Client> clients = clientService.findAll();

        assertNotNull(clients);
        assertEquals(1, clients.size());
    }

    // save() un cliente con nombre único ha de guardarlo correctamente.
    @Test
    public void saveWithUniqueNameShouldSave() {
        ClientDto clientDto = new ClientDto();
        clientDto.setName(CLIENT_NAME);

        ArgumentCaptor<Client> client = ArgumentCaptor.forClass(Client.class);

        clientService.save(null, clientDto);

        verify(clientRepository).save(client.capture());

        assertEquals(CLIENT_NAME, client.getValue().getName());
    }

    // save() un cliente con un nombre ya existente en la BD ha de devolver 'false' y no guardar.
    @Test
    public void saveExistsShouldReturnFalseAndNotSave() {
        ClientDto firstClientDto = new ClientDto();
        firstClientDto.setName(CLIENT_NAME);

        ClientDto existentClientDto = new ClientDto();
        existentClientDto.setName(CLIENT_NAME);

        // Configura que el primer sabe devuelve false (no existe en bd y guarda) y el segundo true
        when(clientRepository.exists(any(Specification.class))).thenReturn(false)  // Para el primer save (nombre no existe) devuelve FALSE de exists() y GUARDA.
                .thenReturn(true);  // Para el primer save (nombre no existe) devuelve TRUE de exists() y NO GUARDA.

        // First save should succeed (assuming it returns true)
        clientService.save(null, firstClientDto);

        // Capture the result of the second save
        boolean result = clientService.save(null, existentClientDto);

        ArgumentCaptor<Client> client = ArgumentCaptor.forClass(Client.class);

        // The repository's save method should only be called once (for the first client)
        verify(clientRepository, times(1)).save(any(Client.class));

        assertFalse(result);
    }

    // delete() un cliente que existe

    @Test
    public void deleteExistsShouldDelete() throws Exception {

        Client client = mock(Client.class);

        when(clientRepository.findById(EXISTENT_CLIENT_ID)).thenReturn(Optional.ofNullable(client));

        clientService.delete(EXISTENT_CLIENT_ID);

        verify(clientRepository).deleteById(EXISTENT_CLIENT_ID);
    }

    // delete() un cliente que no existe ha de lanzar Exception y no guardar
    @Test
    public void deleteClienteNotExistsShouldThrowExceptionAndNotSave() throws Exception {
        assertThrows(Exception.class, () -> {
            clientService.delete(NON_EXISTENT_CLIENT_ID);
        });
    }
}
