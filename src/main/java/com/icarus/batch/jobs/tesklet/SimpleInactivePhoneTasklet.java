package com.icarus.batch.jobs.tesklet;

import com.icarus.batch.member.domain.MemberPhone;
import com.icarus.batch.member.repository.MemberPhoneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
@StepScope
@Component
public class SimpleInactivePhoneTasklet implements Tasklet {

    private final MemberPhoneRepository memberPhoneRepository;
    private final InactiveMemberDataBean<List<Long>> inactiveMemberDataBean;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        List<Long> memberIdxes = inactiveMemberDataBean.get("memberIdxes");

        List<MemberPhone> memberPhones = memberIdxes.stream()
                .map(memberPhoneRepository::findMemberPhonesByMember_Idx)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        memberPhoneRepository.deleteAll(memberPhones);
        return RepeatStatus.FINISHED;
    }
}
