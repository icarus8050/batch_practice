package com.icarus.batch.domain;

import com.icarus.batch.domain.enums.Grade;
import com.icarus.batch.domain.enums.SocialType;
import com.icarus.batch.domain.enums.UserStatus;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode(of = {"idx", "email"})
@NoArgsConstructor
@Entity
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column
    private String name;

    @Column
    private String password;

    @Column
    private String email;

    @Column
    private String principal;

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
    private LocalDateTime createDate;

    @Column
    private LocalDateTime updatedDate;

    @Builder
    public User(String name, String password, String email, String principal,
                SocialType socialType, UserStatus status, LocalDateTime createDate,
                LocalDateTime updatedDate) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.principal = principal;
        this.socialType = socialType;
        this.status = status;
        this.createDate = createDate;
        this.updatedDate = updatedDate;
    }

    public User setInactive() {
        status = UserStatus.INACTIVE;
        return this;
    }
}
