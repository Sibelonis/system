package com.school.system.controllers;

import com.school.system.models.Address;
import com.school.system.models.School;
import com.school.system.models.Student;
import com.school.system.models.Teacher;
import com.school.system.modelsDTO.SchoolDTO;
import com.school.system.modelsDTO.StudentDTO;
import com.school.system.modelsDTO.TeacherDTO;
import com.school.system.repositories.SchoolRepository;
import com.school.system.repositories.StudentRepository;
import com.school.system.repositories.SubjectRepository;
import com.school.system.repositories.TeacherRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SchoolControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private SubjectRepository subjectRepository;


    @BeforeEach
    void setUpOnce() {
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

        Address address = new Address();
        School school = new School(
                "MIT",
                address,
                "High School"
        );
        School school2 = new School(
                "Gavel",
                address,
                "Middle School"
        );
        School school3 = new School(
                "JWT",
                address,
                "Kindergarten"
        );

        Student student = new Student(
                "Pavol",
                "Yamato",
                "Special class",
                15
        );
        Student student2 = new Student(
                "Peter",
                "Mrkva",
                "Upper class",
                15
        );
        Student student3 = new Student(
                "Rafael",
                "Turtle",
                "Ninja class",
                45
        );

        Teacher teacher = new Teacher(
                "Jan",
                "Prvy svojho rodu",
                "sir",
                "Special class",
                60
        );
        Teacher teacher2 = new Teacher(
                "Albert",
                "Einstein",
                "god of physics",
                "Upper class",
                45
        );
        Teacher teacher3 = new Teacher(
                "Master",
                "Splinter",
                "Sensei",
                "Ninja class",
                10
        );
        teacherRepository.saveAll(List.of(teacher, teacher2, teacher3));
        studentRepository.saveAll(List.of(student, student2, student3));
        schoolRepository.saveAll(List.of(school, school2, school3));
        studentRepository.save(student);
    }
    @Test
    void shouldCreateSchool_shouldReturnOk_schoolIsInDatabase() {
        //Arrange
        List<StudentDTO> students = new ArrayList<>();
        List<TeacherDTO> teachers = new ArrayList<>();
        SchoolDTO school = new SchoolDTO(
                "Test School",
                "Test degree",
                students,
                teachers
        );
        //Act
        ResponseEntity<SchoolDTO> response = restTemplate.postForEntity("/school/save", school, SchoolDTO.class);
        //Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    }
    @Test
    void shouldNotCreateSchool_shouldReturnBadRequest_schoolIsNotInDatabase() {
        //Arrange
        SchoolDTO school = null;

        //Act
        ResponseEntity<Void> response = restTemplate.postForEntity("/school/save", school, Void.class);
        //Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        schoolRepository.deleteAll();

    }

    @Test
    void shouldGetAllSchools() {
        ResponseEntity<Void> result = this.restTemplate.getForEntity("/schools", Void.class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void deleteSchool_success() {
        String url = "/schools/delete/school/{id}";

        String resolved = url.replace("{id}", String.valueOf(schoolRepository.findAll().getFirst().getSchoolId()));


        ResponseEntity<Void> result = this.restTemplate.exchange(resolved, HttpMethod.DELETE, null, Void.class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @ParameterizedTest
    @CsvSource({
            "/schools/delete/school/, NOT_FOUND",
            "/schools/delete/school/se, BAD_REQUEST",
            "/schools/delete/school/123, BAD_REQUEST"
    })
    void deleteSchool_failure(String url, HttpStatus expected) {
        ResponseEntity<Void> result = this.restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
        assertThat(result.getStatusCode()).isEqualTo(expected);
    }

    @Test
    void setStudentToSchool_success() {
        String url = "/school/{school-id}/student/{student-id}";

        String resolved = url.replace("{school-id}", String.valueOf(schoolRepository.findAll().getFirst().getSchoolId()))
                .replace("{student-id}", String.valueOf(studentRepository.findAll().getFirst().getId()));

        ResponseEntity<SchoolDTO> result = this.restTemplate.postForEntity(resolved,  null, SchoolDTO.class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }
    @ParameterizedTest
    @CsvSource({
            "/school//student/, NOT_FOUND",
            "/school/1/student/87, BAD_REQUEST",
            "/school/5678/student/1, BAD_REQUEST",
            "/school/asd/student/asd, BAD_REQUEST"
    })
    void setStudentToSchool_parameterized_failure(String path, HttpStatus expected) {
        ResponseEntity<SchoolDTO> response = this.restTemplate.postForEntity(path, null, SchoolDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(expected);
    }


    @Test
    void setTeacherToSchool_success() {
        String url = "/school/{school-id}/teacher/{teacher-id}";

        String resolved = url.replace("{school-id}", String.valueOf(schoolRepository.findAll().getFirst().getSchoolId()))
                .replace("{teacher-id}", String.valueOf(teacherRepository.findAll().getFirst().getId()));

        ResponseEntity<SchoolDTO> result = this.restTemplate.postForEntity(resolved, null, SchoolDTO.class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @ParameterizedTest
    @CsvSource({
            "/school/1/teacher/99, BAD_REQUEST",
            "/school/58/teacher/1, BAD_REQUEST",
            "/school//teacher/, NOT_FOUND",
            "/school/asd/teacher/ad, BAD_REQUEST"
    })
    void setTeacherToSchool_parameterized(String path, HttpStatus expected) {
        ResponseEntity<SchoolDTO> response = this.restTemplate.postForEntity(path, null, SchoolDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(expected);
    }

}