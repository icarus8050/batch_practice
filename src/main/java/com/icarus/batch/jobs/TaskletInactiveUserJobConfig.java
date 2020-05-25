package com.icarus.batch.jobs;

import com.icarus.batch.jobs.tesklet.SimpleInactiveTasklet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.sql.DataSource;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class TaskletInactiveUserJobConfig {
    private static final String JOB_NAME = "taskletInactiveJob";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final InactiveMemberJobParameter jobParameter;

    @Qualifier("memberDataSource")
    private final DataSource dataSource;

    @PersistenceUnit(unitName = "member")
    private EntityManagerFactory memberEmf;

    @Qualifier("memberTransactionManager")
    private final PlatformTransactionManager memberTx;

    @Value("${chunkSize:10}")
    private int chunkSize;

    @Bean
    @JobScope
    public InactiveMemberJobParameter jobParameter() {
        return new InactiveMemberJobParameter();
    }

    @Bean
    public Job inactiveUserJob() throws Exception {
        return jobBuilderFactory
                .get(JOB_NAME)
                .start(inactiveStep(null))
                .build();
    }

    @Bean
    @JobScope
    public Step inactiveStep(SimpleInactiveTasklet simpleInactiveTasklet) {
        return stepBuilderFactory.get("inactiveStep")
                .transactionManager(memberTx)
                .tasklet(simpleInactiveTasklet)
                .build();
    }
}
