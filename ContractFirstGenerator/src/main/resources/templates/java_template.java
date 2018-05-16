package net.intact.webservice;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.WebService;

import PACKAGE_NAME.SERVICE_NAME.ENDPOINT_NAME;
import PACKAGE_NAME.SERVICE_NAME.v1.ENDPOINT_LOWER_NAME.ENDPOINT_NAMERequest;
import PACKAGE_NAME.SERVICE_NAME.v1.ENDPOINT_LOWER_NAME.ENDPOINT_NAMEResponse;

@WebService
public class ENDPOINT_NAMEPortImpl implements ENDPOINT_NAME {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public ENDPOINT_NAMEResponse ENDPOINT_METHOD_NAME(ENDPOINT_NAMERequest message) { 
        LOGGER.info("Executing operation ENDPOINT_METHOD_NAME");

        try {
            ENDPOINT_NAMEResponse resp = new ENDPOINT_NAMEResponse();

            resp.setSomeValueResponse( "Received from request: " + message.getSomeValueRequest() );

            return resp;
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

}
