package net.intact.component;

import java.util.List;

import net.intact.persistence.entity.UbiClient;
import EXAMPLE_ENDPOINT_PACKAGE.v1.exampleendpoint.Client;

public interface Ubi360Manager {

    Iterable<UbiClient> getAllClients();
    List<Client> getAllClientsWithName();

}
