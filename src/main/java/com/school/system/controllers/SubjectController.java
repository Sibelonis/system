package com.school.system.controllers;

import com.school.system.models.Subject;
import com.school.system.services.SubjectService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SubjectController {

    private final SubjectService subjectService;
    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @PostMapping("/subject")
    public Subject saveSubject(@RequestBody Subject subject) {
        return subjectService.create(subject);
    }

    @GetMapping("/subjects")
    public List<Subject> findAllSubjects() {
        return subjectService.findAll();
    }

    @DeleteMapping("/subjects/{subject-id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteSubject(@PathVariable("subject-id") Integer subjectID) {
        subjectService.deleteById(subjectID);
    }
}
