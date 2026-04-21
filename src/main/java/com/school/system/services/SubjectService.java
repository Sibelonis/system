package com.school.system.services;


import com.school.system.maps.SubjectMapper;
import com.school.system.models.Subject;
import com.school.system.modelsDTO.SubjectDTO;
import com.school.system.repositories.SubjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final SubjectMapper subjectMapper;

    public SubjectService(SubjectRepository subjectRepository, SubjectMapper subjectMapper) {
        this.subjectRepository = subjectRepository;
        this.subjectMapper = subjectMapper;
    }

    public SubjectDTO create(SubjectDTO subjectDTO) {
        if (subjectDTO.name() != null ){
            var subject = subjectMapper.toEntity(subjectDTO);
            Subject saved = subjectRepository.save(subject);
            return subjectMapper.toDto(saved);
        }
        return null;
    }
    public List<SubjectDTO> findAll() {
        return subjectRepository.findAll()
                .stream()
                .map(subjectMapper::toDto)
                .collect(Collectors.toList());
    }

    public void deleteById(Integer subjectID) {
        subjectRepository.deleteById(subjectID);
    }
}

