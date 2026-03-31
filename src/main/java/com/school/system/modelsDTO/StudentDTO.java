package com.school.system.modelsDTO;

import java.util.List;

public record StudentDTO(

        String firstName,
        String lastName,
        String homeRoom,
        TeacherDTO teacher,
        List<SubjectDTO> subjects

) {
}
