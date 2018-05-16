package net.intact.config;

import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

ENDPOINTS_DEFINITION_IMPORTS
ENDPOINTS_IMPLEMENTATION_IMPORTS

@Configuration
public class ServiceConfig {

    @Autowired
    private Bus bus;

ENDPOINTS_CONFIG
}
