package ru.yan0kom.clinic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserInDto {
    private Long id;
    private String username;
    private String password;
    private Long roleId;
}
