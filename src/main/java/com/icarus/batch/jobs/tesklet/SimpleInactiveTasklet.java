package com.icarus.batch.jobs.tesklet;

import com.icarus.batch.jobs.InactiveMemberJobParameter;
import com.icarus.batch.member.domain.Member;
import com.icarus.batch.member.domain.enums.UserStatus;
import com.icarus.batch.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@StepScope
@Component
public class SimpleInactiveTasklet implements Tasklet {

    private final MemberRepository memberRepository;
    private final InactiveMemberJobParameter jobParameter;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        LocalDateTime requestDateTime = jobParameter.getRequestDateTime();
        Page<Member> members = memberRepository
                .findByInactiveMember(requestDateTime, UserStatus.ACTIVE, PageRequest.of(0, 10, Sort.by("idx").ascending()));

        members.get().parallel().forEach(Member::setInactive);

        if (members.hasNext()) {
            return RepeatStatus.CONTINUABLE;
        }

        return RepeatStatus.FINISHED;
    }
}
