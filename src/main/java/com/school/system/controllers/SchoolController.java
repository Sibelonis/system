package com.school.system.controllers;

import com.school.system.models.School;
import com.school.system.services.SchoolService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SchoolController {

    private final SchoolService schoolService;
    public SchoolController(SchoolService schoolService) {
        this.schoolService = schoolService;
    }

    @PostMapping("/schools")
    public School saveSchool(@RequestBody School school) {
        return schoolService.create(school);
    }

    @GetMapping("/schools")
    public List<School> getAllSchools() {
        return schoolService.findAll();
    }

    @DeleteMapping("/schools/{school-id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteSchool(@PathVariable("school-id") Integer schoolId) {
        schoolService.deleteById(schoolId);
    }

    @PostMapping("/school-{school-id}/student-{student-id}")
    public ResponseEntity<School> addStudentToSchool(@PathVariable("school-id") Integer schoolId, @PathVariable("student-id") Integer studentId) {
        School created = schoolService.addStudentToSchool(schoolId, studentId );
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/school-{school-id}/teacher-{teacher-id}")
    public ResponseEntity<School> addTeacherToSchool(@PathVariable("school-id") Integer schoolId, @PathVariable("teacher-id") Integer teacherId) {
        School created = schoolService.addTeacherToSchool(schoolId, teacherId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
