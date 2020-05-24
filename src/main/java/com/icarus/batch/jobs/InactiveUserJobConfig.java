package com.icarus.batch.jobs;

import com.icarus.batch.jobs.dto.MemberDto;
import com.icarus.batch.jobs.readers.QuerydslPagingItemReader;
import com.icarus.batch.jobs.readers.QuerydslZeroPagingItemReader;
import com.icarus.batch.member.domain.AwayMember;
import com.icarus.batch.member.domain.Member;
import com.icarus.batch.member.domain.MemberPhone;
import com.icarus.batch.member.domain.enums.UserStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.*;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.icarus.batch.member.domain.QMember.member;
import static com.icarus.batch.member.domain.QMemberPhone.memberPhone;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class InactiveUserJobConfig {

    private static final String JOB_NAME = "inactiveJob";

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
                .start(inactiveMemberJobStep())
                .build();
    }

    @Bean
    @JobScope
    public Step inactiveMemberJobStep() throws Exception {
        return stepBuilderFactory
                .get("inactiveMemberJobStep")
                .transactionManager(memberTx)
                .<MemberDto, Map<String, Object>>chunk(chunkSize)
                .reader(jdbcPagingItemReader())
                .processor(jdbcPagingProcessor())
                .writer(jdbcBatchItemWriter())
                .build();
    }

    /* Jpa Paging Batch */
    @Bean
    @StepScope
    public JpaPagingItemReader<Member> inactiveMemberReader() {
        Map<String, Object> parameterValues = new HashMap<>();
        parameterValues.put("lastUpdatedDate", LocalDateTime.now().minusYears(1L));

        return new JpaPagingItemReaderBuilder<Member>()
                .name("inactiveMemberReader")
                .pageSize(chunkSize)
                .queryString("select m from Member m where m.status = 'ACTIVE' and m.updatedDate < :lastUpdatedDate")
                .parameterValues(parameterValues)
                .entityManagerFactory(memberEmf)
                .build();
    }

    @Bean
    @StepScope
    public QuerydslPagingItemReader<Member> inactiveMemberQuerydslReader() {
        QuerydslPagingItemReader<Member> reader =
                new QuerydslPagingItemReader<>(
                        memberEmf,
                        chunkSize,
                        queryFactory -> queryFactory
                                .selectFrom(member)
                                .leftJoin(member.phones, memberPhone).fetchJoin()
                                .where(
                                        member.status.eq(UserStatus.ACTIVE),
                                        member.updatedDate.before(LocalDateTime.now().minusYears(1L))
                                ).orderBy(member.idx.asc())

                );
        return reader;
    }

    @Bean
    @StepScope
    public QuerydslZeroPagingItemReader<Member> inactiveMemberZeroQuerydslReader() {
        LocalDateTime requestDateTime = jobParameter.getRequestDateTime();

        QuerydslZeroPagingItemReader<Member> reader =
                new QuerydslZeroPagingItemReader<>(
                        memberEmf,
                        chunkSize,
                        queryFactory -> queryFactory
                                .selectFrom(member)
                                .leftJoin(member.phones, memberPhone).fetchJoin()
                                .where(
                                        member.status.eq(UserStatus.ACTIVE),
                                        member.updatedDate.before(requestDateTime.minusYears(1L))
                                ).orderBy(member.idx.asc())
                );
        reader.setTransacted(true);
        return reader;
    }

    @Bean
    @StepScope
    public ItemProcessor<Member, AwayMember> inactiveMemberProcessor() {
        return member -> {
            List<MemberPhone> phones = member.getPhones();

            for (MemberPhone phone : phones) {
                phone.updatePhone("deleted");
            }

            member.setInactive();
            return new AwayMember(member);
        };
    }

    @Bean
    @StepScope
    public JpaItemWriter<AwayMember> inactiveMemberWriter() {
        return new JpaItemWriterBuilder<AwayMember>()
                .entityManagerFactory(memberEmf)
                .build();
    }

    /* JDBC Paging Batch */
    @Bean
    @StepScope
    public JdbcPagingItemReader<MemberDto> jdbcPagingItemReader() throws Exception {
        LocalDateTime requestDateTime = jobParameter.getRequestDateTime().minusYears(1L);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("lastActiveDateTime", requestDateTime);

        return new JdbcPagingItemReaderBuilder<MemberDto>()
                .name("jdbcPagingItemReader")
                .dataSource(dataSource)
                .pageSize(chunkSize)
                .fetchSize(chunkSize)
                .rowMapper(new BeanPropertyRowMapper<>(MemberDto.class))
                .queryProvider(createQueryProvider())
                .parameterValues(parameters)
                .build();
    }

    @Bean
    @StepScope
    public PagingQueryProvider createQueryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource); // Database에 맞는 PagingQueryProvider를 선택하기 위해
        queryProvider.setSelectClause("idx, name, email, socialType, status, grade, createdDate, updatedDate");
        queryProvider.setFromClause("from ex_member.member");
        queryProvider.setWhereClause("where status = 'ACTIVE' and updatedDate <= :lastActiveDateTime");

        Map<String, Order> sortKeys = new HashMap<>(1);
        sortKeys.put("idx", Order.ASCENDING);

        queryProvider.setSortKeys(sortKeys);

        return queryProvider.getObject();
    }

    public ItemProcessor<MemberDto, Map<String, Object>> jdbcPagingProcessor() {
        return item -> {
            Map<String, Object> parameters = new HashMap<>();
            item.setStatus(UserStatus.INACTIVE);
            parameters.put("idx", item.getIdx());
            parameters.put("status", item.getStatus().name());
            return parameters;
        };
    }

    @Bean
    public JdbcBatchItemWriter<Map<String, Object>> jdbcBatchItemWriter() {
        return new JdbcBatchItemWriterBuilder<Map<String, Object>>()
                .dataSource(dataSource)
                .sql("update ex_member.member set status = :status where idx = :idx")
                .columnMapped()
                .build();
    }
}
