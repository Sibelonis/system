package com.school.system.maps;


import com.school.system.models.Student;
import com.school.system.modelsDTO.StudentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    @Mapping(target = "teacher", ignore = true)
    StudentDTO toDTO(Student student);

    @Mapping(target = "school", ignore = true)
    @Mapping(target = "teacher", ignore = true)
    Student  toEntity(StudentDTO studentDTO);
}
