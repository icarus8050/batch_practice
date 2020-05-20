package com.icarus.batch.jobs.writer;

import com.icarus.batch.member.domain.Member;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@StepScope
@Component
public class InactiveMemeberWriter extends JpaItemWriter<Member> {

    @Override
    @Transactional("memberTransactionManager")
    public void write(List<? extends Member> items) {
        super.write(items);
    }
}
