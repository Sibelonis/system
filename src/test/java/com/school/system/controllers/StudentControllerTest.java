package com.school.system.controllers;

import com.school.system.models.Student;
import com.school.system.models.Subject;
import com.school.system.modelsDTO.StudentDTO;
import com.school.system.repositories.SchoolRepository;
import com.school.system.repositories.StudentRepository;
import com.school.system.repositories.SubjectRepository;
import com.school.system.repositories.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
    private SchoolRepository schoolRepository;

    @Autowired
    private TeacherRepository teacherRepository;

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
        Subject subject = new Subject();
        subject.setName("TestSubject");

        subjectRepository.save(subject);
        studentRepository.save(student);
        studentRepository.save(student2);
    }

    @Test
    void saveStudent() {
        StudentDTO studentDTO = new StudentDTO(
                "Jako",
                "Pome",
                "Ne?",
                null
        );

        ResponseEntity<Void> responseEntity = this.restTemplate.postForEntity("/student/save", studentDTO, Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    void createWithNullStudent_shouldThrowException() {

        StudentDTO studentDTO = new StudentDTO(null,null,null,null);
        ResponseEntity<Void> responseEntity = this.restTemplate.postForEntity("/student/save", studentDTO, Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    void findAllStudents() {
        ResponseEntity<Void> responseEntity = restTemplate.getForEntity("/students", Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void deleteStudent() {
        Integer id = studentRepository.findAll().get(0).getId();
        ResponseEntity<Void> responseEntity = this.restTemplate
                .exchange("/students/delete/" + id, HttpMethod.DELETE, null, Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void addSubjectToStudent() {

        Integer studentId = studentRepository.findAll().get(0).getId();

        String url = "/student/" + studentId + "/subject/TestSubject";

        ResponseEntity<StudentDTO>  responseEntity = this.restTemplate.postForEntity(url, null, StudentDTO.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    }
}