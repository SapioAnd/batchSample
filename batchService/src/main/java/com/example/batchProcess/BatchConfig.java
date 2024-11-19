package com.example.batchProcess;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.batch.BatchTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.jdbc.support.JdbcTransactionManager;

@Configuration
public class BatchConfig extends DefaultBatchConfiguration {

	@Autowired
	@Qualifier("batchDataSource")
	private DataSource batchDataSource;

	@Override
	protected DataSource getDataSource() {
		return batchDataSource;
	}
	
	@Value("${spring.batch.jdbc.table-prefix}") 
	private String batchPrefix;

	@Bean("batchJobRepo")
	JobRepository jobRepository(JdbcTransactionManager batchTransactionManager) throws Exception {
		JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
		factory.setTablePrefix(batchPrefix);
		factory.setDataSource(batchDataSource);
		factory.setTransactionManager(batchTransactionManager);
		factory.afterPropertiesSet();
		return factory.getObject();
	}

	@Bean
	JobExplorer jobExplorer(@Qualifier("batchDataSource") DataSource batchDataSource)
			throws Exception {
		JobExplorerFactoryBean factoryBean = new JobExplorerFactoryBean();
		factoryBean.setDataSource(batchDataSource);
		factoryBean.setTablePrefix(batchPrefix);
		return factoryBean.getObject();
	}

	@Bean("batchJobLauncher")
	JobLauncher jobLauncher(@Qualifier("batchJobRepo") JobRepository jobRepository) throws Exception {
		TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
		jobLauncher.setJobRepository(jobRepository);
		jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
		jobLauncher.afterPropertiesSet();
		return jobLauncher;
		
	}

	
}
