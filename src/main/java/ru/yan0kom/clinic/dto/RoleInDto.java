package ru.yan0kom.clinic.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.yan0kom.clinic.validation.PrivilegesListConstraint;

@Schema(description = "Contains parameters to create/update role")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RoleInDto {
    @Schema(description = "Name of the role", example = "doctor", required = true)
    @NotBlank
    @Size(min = 1, max = 100)
    private String name;

    @Schema(description = "List of the role priveleges", example = "[\"clinic.patients.read\", \"clinic.doctors.read\"]", required = true)
    @PrivilegesListConstraint
    private List<String> privileges;
}
