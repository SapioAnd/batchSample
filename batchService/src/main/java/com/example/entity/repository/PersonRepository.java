package com.example.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long>{

}
