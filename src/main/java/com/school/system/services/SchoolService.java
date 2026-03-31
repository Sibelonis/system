package com.school.system.services;

import com.school.system.maps.SchoolMapper;
import com.school.system.models.School;
import com.school.system.models.Student;
import com.school.system.models.Teacher;
import com.school.system.modelsDTO.SchoolDTO;
import com.school.system.repositories.SchoolRepository;
import com.school.system.repositories.StudentRepository;
import com.school.system.repositories.TeacherRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SchoolService {

    private final SchoolRepository schoolRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final SchoolMapper schoolMapper;

    public SchoolService(SchoolRepository schoolRepository, StudentRepository studentRepository, TeacherRepository teacherRepository, SchoolMapper schoolMapper) {
        this.schoolRepository = schoolRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.schoolMapper = schoolMapper;
    }

    public SchoolDTO addStudentToSchool(Integer schoolId, Integer studentId) {
        School school = schoolRepository.findById(schoolId).orElseThrow(()-> new EntityNotFoundException("School not found"));

        Student student = studentRepository.findById(studentId).orElseThrow(()-> new EntityNotFoundException("Student not found"));

        school.addStudent(student);
        School saved = schoolRepository.save(school);
        return schoolMapper.toDto(saved);
    }


    public SchoolDTO addTeacherToSchool(Integer schoolId, Integer teacherId) {
        School school = schoolRepository.findById(schoolId).orElseThrow(()-> new EntityNotFoundException("School not found"));

        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(()-> new EntityNotFoundException("Teacher not found"));
        school.addTeacher(teacher);
        School saved = schoolRepository.save(school);
        return schoolMapper.toDto(saved);
    }

    public SchoolDTO create(SchoolDTO school) {
        var schoolDto = schoolMapper.toEntity(school);
        School saved = schoolRepository.save(schoolDto);
        return schoolMapper.toDto(saved);
    }


    public List<SchoolDTO> findAll() {
        return schoolRepository.findAll()
                .stream()
                .map(schoolMapper::toDto)
                .collect(Collectors.toList());
    }

    public void deleteById(Integer schoolId) {
        schoolRepository.deleteById(schoolId);
    }
}
