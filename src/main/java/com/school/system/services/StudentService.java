package com.school.system.services;

import com.school.system.models.Student;
import com.school.system.models.Subject;
import com.school.system.repositories.StudentRepository;
import com.school.system.repositories.SubjectRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;

    public StudentService(StudentRepository studentRepository, SubjectRepository subjectRepository) {
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
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

    public Student addSubjectToStudent(Integer studentId, String subjectName) {
        Student student = studentRepository.findById(studentId).orElseThrow(()-> new EntityNotFoundException("Student not found"));
        Subject subject = subjectRepository.findByName(subjectName).orElseThrow(()-> new EntityNotFoundException("Subject not found"));

        student.addSubject(subject);
        subject.addStudent(student);
        subjectRepository.save(subject);
        return studentRepository.save(student);
    }
}
