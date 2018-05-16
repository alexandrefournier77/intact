    // Endpoint: ENDPOINT_NAME
    @Bean
    public EndpointImpl ENDPOINT_LOWER_NAME() {
        EndpointImpl endpoint = new EndpointImpl(bus, getENDPOINT_NAME());
        endpoint.publish("/v1/ENDPOINT_NAME");
        return endpoint;
    }
    @Bean
    public ENDPOINT_NAME getENDPOINT_NAME() {
        return new ENDPOINT_NAMEPortImpl();
    }
