package com.school.system.controllers;

import com.school.system.models.Teacher;
import com.school.system.modelsDTO.TeacherDTO;
import com.school.system.services.TeacherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TeacherController {

    private final TeacherService teacherService;
    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @PostMapping("/teacher")
    public TeacherDTO saveTeacher(@RequestBody TeacherDTO teacher){
        return teacherService.create(teacher);
    }

    @GetMapping("/teachers")
    public List<TeacherDTO> findAllTeacher(){
        return teacherService.findAll();
    }

    @DeleteMapping("/teachers/delete-{teacher-id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTeacher(@PathVariable("teacher-id") Integer teacherId) {
            teacherService.deleteById(teacherId);

    }
    @PostMapping("/teacher-{teacher-id}/student-{student-id}")
    public ResponseEntity<TeacherDTO> addStudentToTeacher(@PathVariable("teacher-id") Integer teacherId, @PathVariable("student-id") Integer studentId) {
        TeacherDTO updatedTeacher = teacherService.addStudentToTeacher(teacherId, studentId);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedTeacher);
    }

    @PostMapping("/teacher-{teacher-id}/subject-{subject-name}")
    public ResponseEntity<TeacherDTO> addSubjectToTeacher(@PathVariable("teacher-id")  Integer teacherId, @PathVariable("subject-name") String subjectName) {
        TeacherDTO updatedTeacher = teacherService.addSubjectToTeacher(teacherId, subjectName);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedTeacher);
    }
}
