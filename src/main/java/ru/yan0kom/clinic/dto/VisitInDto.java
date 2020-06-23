package ru.yan0kom.clinic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class VisitInDto {
    private LocalDateTime visitDateTime;
    private Long patientId;
    private Long doctorId;
}
