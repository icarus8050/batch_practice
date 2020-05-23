package com.icarus.batch.jobs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
public class InactiveMemberJobParameter {

    private LocalDateTime requestDateTime;

    @Value("#{jobParameters[requestDate]}")
    public void setRequestDateTime(String requestDate) {
        this.requestDateTime = LocalDateTime.of(
                LocalDate.parse(requestDate, DateTimeFormatter.ofPattern("yyyyMMdd")),
                LocalTime.MIN
        );
    }
}
