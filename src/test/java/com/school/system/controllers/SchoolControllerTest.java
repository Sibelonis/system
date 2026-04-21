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
import com.school.system.repositories.TeacherRepository;
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


    @BeforeAll
    void setUpOnce() {
        MockitoAnnotations.openMocks(this);
        schoolRepository.deleteAll();
        studentRepository.deleteAll();
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
        schoolRepository.saveAll(List.of(school, school2, school3));
        studentRepository.saveAll(List.of(student, student2, student3));
        teacherRepository.saveAll(List.of(teacher, teacher2, teacher3));
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
        ResponseEntity<SchoolDTO> response = restTemplate.postForEntity("/school-save", school, SchoolDTO.class);
        //Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    }
    @Test
    void shouldNotCreateSchool_shouldReturnBadRequest_schoolIsNotInDatabase() {
        //Arrange
        SchoolDTO school = null;

        //Act
        ResponseEntity<Void> response = restTemplate.postForEntity("/school-save", school, Void.class);
        //Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    void shouldGetAllSchools() {
        ResponseEntity<Void> result = this.restTemplate.getForEntity("/schools", Void.class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldDeleteSchool_shouldReturnOk_schoolRemovedFromDatabase() {
        ResponseEntity<Void> result = this.restTemplate.exchange("/schools/delete-school-3", HttpMethod.DELETE,null, Void.class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @Test
    void tryDeleteSchoolWithNoId_shouldReturnBadRequest_schoolIsInDatabase() {
        ResponseEntity<Void> result = this.restTemplate.exchange("/schools/delete-school-", HttpMethod.DELETE, null, Void.class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    @Test
    void tryDeleteSchoolWithLettersInId_shouldReturnBadRequest_schoolIsInDatabase() {
        ResponseEntity<Void> result = this.restTemplate.exchange("/schools/delete-school-se", HttpMethod.DELETE, null, Void.class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void tryDeleteSchoolWithIncorrectId_shouldReturnNotFound_schoolIsInDatabase() {
        ResponseEntity<Void> result = this.restTemplate.exchange("/schools/delete-school-123", HttpMethod.DELETE, null, Void.class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldSetStudentToSchool_shouldReturnedCreated_andSchoolHasOneStudent() {
        String url = "/school-1/student-1";
        ResponseEntity<SchoolDTO> response = this.restTemplate.postForEntity(url, null, SchoolDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }
    @Test
    void trySetStudentWitWrongIdToRealSchool_shouldReturnBadRequest_schoolHasNoStudents() {
        String url = "/school-1/student-87";
        ResponseEntity<SchoolDTO> response = this.restTemplate.postForEntity(url, null, SchoolDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void trySetStudentWithRightIdToFakeSchool_shouldReturnBadRequest_schoolHasNoStudents() {
        String url = "/school-5678/student-1";
        ResponseEntity<SchoolDTO> response = this.restTemplate.postForEntity(url, null, SchoolDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void trySetStudentWithBlankIdBlankSchool_shouldReturnBadRequest_schoolHasNoStudents() {
        String url = "/school-/student-";
        ResponseEntity<SchoolDTO> response = this.restTemplate.postForEntity(url, null, SchoolDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    @Test
    void trySetStudentToSchoolButIdsAreLetters_shouldReturnBadRequest_schoolHasNoStudents() {
        String url = "/school-asd/student-asd";
        ResponseEntity<SchoolDTO> response = this.restTemplate.postForEntity(url, null, SchoolDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    @Test
    void shouldSetTeacherToSchool_shouldReturnedCreated_andSchoolHasOneTeacher() {
        String url = "/school-1/teacher-2";
        ResponseEntity<SchoolDTO> response = this.restTemplate.postForEntity(url, null, SchoolDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }
    @Test
    void trySetTeacherWithWrongIdToSchool_shouldReturnedBadRequest_andSchoolHasNoTeacher() {
        String url = "/school-1/teacher-2123";
        ResponseEntity<SchoolDTO> response = this.restTemplate.postForEntity(url, null, SchoolDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    @Test
    void trySetTeacherWithNoIdToSchool_shouldReturnedBadRequest_andSchoolHasNoTeacher() {
        String url = "/school-1/teacher-";
        ResponseEntity<SchoolDTO> response = this.restTemplate.postForEntity(url, null, SchoolDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    @Test
    void trySetTeacherToSchoolWithWrongId_shouldReturnedBadRequest_andSchoolHasNoTeacher() {
        String url = "/school-1231/teacher-1";
        ResponseEntity<SchoolDTO> response = this.restTemplate.postForEntity(url, null, SchoolDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    @Test
    void trySetTeacherToSchoolWithNoId_shouldReturnedBadRequest_andSchoolHasNoTeacher() {
        String url = "/school-/teacher-1";
        ResponseEntity<SchoolDTO> response = this.restTemplate.postForEntity(url, null, SchoolDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    @Test
    void trySetTeacherToSchoolButBothHaseWrongId_shouldReturnedBadRequest_andSchoolHasNoTeacher() {
        String url = "/school-1231/teacher-1456";
        ResponseEntity<SchoolDTO> response = this.restTemplate.postForEntity(url, null, SchoolDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    @Test
    void trySetTeacherToSchoolButIdIsLetters_shouldReturnedBadRequest_andSchoolHasNoTeacher() {
        String url = "/school-asd/teacher-asd";
        ResponseEntity<SchoolDTO> response = this.restTemplate.postForEntity(url, null, SchoolDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

}