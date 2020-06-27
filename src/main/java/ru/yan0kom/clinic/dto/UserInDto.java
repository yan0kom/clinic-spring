package ru.yan0kom.clinic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class UserInDto {
    private Long id;
    private String username;
    @ToString.Exclude
    private String password;
    private Long roleId;
}
