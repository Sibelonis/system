package com.school.system.controllers;

import com.school.system.maps.SubjectMapper;
import com.school.system.models.Student;
import com.school.system.models.Subject;
import com.school.system.modelsDTO.StudentDTO;
import com.school.system.modelsDTO.SubjectDTO;
import com.school.system.repositories.StudentRepository;
import com.school.system.repositories.SubjectRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTest {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private SubjectMapper subjectMapper;
    @Autowired
    private TestRestTemplate restTemplate;


    @BeforeAll
    void setUp() {
        MockitoAnnotations.initMocks(this);
        studentRepository.deleteAll();
        Student student = new Student(
                "First",
                "Second",
                "Ninja",
                15
        );
        Student student2 = new Student(
                "First",
                "Second",
                "Ninja",
                15
        );
        studentRepository.save(student);
        studentRepository.save(student2);
    }

    @Test
    void saveStudent() {
        StudentDTO studentDTO = new StudentDTO(
                "Jako",
                "Pome",
                "Ne?",
                null,
                null
        );

        ResponseEntity<Void> responseEntity = this.restTemplate.postForEntity("/student-save", studentDTO, Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    void createWithNullStudent_shouldThrowException() {

        StudentDTO studentDTO = new StudentDTO(null,null,null,null,null);
        ResponseEntity<Void> responseEntity = this.restTemplate.postForEntity("/student-save", studentDTO, Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    void findAllStudents() {
        ResponseEntity<Void> responseEntity = restTemplate.getForEntity("/students", Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void deleteStudent() {

        ResponseEntity<Void> responseEntity = this.restTemplate.exchange("/students/delete-2", HttpMethod.DELETE,null, Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void addSubjectToStudent() {
        subjectRepository.deleteAll();
        SubjectDTO subjectDTO = new SubjectDTO("Hodina",null);
        Subject subject = subjectMapper.toEntity(subjectDTO);
        subjectRepository.save(subject);

        ResponseEntity<StudentDTO>  responseEntity = this.restTemplate.postForEntity("/student-1/subject-Hodina", subjectDTO, StudentDTO.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);


    }
}