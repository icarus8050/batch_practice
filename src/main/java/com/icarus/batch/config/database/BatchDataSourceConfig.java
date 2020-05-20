package com.icarus.batch.config.database;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories
public class BatchDataSourceConfig {

    /*@Bean(name = "batchDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.hikari.batch")
    public DataSource batchDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }*/
}
