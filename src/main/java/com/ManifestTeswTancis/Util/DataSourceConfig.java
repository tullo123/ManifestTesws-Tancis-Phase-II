package com.ManifestTeswTancis.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig { // Added 21/04/2021
    @Autowired
    private  Environment env;

    @Bean
    @Primary
    public DataSource tancisExternalDataSourceConfiguration(){
        DriverManagerDataSource datasource = new DriverManagerDataSource();
        datasource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        datasource.setUrl(env.getProperty("spring.datasource.url"));
        datasource.setUsername(env.getProperty("spring.datasource.username"));
        datasource.setPassword(env.getProperty("spring.datasource.password"));
        return datasource;
    }
    @Bean
    public DataSource tancisInternalDataSourceConfiguration(){
        DriverManagerDataSource datasource = new DriverManagerDataSource();
        datasource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        datasource.setUrl(env.getProperty("oracle.datasource.url"));
        datasource.setUsername(env.getProperty("oracle.datasource.username"));
        datasource.setPassword(env.getProperty("oracle.datasource.password"));
        return datasource;

    }
}
