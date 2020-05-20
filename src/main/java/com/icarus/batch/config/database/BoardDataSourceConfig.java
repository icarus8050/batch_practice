package com.icarus.batch.config.database;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Objects;

@EnableJpaRepositories(
        basePackages = {"com.icarus.batch.board"},
        entityManagerFactoryRef = "boardEntityManagerFactory",
        transactionManagerRef = "boardTransactionManager"
)
@Configuration
public class BoardDataSourceConfig {

    @Bean(name = "boardDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.hikari.board")
    public DataSource boardDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean(name = "boardEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean boardEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder.dataSource(boardDataSource())
                .packages("com.icarus.batch")
                .persistenceUnit("board")
                .build();
    }

    @Bean(name = "boardTransactionManager")
    public PlatformTransactionManager boardTransactionManager(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(Objects.requireNonNull(boardEntityManagerFactory(builder).getObject()));
    }
}
