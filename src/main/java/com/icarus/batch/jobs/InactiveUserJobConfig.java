package com.icarus.batch.jobs;

import com.icarus.batch.domain.User;
import com.icarus.batch.domain.enums.UserStatus;
import com.icarus.batch.jobs.readers.QuerydslPagingItemReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;

import static com.icarus.batch.domain.QUser.user;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class InactiveUserJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public Job inactiveUserJob() {
        return jobBuilderFactory.get("inactiveUserJob")
                .preventRestart()
                .start(inactiveJobStep())
                .build();
    }

    @Bean
    public Step inactiveJobStep() {
        return stepBuilderFactory.get("inactiveUserStep")
                .<User, User>chunk(10)
                .reader(querydslPagingItemReader())
                .processor(inactiveUserProcessor())
                .writer(inactiveUserWriter())
                .build();
    }

    @Bean
    public QuerydslPagingItemReader<User> querydslPagingItemReader() {
        return new QuerydslPagingItemReader<>(
                entityManagerFactory, 10,
                queryFactory -> queryFactory
                        .selectFrom(user)
                        .where(user.status.eq(UserStatus.ACTIVE),
                                user.createdDate.before(LocalDateTime.now().minusYears(1L)))
                        .orderBy(user.idx.asc()));
    }

    public ItemProcessor<User, User> inactiveUserProcessor() {
        return User::setInactive;
    }

    @Bean
    public JpaItemWriter<User> inactiveUserWriter() {
        return new JpaItemWriterBuilder<User>().entityManagerFactory(entityManagerFactory).build();
    }
}
