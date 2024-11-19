package com.example.batchProcess;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.example.entity.model.Person;

@Component
public class PersonEntityItemProcessor implements ItemProcessor<Person, Person>{

	@Override
	public Person process(Person item) throws Exception {
		
		item.setNome(item.getNome().toUpperCase());
		item.setCognome(item.getCognome().toUpperCase());
		return item;
	}
	
}
