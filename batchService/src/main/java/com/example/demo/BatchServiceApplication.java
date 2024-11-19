package com.example.demo;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@EnableTask
@SpringBootApplication
@ComponentScan(basePackages = "com.example")
@EntityScan(basePackages = "com.example.model")
public class BatchServiceApplication {
    
	public static void main(String[] args) {
		SpringApplication.run(BatchServiceApplication.class, args);
	}
	
	@Configuration
	public static class TaskConfiguration {

		@Autowired
	    @Qualifier("batchJobLauncher")
	    private JobLauncher jobLauncher;

	    @Autowired
	    private Job csvToDb;
	    
		@Bean
		CommandLineRunner commandLineRunner() {
			return new CommandLineRunner() {
				@Override
				public void run(String... args) throws Exception {
					try {
			            JobExecution execution = jobLauncher.run(csvToDb, new JobParameters());
			            System.out.println("Job Execution Status: " + execution.getStatus());
			        } catch (Exception e) {
			            System.err.println("Errore durante l'esecuzione del job: " + e.getMessage());
			        }
				}
			};
		}

	}

}