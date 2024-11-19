package com.example.batchProcess;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.task.configuration.DefaultTaskConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomTaskConfigurer extends DefaultTaskConfigurer{
	
	@Autowired
	public CustomTaskConfigurer(@Qualifier("batchDataSource") DataSource dataSource, 
			@Value("${spring.cloud.task.table-prefix}") String tablePrefix, 
			ApplicationContext context) {
		super(dataSource, tablePrefix, context);
	}
}
