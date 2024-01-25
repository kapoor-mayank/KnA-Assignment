package com.example.camel.postgresql.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.camel.postgresql.entities.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

    Person findPersonByFirstNameIgnoreCase(String name);
}
