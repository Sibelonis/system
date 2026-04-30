package com.school.system.services;

import com.school.system.errorHandling.BadRequestException;
import com.school.system.maps.SchoolMapper;
import com.school.system.models.School;
import com.school.system.models.Student;
import com.school.system.models.Teacher;
import com.school.system.modelsDTO.SchoolDTO;
import com.school.system.repositories.SchoolRepository;
import com.school.system.repositories.StudentRepository;
import com.school.system.repositories.TeacherRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        School school = schoolRepository.findById(schoolId)
                .orElseThrow(()-> new BadRequestException("Invalid school: " + schoolId + "\n Please use real school ID."));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(()-> new BadRequestException("Invalid student: " + studentId + "\n Please use real student ID."));

        school.addStudent(student);
        School saved = schoolRepository.save(school);
        return schoolMapper.toDto(saved);
    }


    public SchoolDTO addTeacherToSchool(Integer schoolId, Integer teacherId) {
        School school = schoolRepository.findById(schoolId)
                .orElseThrow(()-> new BadRequestException("Invalid school: " + schoolId + "\n Please use real school ID."));

        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(()-> new BadRequestException("Invalid teacher: " + teacherId + "\n Please use real teacher ID."));

        school.addTeacher(teacher);
        School saved = schoolRepository.save(school);
        return schoolMapper.toDto(saved);
    }

    public SchoolDTO create(SchoolDTO schoolDTO) {
        if (schoolDTO == null) throw new BadRequestException("Request body is required");
        if (schoolDTO.schoolName() == null || schoolDTO.degree() == null) {
            throw new BadRequestException("schoolName and degree are required");
        }
        var school = schoolMapper.toEntity(schoolDTO);
        return schoolMapper.toDto(schoolRepository.save(school));
    }


    public List<SchoolDTO> findAll() {
        return schoolRepository.findAll()
                .stream()
                .map(schoolMapper::toDto)
                .collect(Collectors.toList());
    }

    public void deleteById(Integer schoolId) {
        var school = schoolRepository.findById(schoolId)
                .orElseThrow(() -> new BadRequestException( "School not found: " + schoolId));
        var students = school.getStudents();
        if (students != null && !students.isEmpty()) {
            students.forEach(s -> s.setSchool(null));
            studentRepository.saveAll(students);
        }
        var teachers = school.getTeachers();
        if (teachers != null && !teachers.isEmpty()) {
            teachers.forEach(t -> t.setSchool(null));
            teacherRepository.saveAll(teachers);
        }
        schoolRepository.deleteById(schoolId);
    }
}
