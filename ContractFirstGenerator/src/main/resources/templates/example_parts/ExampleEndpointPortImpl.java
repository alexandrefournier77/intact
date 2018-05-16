package net.intact.webservice;

import java.lang.invoke.MethodHandles;
import java.util.List;

import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import net.intact.component.Ubi360Manager;
import EXAMPLE_ENDPOINT_PACKAGE.ExampleEndpoint;
import EXAMPLE_ENDPOINT_PACKAGE.v1.exampleendpoint.Client;
import EXAMPLE_ENDPOINT_PACKAGE.v1.exampleendpoint.ClientList;
import EXAMPLE_ENDPOINT_PACKAGE.v1.exampleendpoint.ExampleEndpointRequest;
import EXAMPLE_ENDPOINT_PACKAGE.v1.exampleendpoint.ExampleEndpointResponse;


@WebService
public class ExampleEndpointPortImpl implements ExampleEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private Ubi360Manager ubi360Manager;

    @Override
    public ExampleEndpointResponse exampleEndpoint(ExampleEndpointRequest message) { 
        LOGGER.info("Executing operation exampleEndpoint");

        try {
            // Get data from DB
            List<Client> clients = ubi360Manager.getAllClientsWithName();
            ClientList cl = new ClientList();
            cl.getClient().addAll(clients);

            ExampleEndpointResponse resp = new ExampleEndpointResponse();
            resp.setSomeValueResponse( "Received from request: " + message.getSomeValueRequest() );
            resp.setClientsList(cl);

            return resp;
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
}
