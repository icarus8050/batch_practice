package com.icarus.batch.config.database;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
)
public class BatchDataSourceConfig {

    /*@Bean(name = "batchDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.hikari.batch")
    public DataSource batchDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }*/
}
