package com.school.system.controllers;

import com.school.system.modelsDTO.StudentDTO;
import com.school.system.services.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StudentController {

    private final StudentService studentService;
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/student-save")
    public HttpStatus saveStudent(@RequestBody StudentDTO student) {
        studentService.create(student);
        return HttpStatus.OK;
    }

    @GetMapping("/students")
    public List<StudentDTO> findAllStudents() {
               return studentService.findAll();
    }

    @DeleteMapping("/students/delete-{student-id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteStudent(@PathVariable("student-id") Integer studentId) {
        studentService.deleteById(studentId);

    }
    @PostMapping("/student-{student-id}/subject-{subject-name}")
    public ResponseEntity<StudentDTO> addSubjectToStudent(@PathVariable("student-id") Integer studentId, @PathVariable("subject-name") String subjectName) {
        StudentDTO create = studentService.addSubjectToStudent(studentId,subjectName);
        return ResponseEntity.status(HttpStatus.CREATED).body(create);
    }
}
