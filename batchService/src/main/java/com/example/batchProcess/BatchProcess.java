package com.example.batchProcess;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.entity.model.Person;

import jakarta.persistence.EntityManagerFactory;

@Configuration
public class BatchProcess {
	
	private final Resource resource = new ClassPathResource("persone.csv");
	
	@Autowired
	@Qualifier("entityManagerFactory")
	private EntityManagerFactory entityManagerFactory;
    
	@Bean
	@StepScope
	FlatFileItemReader<Person> personItemReader(){
		return new FlatFileItemReaderBuilder<Person>()
				.name("personItemReader")
				.delimited()
				.names("nome","cognome")
				.targetType(Person.class)
				.resource(resource)
				.build();
	}
	
	@Bean
	@StepScope
	ItemProcessor<Person, Person> personItemProcessor(){
		return new PersonEntityItemProcessor();
	}
	
	@Bean
	@StepScope
	JpaItemWriter<Person> jpaItemWriter() {
		return new JpaItemWriterBuilder<Person>()
				.entityManagerFactory(entityManagerFactory)
				.build();
	}
	
	@Bean
	Step step1(@Qualifier("batchJobRepo") JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("step1", jobRepository)
				.<Person, Person>chunk(100, transactionManager)
				.reader(personItemReader())
				.processor(personItemProcessor())
				.writer(jpaItemWriter())
				.build();
	}
	
	@Bean
	Job csvToDb(@Qualifier("batchJobRepo") JobRepository jobRepository, Step step1) {
		return new JobBuilder("csvToDb", jobRepository)
				.start(step1)
				.build();
	}
	
}
