package com.school.system.modelsDTO;

import jakarta.validation.constraints.NotEmpty;

public record SubjectDTO(
        @NotEmpty
        String name,
        String description
) {


}
