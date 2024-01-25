package com.example.jsonxml;

import org.apache.camel.builder.RouteBuilder;
//import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.component.jacksonxml.JacksonXMLDataFormat;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.SerializationFeature;
 
@Component
public class XmlToJsonRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        JacksonXMLDataFormat jacksonDataFormat = new JacksonXMLDataFormat();
        jacksonDataFormat.setPrettyPrint(true);
        jacksonDataFormat.enableFeature(SerializationFeature.WRAP_ROOT_VALUE);
        
     // XML to JSON route
//         from("direct:xmlIn").log( "POJO Body ${body}").marshal(jacksonDataFormat)
//         .log("XML Marshalled Body: ${body}").unmarshal(jacksonDataFormat).
//                    log("Unmarshalled JSON Body: ${body}").
//                       to("seda:output");
        from("direct:xmlIn")
        .log("POJO Body ${body}")
        .marshal(jacksonDataFormat)
        .log("XML Marshalled Body: ${body}")
        .unmarshal(jacksonDataFormat).
      log("Unmarshalled JSON Body: ${body}")
        .to("seda:output");
        
      // JSON to XML route
         from("direct:jsonIn")
             .log("JSON Body ${body}")
             .marshal(jacksonDataFormat)
             .log("JSON Marshalled Body: ${body}")
//             .unmarshal(jacksonDataFormat)
//             .log("Unmarshalled XML Body: ${body}")
             .to("seda:output");
    }
}