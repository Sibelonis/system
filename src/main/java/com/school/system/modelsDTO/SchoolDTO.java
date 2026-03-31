package com.school.system.modelsDTO;

import java.util.List;

public record SchoolDTO (

        String schoolName,
        String degree,
        List<StudentDTO> students,
        List<TeacherDTO> teachers

) {

}
