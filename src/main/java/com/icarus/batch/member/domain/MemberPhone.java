package com.icarus.batch.member.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@EqualsAndHashCode(of = {"idx"})
@NoArgsConstructor
@Table(schema = "ex_member")
@Entity
public class MemberPhone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_idx", nullable = false)
    private Member member;

    private String phone;

    public void updatePhone(String phone) {
        this.phone = phone;
    }

    public MemberPhone(Member member, String phone) {
        this.member = member;
        this.phone = phone;
    }
}
