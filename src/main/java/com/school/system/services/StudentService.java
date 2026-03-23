package com.school.system.services;

import com.school.system.models.Student;
import com.school.system.repositories.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student create(Student student) {
        studentRepository.save(student);
        return student;
    }

    public List<Student> findAll() {

        return studentRepository.findAll();
    }
    public void deleteById(Integer studentID) {
        studentRepository.deleteById(studentID);
    }
}
