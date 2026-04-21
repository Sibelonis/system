package com.school.system.services;

import com.school.system.errorHandling.BadRequestException;
import com.school.system.maps.StudentMapper;
import com.school.system.models.Student;
import com.school.system.models.Subject;
import com.school.system.modelsDTO.StudentDTO;
import com.school.system.repositories.StudentRepository;
import com.school.system.repositories.SubjectRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public StudentDTO create(StudentDTO studentDto) {
        if (studentDto == null) throw new BadRequestException("Request body is required");
        if (studentDto.firstName() == null || studentDto.lastName() == null) {
            throw new BadRequestException("Students first name and last name are required");
        }
            var studentDTO = studentMapper.toEntity(studentDto);
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
        if (studentRepository.findById(studentID).isPresent()) {
            studentRepository.deleteById(studentID);
        }else  {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public StudentDTO addSubjectToStudent(Integer studentId, String subjectName) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(()-> new BadRequestException("Invalid student: " + studentId + "\n Please use real student ID."));
        Subject subject = subjectRepository.findByName(subjectName)
                .orElseThrow(()-> new BadRequestException("Invalid subject: " + subjectName + "\n Please use real subject name."));

        student.addSubject(subject);
        subject.addStudent(student);
        subjectRepository.save(subject);
        Student savedStudent = studentRepository.save(student);

        return studentMapper.toDTO(savedStudent);
    }
}
