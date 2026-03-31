package com.school.system.services;

import com.school.system.maps.StudentMapper;
import com.school.system.models.Student;
import com.school.system.models.Subject;
import com.school.system.modelsDTO.StudentDTO;
import com.school.system.repositories.StudentRepository;
import com.school.system.repositories.SubjectRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final StudentMapper studentMapper;

    public StudentService(StudentRepository studentRepository, SubjectRepository subjectRepository, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
        this.studentMapper = studentMapper;
    }

    public StudentDTO create(StudentDTO student) {
        var studentDTO = studentMapper.toEntity(student);
        Student saved= studentRepository.save(studentDTO);
        return studentMapper.toDTO(saved);
    }

    public List<StudentDTO> findAll() {

        return studentRepository.findAll()
                .stream()
                .map(studentMapper::toDTO)
                .collect(Collectors.toList());
    }
    public void deleteById(Integer studentID) {
        studentRepository.deleteById(studentID);
    }

    public StudentDTO addSubjectToStudent(Integer studentId, String subjectName) {
        Student student = studentRepository.findById(studentId).orElseThrow(()-> new EntityNotFoundException("Student not found"));
        Subject subject = subjectRepository.findByName(subjectName).orElseThrow(()-> new EntityNotFoundException("Subject not found"));

        student.addSubject(subject);
        subject.addStudent(student);
        subjectRepository.save(subject);
        Student savedStudent = studentRepository.save(student);

        return studentMapper.toDTO(savedStudent);
    }
}
