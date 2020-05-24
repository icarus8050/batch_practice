package com.icarus.batch.member.domain;

import com.icarus.batch.member.domain.enums.Grade;
import com.icarus.batch.member.domain.enums.SocialType;
import com.icarus.batch.member.domain.enums.UserStatus;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(of = {"idx", "email"})
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "ex_member")
@Entity
public class Member implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @OneToMany(mappedBy = "member")
    private List<MemberPhone> phones = new ArrayList<>();

    @Column
    private String name;

    @Column
    private String password;

    @Column
    private String email;

    @Column
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Column
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column
    @Enumerated(EnumType.STRING)
    private Grade grade;

    @Column
    private LocalDateTime createdDate;

    @Column
    private LocalDateTime updatedDate;

    @Builder
    public Member(String name, String password, String email,
                  SocialType socialType, UserStatus status, LocalDateTime createdDate,
                  LocalDateTime updatedDate) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.socialType = socialType;
        this.status = status;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public void setInactive() {
        status = UserStatus.INACTIVE;
    }
}
