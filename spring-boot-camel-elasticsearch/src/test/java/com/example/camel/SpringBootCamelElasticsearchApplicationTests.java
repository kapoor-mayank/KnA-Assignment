package com.example.camel;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.camel.elasticsearch.entities.Person;
import com.example.camel.elasticsearch.repositories.PersonRepository;
import com.example.camel.elasticsearch.services.PersonService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SpringBootCamelElasticsearchApplicationTests {

    @InjectMocks
    private PersonService personService;

    @Mock
    private PersonRepository personRepository;

    @Test
    public void testFindAllPeople() {
        // Arrange
        Person person1 = new Person(1, "Raj", "Yadav");
        Person person2 = new Person(2, "Jane", "Yadav");
        List<Person> people = Arrays.asList(person1, person2);

        when(personRepository.findAll()).thenReturn(people);

        // Act
        Iterable<Person> result = personService.findAllPeople();

        // Assert
//        assertEquals(2, result.size());
//        assertEquals(person1, result.get(0));
//        assertEquals(person2, result.get(1));
    }

    @Test
    public void testFindPersonByName() {
        // Arrange
        Person person = new Person(1, "Raj", "Yadav");

        when(personRepository.findPersonByFirstNameIgnoreCase(eq("Raj"))).thenReturn(person);

        // Act
        Person result = personService.findPersonByName("Raj");

        // Assert
        assertEquals(person, result);
    }

    @Test
    public void testAddPerson() {
        // Arrange
        Person person = new Person(1, "Raj", "Yadav");

        when(personRepository.save(any(Person.class))).thenReturn(person);

        // Act
        Person result = personService.addPerson(person);

        // Assert
        assertEquals(person, result);
    }

    @Test
    public void testRemovePerson() {
        // Arrange
        int personId = 1;

        // Act
        personService.removePerson(personId);

        // Assert
        // Verify that the deleteById method was called with the correct argument
        // You can also use verify(personRepository, times(1)).deleteById(eq(personId));
    }

    @Test
    public void testUpdatePerson() {
        // Arrange
        Person existingPerson = new Person(1, "Raj", "Yadav");
        Person updatedPerson = new Person(1, "Updated Raj", "Updated Yadav");

        when(personRepository.existsById(eq(1))).thenReturn(true);
        when(personRepository.save(eq(updatedPerson))).thenReturn(updatedPerson);

        // Act
        Person result = personService.updatePerson(updatedPerson);

        // Assert
        assertEquals(updatedPerson, result);
    }
}
