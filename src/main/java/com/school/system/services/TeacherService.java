package com.school.system.services;

import com.school.system.errorHandling.BadRequestException;
import com.school.system.maps.TeacherMapper;
import com.school.system.models.Student;
import com.school.system.models.Subject;
import com.school.system.models.Teacher;
import com.school.system.modelsDTO.TeacherDTO;
import com.school.system.repositories.StudentRepository;
import com.school.system.repositories.SubjectRepository;
import com.school.system.repositories.TeacherRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherService {


    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherMapper teacherMapper;

    public TeacherService(TeacherRepository teacherRepository, StudentRepository studentRepository, SubjectRepository subjectRepository, TeacherMapper teacherMapper) {
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
        this.teacherMapper = teacherMapper;
    }

    public TeacherDTO create(TeacherDTO teacherDTO) {
        if (teacherDTO == null) throw new BadRequestException("Request body is required");
        if (teacherDTO.firstName() == null || teacherDTO.lastName() == null) {
            throw new BadRequestException("Teacher first name and last name are required");
        }
            var teacher = teacherMapper.toEntity(teacherDTO);
            Teacher saved = teacherRepository.save(teacher);
            return teacherMapper.toDto(saved);

    }

    public List<TeacherDTO> findAll() {
        return teacherRepository.findAll()
                .stream()
                .map(teacherMapper::toDto)
                .collect(Collectors.toList());
    }
    public void deleteById(Integer teacherID) {
        var teacher = teacherRepository.findById(teacherID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Teacher not found: " + teacherID));

        if (teacher.getSubject() != null) {
            var subject = teacher.getSubject();
            subject.setTeacher(null);
            subjectRepository.save(subject);
            teacher.setSubject(null);
        }

        var students = teacher.getStudents();
        if (students != null && !students.isEmpty()) {
            students.forEach(s -> s.setTeacher(null));
            studentRepository.saveAll(students);
            teacher.getStudents().clear();
        }

        teacherRepository.deleteById(teacherID);
    }

    public TeacherDTO addStudentToTeacher(Integer teacherId, Integer studentId) {
        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(()-> new BadRequestException("Teacher not found. Make sure teacher exists"));
        Student student = studentRepository.findById(studentId).orElseThrow(()-> new BadRequestException("Student not found. Make sure student exists."));

        teacher.addStudent(student);
        Teacher saved = teacherRepository.save(teacher);
        return teacherMapper.toDto(saved);
    }

    public TeacherDTO addSubjectToTeacher(Integer teacherId, String subjectName) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(()-> new BadRequestException("Teacher not found. Make sure teacher exists."));
        Subject subject = subjectRepository.findByName(subjectName)
                .orElseThrow(()-> new BadRequestException("Subject not found. Make sure you are using existing subject name."));

        teacher.addSubject(subject);
        Teacher saved = teacherRepository.save(teacher);
        return teacherMapper.toDto(saved);
    }
}
