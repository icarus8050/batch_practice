package com.icarus.batch.member.repository;

import com.icarus.batch.member.domain.Member;
import com.icarus.batch.member.domain.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByUpdatedDateBeforeAndStatusEquals(LocalDateTime localDateTime,
                                                        UserStatus status);
}
