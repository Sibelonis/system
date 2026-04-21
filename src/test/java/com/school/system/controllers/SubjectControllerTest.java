package com.school.system.controllers;

import com.school.system.Difficulty;
import com.school.system.models.Subject;
import com.school.system.modelsDTO.SubjectDTO;
import com.school.system.repositories.SubjectRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SubjectControllerTest {
    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeAll
    void setUp() {
        subjectRepository.deleteAll();

        Subject subject = new Subject();
        subject.setName("Test Subject");
        subject.setDescription("Test Subject description");
        subject.setDifficulty(Difficulty.MEDIUM);

        subjectRepository.save(subject);
    }

    @Test
    void saveSubject() {
        SubjectDTO subjectDTO = new SubjectDTO(
                "Pekne",
                null
        );
        ResponseEntity<SubjectDTO> response = restTemplate.postForEntity("/subject",subjectDTO, SubjectDTO.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void findAllSubjects() {

        ResponseEntity<HttpStatus> response = restTemplate.getForEntity("/subjects", HttpStatus.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteSubject() {

        ResponseEntity<Void> responseEntity = this.restTemplate.exchange("/subjects/delete-1", HttpMethod.DELETE,null, Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}