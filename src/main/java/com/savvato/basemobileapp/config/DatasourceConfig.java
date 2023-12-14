package com.savvato.basemobileapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@Configuration
@ComponentScan({"com.savvato.basemobileapp.*"})
public class DatasourceConfig {

	// CREATE DATABASE basemobileapp_db;
	// CREATE USER 'basemobileappdbuser'@'localhost' IDENTIFIED BY 'supersecure';
	// GRANT ALL PRIVILEGES on basemobileapp_db.* TO 'basemobileappdbuser'@'localhost';

	@Bean(name = "dataSource")
	public DriverManagerDataSource dataSource() {
	    DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
	    driverManagerDataSource.setDriverClassName("com.mysql.jdbc.Driver");
	    driverManagerDataSource.setUrl("jdbc:mysql://localhost:3306/basemobileapp_db?verifyServerCertificate=false&useSSL=true&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
	    driverManagerDataSource.setUsername("basemobileappdbuser");
	    driverManagerDataSource.setPassword("supersecure");
	    return driverManagerDataSource;
	}
}
