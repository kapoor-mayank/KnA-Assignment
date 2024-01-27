package com.example.camel.elasticsearch.repositories;


import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.example.camel.elasticsearch.entities.Person;

public interface PersonRepository extends ElasticsearchRepository<Person, Integer> {

    Person findPersonByFirstNameIgnoreCase(String name);
}
