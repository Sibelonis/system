package com.school.system.controllers;

import com.school.system.Difficulty;
import com.school.system.models.Student;
import com.school.system.models.Subject;
import com.school.system.models.Teacher;
import com.school.system.modelsDTO.TeacherDTO;
import com.school.system.repositories.StudentRepository;
import com.school.system.repositories.SubjectRepository;
import com.school.system.repositories.TeacherRepository;
import com.school.system.services.TeacherService;
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
import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TeacherControllerTest {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private SubjectRepository subjectRepository;

    @BeforeAll
    void setUp() {
        MockitoAnnotations.initMocks(this);
        teacherRepository.deleteAll();
        studentRepository.deleteAll();
        subjectRepository.deleteAll();
        Teacher teacher = new Teacher();
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacher.setTitle("John Doe");
        teacher.setAge(89);

        Teacher teacher2 = new Teacher();
        teacher2.setFirstName("John");
        teacher2.setLastName("Doe");
        teacher2.setTitle("John Doe");
        teacher2.setAge(89);

        Student student = new Student();
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setAge(12);

        Subject subject = new Subject();
        subject.setName("Test");
        subject.setDescription("Test Description");
        subject.setDifficulty(Difficulty.EASY);

        teacherRepository.save(teacher);
        teacherRepository.save(teacher2);
        studentRepository.save(student);
        subjectRepository.save(subject);

    }


    @Test
    void createTeacher() {
        TeacherDTO teacher = new TeacherDTO(
                "Pome",
                "Dalej",
                "Radsej",
                null);
        ResponseEntity<TeacherDTO> response = restTemplate.postForEntity("/teacher",teacher, TeacherDTO.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }
    @Test
    void createTeacher_whenNull_returnsException() {
        ResponseEntity<TeacherDTO> response = restTemplate.postForEntity("/teacher", null, null);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void findAllTeacher() {
        ResponseEntity<Void> response = restTemplate.getForEntity("/teachers", Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);    }

    @Test
    void deleteTeacher() {
        ResponseEntity<Void> response = restTemplate.exchange("/teachers/delete-2", HttpMethod.DELETE,null, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void addStudentToTeacher() {
        String url = "/teacher-1/student-1";
        ResponseEntity<TeacherDTO> response = this.restTemplate.postForEntity(url,null,TeacherDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void addSubjectToTeacher() {

        String url = "/teacher-1/subject-Test";
        ResponseEntity<TeacherDTO> response = this.restTemplate.postForEntity(url,null,TeacherDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }
}