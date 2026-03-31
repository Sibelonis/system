package com.school.system.maps;

import com.school.system.models.School;
import com.school.system.modelsDTO.SchoolDTO;
import org.mapstruct.Mapper;

@Mapper (componentModel = "spring", uses = {StudentMapper.class, TeacherMapper.class})
public interface SchoolMapper {
//
//    @Mapping(target = "students", source = "students")
//    @Mapping(target = "teachers", source = "teachers")
    SchoolDTO toDto(School school);
//
//    @Mapping(target = "students", source = "students")
//    @Mapping(target = "teachers", source = "teachers")
    School toEntity(SchoolDTO schoolDTO);
}
