package com.school.system.services;


import com.school.system.models.Subject;
import com.school.system.repositories.SubjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public Subject create(Subject subject) {
        subjectRepository.save(subject);
        return subject;
    }
    public List<Subject> findAll() {
        return subjectRepository.findAll();
    }

    public void deleteById(Integer subjectID) {
        subjectRepository.deleteById(subjectID);
    }
}

