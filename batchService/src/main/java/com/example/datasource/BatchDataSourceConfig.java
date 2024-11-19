package com.example.datasource;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.batch.BatchTransactionManager;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.JdbcTransactionManager;

@Configuration
@ConfigurationProperties(prefix = "batch.datasource")
public class BatchDataSourceConfig {

	private String url;
	private String username;
	private String password;
	private String driverClassName;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	@Bean(name = "batchDataSource")
	DataSource batchDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setUrl(this.url);
		dataSource.setUsername(this.username);
		dataSource.setPassword(this.password);
		dataSource.setDriverClassName(this.driverClassName);
		return dataSource;
	}
	
	@Bean("batchTransactionManager")
	@BatchTransactionManager
	JdbcTransactionManager batchTransactionManager(DataSource batchDataSource) {
		return new JdbcTransactionManager(batchDataSource);
	}
}
