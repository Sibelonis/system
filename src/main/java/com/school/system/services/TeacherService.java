package com.school.system.services;

import com.school.system.maps.TeacherMapper;
import com.school.system.models.Student;
import com.school.system.models.Subject;
import com.school.system.models.Teacher;
import com.school.system.modelsDTO.TeacherDTO;
import com.school.system.repositories.StudentRepository;
import com.school.system.repositories.SubjectRepository;
import com.school.system.repositories.TeacherRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

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

    public Teacher create(Teacher teacher) {
        teacherRepository.save(teacher);
        return teacher;
    }

    public List<TeacherDTO> findAll() {
        return teacherRepository.findAll()
                .stream()
                .map(teacherMapper::toDto)
                .collect(Collectors.toList());
    }
    public void deleteById(Integer teacherID) {
        teacherRepository.deleteById(teacherID);
    }

    public TeacherDTO addStudentToTeacher(Integer teacherId, Integer studentId) {
        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(()-> new EntityNotFoundException("Teacher not found"));

        Student student = studentRepository.findById(studentId).orElseThrow(()-> new EntityNotFoundException("Student not found"));
        teacher.addStudent(student);
        Teacher saved =  teacherRepository.save(teacher);
        return teacherMapper.toDto(saved);
    }

    public TeacherDTO addSubjectToTeacher(Integer teacherId, String subjectName) {
        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(()-> new EntityNotFoundException("Teacher not found"));
        Subject subject = subjectRepository.findByName(subjectName).orElseThrow(()-> new EntityNotFoundException("Subject not found"));
        teacher.addSubject(subject);
        Teacher saved = teacherRepository.save(teacher);
        return teacherMapper.toDto(saved);
    }
}
