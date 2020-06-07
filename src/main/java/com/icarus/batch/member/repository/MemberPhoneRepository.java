package com.icarus.batch.member.repository;

import com.icarus.batch.member.domain.MemberPhone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberPhoneRepository extends JpaRepository<MemberPhone, Long> {

    List<MemberPhone> findMemberPhonesByMember_Idx(Long memberIdx);
}
