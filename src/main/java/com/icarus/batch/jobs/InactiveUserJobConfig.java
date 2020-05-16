package com.icarus.batch.jobs;

import com.icarus.batch.domain.User;
import com.icarus.batch.domain.enums.UserStatus;
import com.icarus.batch.jobs.readers.QuerydslPagingItemReader;
import com.icarus.batch.jobs.readers.QueueItemReader;
import com.icarus.batch.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.List;

import static com.icarus.batch.domain.QUser.user;

@RequiredArgsConstructor
@Configuration
public class InactiveUserJobConfig {

    private final UserRepository userRepository;
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
    @StepScope
    public QueueItemReader<User> inactiveUserReader() {
        List<User> oldUsers =
                userRepository.findByUpdatedDateBeforeAndStatusEquals(
                        LocalDateTime.now().minusYears(1), UserStatus.ACTIVE);
        return new QueueItemReader<>(oldUsers);
    }

    @Bean
    public QuerydslPagingItemReader<User> querydslPagingItemReader() {
        return new QuerydslPagingItemReader<>(
                entityManagerFactory, 5,
                queryFactory -> queryFactory
                        .selectFrom(user)
                        .where(user.createdDate.before(LocalDateTime.now()))
                        .orderBy(user.idx.asc()));
    }

    public ItemProcessor<User, User> inactiveUserProcessor() {
        return User::setInactive;
    }

    public ItemWriter<User> inactiveUserWriter() {
        return ((List<? extends User> users) -> userRepository.saveAll(users));
    }
}
