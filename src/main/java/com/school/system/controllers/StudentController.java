package com.school.system.controllers;

import com.school.system.models.Student;
import com.school.system.services.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StudentController {

    private final StudentService studentService;
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/student")
    public Student saveStudent(@RequestBody Student student) {
        return studentService.create(student);
    }

    @GetMapping("/students")
    public List<Student> findAllStudents() {
        return studentService.findAll();
    }
    @DeleteMapping("/students/{student-id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteStudent(@PathVariable("student-id") Integer studentId) {
        studentService.deleteById(studentId);

    }
}
