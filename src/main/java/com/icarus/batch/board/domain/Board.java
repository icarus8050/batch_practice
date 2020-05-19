package com.icarus.batch.board.domain;

import com.icarus.batch.member.domain.Member;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@EqualsAndHashCode(of = {"idx"})
@NoArgsConstructor
@Table(schema = "ex_board")
@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_idx")
    private Member member;

    private String title;

    private String content;

    private Boolean status = Boolean.TRUE;
}
