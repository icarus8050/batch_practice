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

    private Long memberIdx;

    private String title;

    private String content;

    private Boolean status = Boolean.TRUE;
}
