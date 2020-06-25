package ru.yan0kom.clinic.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "Contains parameters to create/update role")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RoleInDto {
    @Schema(description = "Name of the role", example = "doctor", required = true)
    private String name;

    @Schema(description = "List of the role priveleges", example = "[\"clinic.patients.read\", \"clinic.doctors.read\"]", required = true)
    private List<String> privileges;
}
