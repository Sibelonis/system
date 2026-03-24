package com.school.system.services;

import com.school.system.models.Student;
import com.school.system.models.Subject;
import com.school.system.models.Teacher;
import com.school.system.repositories.SchoolRepository;
import com.school.system.repositories.StudentRepository;
import com.school.system.repositories.SubjectRepository;
import com.school.system.repositories.TeacherRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {


    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;

    public TeacherService(TeacherRepository teacherRepository, StudentRepository studentRepository, SubjectRepository subjectRepository) {
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
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

    public Teacher addStudentToTeacher(Integer teacherId, Integer studentId) {
        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(()-> new EntityNotFoundException("Teacher not found"));

        Student student = studentRepository.findById(studentId).orElseThrow(()-> new EntityNotFoundException("Student not found"));
        teacher.addStudent(student);
        return teacherRepository.save(teacher);
    }

    public Teacher addSubjectToTeacher(Integer teacherId, String subjectName) {
        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(()-> new EntityNotFoundException("Teacher not found"));
        Subject subject = subjectRepository.findByName(subjectName).orElseThrow(()-> new EntityNotFoundException("Subject not found"));
        teacher.addSubject(subject);
        return teacherRepository.save(teacher);
    }
}
