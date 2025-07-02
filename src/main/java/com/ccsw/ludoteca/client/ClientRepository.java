package com.ccsw.ludoteca.client;

import com.ccsw.ludoteca.client.model.Client;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Client, Long>, JpaSpecificationExecutor<Client> {
}
