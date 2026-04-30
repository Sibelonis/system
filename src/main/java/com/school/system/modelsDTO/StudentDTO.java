package com.school.system.modelsDTO;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record StudentDTO(
        @NotNull
        String firstName,
        @NotNull
        String lastName,
        String homeRoom,
        List<SubjectDTO> subjects

) {
}
