package net.intact.component;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.intact.persistence.entity.UbiClient;
import net.intact.persistence.repository.UbiClientRepository;
import EXAMPLE_ENDPOINT_PACKAGE.v1.exampleendpoint.Client;

@Component
public class Ubi360ManagerImpl implements Ubi360Manager {

    @Autowired
    private UbiClientRepository ubiClientRepository;

    public Iterable<UbiClient> getAllClients() {
        return ubiClientRepository.findAll();
    }

    public List<Client> getAllClientsWithName() {
        return ubiClientRepository.getAllClientsWithName().stream()
                .limit(10)
                .map(c -> {
                    Client client = new Client();
                    client.setClientNumber(c.getClientNumber());
                    client.setFirstName(c.getFirstName());
                    client.setLastName(c.getLastName());
                    return client;
                })
                .collect(Collectors.toList());
    }

}
