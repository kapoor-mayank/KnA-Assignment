package com.example.camel.elasticsearch.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import com.example.camel.elasticsearch.entities.Person;
import com.example.camel.elasticsearch.services.PersonService;

@Component
public class PersonRoute extends RouteBuilder {


    private final Environment env;

    public PersonRoute(Environment env) {
        this.env = env;
    }

    public void configure() throws Exception {

        restConfiguration()
                .contextPath(env.getProperty("camel.component.servlet.mapping.contextPath", "/rest/*"))
                .apiContextPath("/api-doc")
                .apiProperty("api.title", "Spring Boot Camel Postgres Rest API.")
                .apiProperty("api.version", "1.0")
                .apiProperty("cors", "true")
                .apiContextRouteId("doc-api")
                .port(env.getProperty("server.port", "8080"))
                .bindingMode(RestBindingMode.json);

        rest("/person")
                .consumes(MediaType.APPLICATION_JSON_VALUE)
                .produces(MediaType.APPLICATION_JSON_VALUE)
                .get("/{name}").route()
                .to("{{route.findPersonByName}}")
                .endRest()
                .get("/").route()
                .to("{{route.findAllPeople}}")
                .endRest()
                .post("/").route()
                .marshal().json()
                .unmarshal(getJacksonDataFormat(Person.class))
                .to("{{route.savePerson}}")
                .endRest()
                .put("/{personId}").route()
                .marshal().json()
                .unmarshal(getJacksonDataFormat(Person.class))
                .to("{{route.updatePerson}}")
                .endRest()
                .delete("/{personId}").route()
                .to("{{route.removePerson}}")
                .end();

        from("{{route.findPersonByName}}")
                .log("Received header : ${header.name}")
                .bean(PersonService.class, "findPersonByName(${header.name})");

        from("{{route.findAllPeople}}")
                .bean(PersonService.class, "findAllPeople");


        from("{{route.savePerson}}")
                .log("Received Body ${body}")
                .bean(PersonService.class, "addPerson(${body})");
        
        from("{{route.updatePerson}}")
        .log("Received Body ${body}")
        .bean(PersonService.class, "updatePerson(${body})");

        from("{{route.removePerson}}")
                .log("Received header : ${header.personId}")
                .bean(PersonService.class, "removePerson(${header.personId})");
    }

    private JacksonDataFormat getJacksonDataFormat(Class<?> unmarshalType) {
        JacksonDataFormat format = new JacksonDataFormat();
        format.setUnmarshalType(unmarshalType);
        return format;
    }
}
