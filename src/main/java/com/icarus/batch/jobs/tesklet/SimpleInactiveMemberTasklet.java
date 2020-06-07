package com.icarus.batch.jobs.tesklet;

import com.icarus.batch.member.domain.Member;
import com.icarus.batch.member.domain.enums.UserStatus;
import com.icarus.batch.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@StepScope
@Component
public class SimpleInactiveMemberTasklet implements Tasklet, StepExecutionListener {

    private final MemberRepository memberRepository;
    private final InactiveMemberDataBean<List<Long>> inactiveMemberDataBean;
    private LocalDateTime requestDateTime;
    private List<Long> memberIdxes = new ArrayList<>();

    @Override
    public void beforeStep(StepExecution stepExecution) {
        JobParameters jobParameters = stepExecution.getJobParameters();
        String requestDate = jobParameters.getString("requestDate");

        assert requestDate != null;

        requestDateTime = LocalDateTime.of(
                LocalDate.parse(requestDate, DateTimeFormatter.ofPattern("yyyyMMdd")),
                LocalTime.MIN);
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        //read..
        Page<Member> members = memberRepository
                .findByInactiveMember(requestDateTime, UserStatus.ACTIVE, PageRequest.of(0, 10, Sort.by("idx").ascending()));
        contribution.incrementReadCount();

        //process and write..
        members.get().forEach(member -> {
            member.setInactive();
            memberIdxes.add(member.getIdx());
        });
        inactiveMemberDataBean.put("memberIdxes", memberIdxes);
        contribution.incrementWriteCount(members.getNumberOfElements());

        if (members.hasNext()) {
            return RepeatStatus.CONTINUABLE;
        }

        return RepeatStatus.FINISHED;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("{} readCount..", stepExecution.getReadCount());
        log.info("{} commitCount..", stepExecution.getCommitCount());
        log.info("{} writeCount..", stepExecution.getWriteCount());
        return ExitStatus.COMPLETED;
    }
}
