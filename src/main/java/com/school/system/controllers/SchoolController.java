package com.school.system.controllers;

import com.school.system.modelsDTO.SchoolDTO;
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

    @PostMapping("/school/save")
    @ResponseStatus(HttpStatus.CREATED)
    public SchoolDTO saveSchool(@RequestBody SchoolDTO schoolDTO) {
        if (schoolDTO != null) {

            return schoolService.create(schoolDTO);
        }
        return null;
    }

    @GetMapping("/schools")
    public List<SchoolDTO> getAllSchools() {
        return schoolService.findAll();
    }

    @DeleteMapping("/schools/delete/school/{school-id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteSchool(@PathVariable("school-id") Integer schoolId) {
        schoolService.deleteById(schoolId);

    }

    @PostMapping("/school/{school-id}/student/{student-id}")
    public ResponseEntity<SchoolDTO> addStudentToSchool(
            @PathVariable("school-id") Integer schoolId,
            @PathVariable("student-id") Integer studentId) {

        SchoolDTO updateSchool = schoolService.addStudentToSchool(schoolId, studentId );
        return ResponseEntity.status(HttpStatus.CREATED).body(updateSchool);

    }

    @PostMapping("/school/{school-id}/teacher/{teacher-id}")
    public ResponseEntity<SchoolDTO> addTeacherToSchool(
            @PathVariable("school-id") Integer schoolId,
            @PathVariable("teacher-id") Integer teacherId) {

        SchoolDTO updateSchool = schoolService.addTeacherToSchool(schoolId, teacherId);
        return ResponseEntity.status(HttpStatus.CREATED).body(updateSchool);
    }
}
