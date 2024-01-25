package com.example.jsonxml;

import com.example.jsonxml.MySpringBootApplication;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.MockEndpoints;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

@CamelSpringBootTest
@SpringBootTest(classes = MySpringBootApplication.class)
@MockEndpoints
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class MySpringBootApplicationTest {

    @Autowired
    private CamelContext camelContext;

    @Autowired
    private ProducerTemplate producerTemplate;

    @Autowired
    private ConsumerTemplate consumerTemplate;

    @Test
    public void testXmlToJsonRoute() throws Exception {
        // Arrange
        String xmlInput = "<Customer><firstName>Raj</firstName><lastName>Yadav</lastName></Customer>";

        // Act
        String result = producerTemplate.requestBody("direct:xmlIn", xmlInput, String.class);

        // Assert
        assertEquals("{\"Customer\":{\"firstName\":\"Raj\",\"lastName\":\"Yadav\"}}", result);
    }

    @Test
    public void testJsonToXmlRoute() throws Exception {
        // Arrange
        String jsonInput = "{\"Customer\":{\"firstName\":\"Raj\",\"lastName\":\"Yadav\"}}";

        // Act
        String result = producerTemplate.requestBody("direct:jsonIn", jsonInput, String.class);

        // Assert
        assertEquals("<Customer><firstName>Raj</firstName><lastName>Yadav</lastName></Customer>", result);
    }

    @Test
    public void testXmlToJsonRouteWithMock() throws Exception {
        // Arrange
        String xmlInput = "<Customer><firstName>Raj</firstName><lastName>Yadav</lastName></Customer>";

        // Mock the seda:output endpoint
        MockEndpoint mockEndpoint = camelContext.getEndpoint("seda:output", MockEndpoint.class);
        mockEndpoint.expectedMessageCount(1);

        // Act
        producerTemplate.sendBody("direct:xmlIn", xmlInput);

        // Assert
        mockEndpoint.assertIsSatisfied();

        // Verify the output message
        Exchange exchange = mockEndpoint.getExchanges().get(0);
        String result = exchange.getIn().getBody(String.class);
        assertEquals("{\"Customer\":{\"firstName\":\"Raj\",\"lastName\":\"Yadav\"}}", result);
    }

    @Test
    public void testJsonToXmlRouteWithMock() throws Exception {
        // Arrange
        String jsonInput = "{\"Customer\":{\"firstName\":\"Raj\",\"lastName\":\"Yadav\"}}";

     // Mock the seda:output endpoint with a longer timeout
        MockEndpoint mockEndpoint = camelContext.getEndpoint("seda:output", MockEndpoint.class);
        mockEndpoint.expectedMessageCount(1);
        mockEndpoint.setResultWaitTime(60000); // Set timeout to 60 seconds


        // Act
        producerTemplate.sendBody("direct:jsonIn", jsonInput);

        // Assert
        mockEndpoint.assertIsSatisfied();

        // Verify the output message
        Exchange exchange = mockEndpoint.getExchanges().get(0);
        String result = exchange.getIn().getBody(String.class);
        assertEquals("<Customer><firstName>Raj</firstName><lastName>Yadav</lastName></Customer>", result);
    }
}
