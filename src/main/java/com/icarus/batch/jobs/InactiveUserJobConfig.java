package com.icarus.batch.jobs;

import com.icarus.batch.member.domain.Member;
import com.icarus.batch.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class InactiveUserJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final MemberRepository memberRepository;

    @PersistenceUnit(unitName = "member")
    private EntityManagerFactory memberEmf;

    @Qualifier("memberTransactionManager")
    private final PlatformTransactionManager memberTx;

    @Bean
    public Job inactiveUserJob() {
        return jobBuilderFactory
                .get("inactiveJob")
                .start(inactiveMemberJobStep())
                .build();
    }

    @Bean
    @JobScope
    public Step inactiveMemberJobStep() {
        return stepBuilderFactory
                .get("inactiveMemberJobStep")
                .<Member, Member>chunk(10)
                .reader(inactiveMemberReader())
                .processor(inactiveMemberProcessor())
                .writer(inactiveMemberWriter())
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<Member> inactiveMemberReader() {
        Map<String, Object> parameterValues = new HashMap<>();
        parameterValues.put("lastUpdatedDate", LocalDateTime.now().minusYears(1L));

        return new JpaPagingItemReaderBuilder<Member>()
                .name("inactiveMemberReader")
                .pageSize(10)
                .queryString("select m from Member m where m.status = 'ACTIVE' and m.updatedDate < :lastUpdatedDate")
                .parameterValues(parameterValues)
                .entityManagerFactory(memberEmf)
                .transacted(true)
                .build();
    }

    public ItemProcessor<Member, Member> inactiveMemberProcessor() {
        return Member::setInactive;
    }

    @Bean
    @StepScope
    public JpaItemWriter<Member> inactiveMemberWriter() {
        return new JpaItemWriterBuilder<Member>()
                .entityManagerFactory(memberEmf)
                .build();
    }
}
