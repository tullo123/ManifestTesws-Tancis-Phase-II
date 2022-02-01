package com.ManifestTeswTancis.Util;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
public class DataSourceConfig { // Added 21/04/2021
    private final Environment env;

    public DataSourceConfig(Environment env) {
        this.env = env;
    }

    @Bean
    @Primary
    public DataSource tancisExternalDataSourceConfiguration(){
        DriverManagerDataSource datasource = new DriverManagerDataSource();
        datasource.setDriverClassName(Objects.requireNonNull(env.getProperty("spring.datasource.driver-class-name")));
        datasource.setUrl(env.getProperty("spring.datasource.url"));
        datasource.setUsername(env.getProperty("spring.datasource.username"));
        datasource.setPassword(env.getProperty("spring.datasource.password"));
        return datasource;
    }
    @Bean
    public DataSource tancisInternalDataSourceConfiguration(){
        DriverManagerDataSource datasource = new DriverManagerDataSource();
        datasource.setDriverClassName(Objects.requireNonNull(env.getProperty("spring.datasource.driver-class-name")));
        datasource.setUrl(env.getProperty("oracle.datasource.url"));
        datasource.setUsername(env.getProperty("oracle.datasource.username"));
        datasource.setPassword(env.getProperty("oracle.datasource.password"));
        return datasource;

    }
    @Bean
    public DataSource itaxDataSourceConfiguration(){
        DriverManagerDataSource datasource = new DriverManagerDataSource();
        datasource.setDriverClassName(Objects.requireNonNull(env.getProperty("spring.datasource.driver-class-name")));
        datasource.setUrl(env.getProperty("oracle.datasource2.url"));
        datasource.setUsername(env.getProperty("oracle.datasource2.username"));
        datasource.setPassword(env.getProperty("oracle.datasource2.password"));
        return datasource;

    }
}
