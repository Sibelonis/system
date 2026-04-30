package com.school.system.controllers;

import com.school.system.Difficulty;
import com.school.system.models.Subject;
import com.school.system.modelsDTO.SubjectDTO;
import com.school.system.repositories.SchoolRepository;
import com.school.system.repositories.StudentRepository;
import com.school.system.repositories.SubjectRepository;
import com.school.system.repositories.TeacherRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SubjectControllerTest {
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private SchoolRepository schoolRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        studentRepository.findAll().forEach(s -> {
            s.setSubjects(null);
            studentRepository.save(s);
        });
        teacherRepository.findAll().forEach(t -> {
            if (t.getSubject() != null) {
                t.setSubject(null);
                teacherRepository.save(t);
            }
        });

        subjectRepository.deleteAll();
        studentRepository.deleteAll();
        schoolRepository.deleteAll();
        teacherRepository.deleteAll();

        Subject subject = new Subject();
        subject.setName("Test Subject");
        subject.setDescription("Test Subject description");
        subject.setDifficulty(Difficulty.MEDIUM);

        Subject subject2 = new Subject();
        subject2.setName("Test Subject");
        subject2.setDescription("Test Subject description");
        subject2.setDifficulty(Difficulty.MEDIUM);

        subjectRepository.save(subject);
        subjectRepository.save(subject2);
    }

    @Test
    void saveSubject() {
        SubjectDTO subjectDTO = new SubjectDTO(
                "Pekne",
                null
        );
        ResponseEntity<SubjectDTO> response = restTemplate.postForEntity("/subject/save",subjectDTO, SubjectDTO.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void findAllSubjects() {

        ResponseEntity<Void> response = restTemplate.getForEntity("/subjects", Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteSubject() {
        Integer id = subjectRepository.findAll().get(0).getId();
        ResponseEntity<Void> responseEntity = this.restTemplate
                .exchange("/subjects/delete/" + id, HttpMethod.DELETE, null, Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}