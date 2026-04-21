package com.school.system.modelsDTO;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SchoolDTO (
        @NotNull
        String schoolName,
        @NotNull
        String degree,
        List<StudentDTO> students,
        List<TeacherDTO> teachers

) {
}
