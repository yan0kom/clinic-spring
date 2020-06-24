package ru.yan0kom.clinic.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RoleInDto {
    private String name;
    private List<String> privileges;
}
