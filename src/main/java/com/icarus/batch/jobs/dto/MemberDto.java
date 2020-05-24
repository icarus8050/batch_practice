package com.icarus.batch.jobs.dto;

import com.icarus.batch.member.domain.enums.Grade;
import com.icarus.batch.member.domain.enums.SocialType;
import com.icarus.batch.member.domain.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {

    private Long idx;
    private String name;
    private String email;
    private SocialType socialType;
    private UserStatus status;
    private Grade grade;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
