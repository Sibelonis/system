package com.school.system.services;

import com.school.system.models.School;
import com.school.system.models.Student;
import com.school.system.models.Teacher;
import com.school.system.repositories.SchoolRepository;
import com.school.system.repositories.StudentRepository;
import com.school.system.repositories.TeacherRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchoolService {


    private final SchoolRepository schoolRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    public SchoolService(SchoolRepository schoolRepository, StudentRepository studentRepository, TeacherRepository teacherRepository) {
        this.schoolRepository = schoolRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }

    public School addStudentToSchool( Integer schoolId, Integer studentId) {
        School school = schoolRepository.findById(schoolId).orElseThrow(()-> new EntityNotFoundException("School not found"));

        Student student = studentRepository.findById(studentId).orElseThrow(()-> new EntityNotFoundException("Student not found"));
        school.addStudent(student);
        return schoolRepository.save(school);
    }
    public School addTeacherToSchool(Integer schoolId, Integer teacherId) {
        School school = schoolRepository.findById(schoolId).orElseThrow(()-> new EntityNotFoundException("School not found"));

        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(()-> new EntityNotFoundException("Teacher not found"));
        school.addTeacher(teacher);
        return schoolRepository.save(school);
    }

    public School create(School school) {
        schoolRepository.save(school);
        return school;
    }


    public List<School> findAll() {
        return schoolRepository.findAll();
    }

    public void deleteById(Integer schoolId) {
        schoolRepository.deleteById(schoolId);
    }
}
