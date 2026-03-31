package com.school.system.maps;


import com.school.system.models.Teacher;
import com.school.system.modelsDTO.TeacherDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TeacherMapper {

    TeacherDTO toDto(Teacher teacher);

    @Mapping(target = "school", ignore = true)
    Teacher toEntity(TeacherDTO teacherDTO);
}
