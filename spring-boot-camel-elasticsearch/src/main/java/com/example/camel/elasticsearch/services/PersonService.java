package com.example.camel.elasticsearch.services;

import org.springframework.stereotype.Service;

import com.example.camel.elasticsearch.entities.Person;
import com.example.camel.elasticsearch.repositories.PersonRepository;

import java.util.List;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Iterable<Person> findAllPeople() {
        return personRepository.findAll();
    }

    public Person findPersonByName(String name) {
        return personRepository.findPersonByFirstNameIgnoreCase(name);
    }

    public Person addPerson(Person person) {
        return personRepository.save(person);
    }

    public void removePerson(int personId) {
    	personRepository.deleteById(personId);
    }
    
    public Person updatePerson(Person updatedPerson) {
        // Check if the book with the given ID exists
        if (personRepository.existsById(updatedPerson.getId())) {
           
            return personRepository.save(updatedPerson);
        } else {
            return null;
        }
    }
}
