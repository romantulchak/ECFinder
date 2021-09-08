package com.ecfinder.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class ECFinderConfig {

    private final ApplicationContext applicationContext;

    @Autowired
    public ECFinderConfig(ApplicationContext applicationContext){
        this.applicationContext = applicationContext;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(){
        return new JdbcTemplate(applicationContext.getBean(DataSource.class));
    }

}
