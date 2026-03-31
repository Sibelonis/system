package com.school.system.maps;

import com.school.system.models.Subject;
import com.school.system.modelsDTO.SubjectDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubjectMapper {

    SubjectDTO toDto(Subject subject);

    @Mapping(target = "students", ignore = true)
    Subject toEntity(SubjectDTO subjectDTO);
}
