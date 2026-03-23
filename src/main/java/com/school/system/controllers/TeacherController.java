package com.school.system.controllers;

import com.school.system.models.Teacher;
import com.school.system.services.TeacherService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TeacherController {

    private final TeacherService teacherService;
    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @PostMapping("/teacher")
    public Teacher saveTeacher(@RequestBody Teacher teacher){
        return teacherService.create(teacher);
    }

    @GetMapping("/teachers")
    public List<Teacher> findAllTeacher(){
        return teacherService.findAll();
    }
    @DeleteMapping("/teachers/{teacher-id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTeacher(@PathVariable("teacher-id") Integer teacherId) {
            teacherService.deleteById(teacherId);

    }
}
