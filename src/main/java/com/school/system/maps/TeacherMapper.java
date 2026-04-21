package com.school.system.maps;


import com.school.system.models.Teacher;
import com.school.system.modelsDTO.TeacherDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = {StudentMapper.class, SubjectMapper.class})
public interface TeacherMapper {
    @Mapping(target = "students", source = "students")
    TeacherDTO toDto(Teacher teacher);

    @Mapping(target = "school", ignore = true)
    Teacher toEntity(TeacherDTO teacherDTO);
}
