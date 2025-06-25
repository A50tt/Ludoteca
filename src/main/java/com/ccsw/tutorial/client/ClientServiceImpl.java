package com.ccsw.tutorial.client;

import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.client.model.ClientDto;
import com.ccsw.tutorial.common.criteria.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    ClientRepository clientRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Client> findAll() {
        return (List<Client>) clientRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Client get(Long id) {
        return this.clientRepository.findById(id).orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean save(Long id, ClientDto dto) {
        Client client;

        Specification<Client> nameSpec = new ClientSpecification(new SearchCriteria("name", ":", dto.getName()));
        if (clientRepository.exists(nameSpec)) {
            //Client 'name' already exists in DB.
            System.out.println("Nombre " + dto.getName() + " ya existe!");
            return false;
        } else {
            //Client 'name' does not exist already in DB
            if (id == null) {
                client = new Client();
            } else {
                client = this.get(id);
            }
            client.setName(dto.getName());

            clientRepository.save(client);
            return true;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) throws Exception {
        if (this.get(id) == null) {
            throw new Exception("Not exists");
        }
        clientRepository.deleteById(id);
    }
}