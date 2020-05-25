package com.icarus.batch.member.repository;

import com.icarus.batch.member.domain.Member;
import com.icarus.batch.member.domain.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUpdatedDateBeforeAndStatusEquals(LocalDateTime localDateTime, UserStatus status);

    @Query("select m from Member m where m.status = :status and m.updatedDate < :lastUpdatedDate")
    Page<Member> findByInactiveMember(LocalDateTime lastUpdatedDate,
                                      UserStatus status,
                                      Pageable pageRequest);
}
