package net.intact.junit.webservice;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import net.intact.config.TestConfig;
import PACKAGE.ENDPOINT_NAME;
import PACKAGE.v1.LOWER_ENDPOINT_NAME.ENDPOINT_NAMERequest;
import PACKAGE.v1.LOWER_ENDPOINT_NAME.ENDPOINT_NAMEResponse;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=TestConfig.class)
public class ENDPOINT_NAMETest {

    @Autowired
    private ENDPOINT_NAME LOWERCAMEL_ENDPOINT_NAME;

    @Test
    public void testFirstEndpoint() {
        // Set request
        ENDPOINT_NAMERequest request = new ENDPOINT_NAMERequest();
        request.setSomeValueRequest("abc");

        // Call service
        ENDPOINT_NAMEResponse response = LOWERCAMEL_ENDPOINT_NAME.LOWERCAMEL_ENDPOINT_NAME(request);

        // Validate response
        assertNotNull(response);
        assertTrue("Received from request: abc".equals(response.getSomeValueResponse()));
    }

}
