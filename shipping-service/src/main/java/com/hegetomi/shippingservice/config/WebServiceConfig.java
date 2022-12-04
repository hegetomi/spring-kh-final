package com.hegetomi.shippingservice.config;

import com.hegetomi.shippingservice.xmlws.ShippingXmlWs;
import lombok.RequiredArgsConstructor;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

@Configuration
@RequiredArgsConstructor
public class WebServiceConfig {

    private final Bus bus;
    private final ShippingXmlWs shippingXmlWs;

    @Bean
    public Endpoint endpoint(){
        EndpointImpl endpoint = new EndpointImpl(bus, shippingXmlWs);
        endpoint.publish("/delivery-request");
        return endpoint;
    }

}
