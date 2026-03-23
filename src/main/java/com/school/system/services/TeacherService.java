package com.school.system.services;

import com.school.system.models.Teacher;
import com.school.system.repositories.SchoolRepository;
import com.school.system.repositories.TeacherRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {


    private final TeacherRepository teacherRepository;

    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public Teacher create(Teacher teacher) {
        teacherRepository.save(teacher);
        return teacher;
    }

    public List<Teacher> findAll() {
        return teacherRepository.findAll();
    }
    public void deleteById(Integer teacherID) {
        teacherRepository.deleteById(teacherID);
    }
}
