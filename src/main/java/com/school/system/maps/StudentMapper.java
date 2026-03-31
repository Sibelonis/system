package com.school.system.maps;


import com.school.system.models.Student;
import com.school.system.modelsDTO.StudentDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    StudentDTO toDTO(Student student);

//    @Mapping(target = "school", ignore = true)
    Student  toEntity(StudentDTO studentDTO);
}
