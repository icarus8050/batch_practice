package com.icarus.batch.member.repository;

import com.icarus.batch.member.domain.MemberPhone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberPhoneRepository extends JpaRepository<MemberPhone, Long> {
}
