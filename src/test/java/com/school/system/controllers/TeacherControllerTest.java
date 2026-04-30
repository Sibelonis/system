package com.school.system.controllers;

import com.school.system.Difficulty;
import com.school.system.models.Student;
import com.school.system.models.Subject;
import com.school.system.models.Teacher;
import com.school.system.modelsDTO.TeacherDTO;
import com.school.system.repositories.SchoolRepository;
import com.school.system.repositories.StudentRepository;
import com.school.system.repositories.SubjectRepository;
import com.school.system.repositories.TeacherRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Autowired
    private SchoolRepository schoolRepository;


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
        ResponseEntity<TeacherDTO> response = restTemplate.postForEntity("/teacher/save",teacher, TeacherDTO.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }
    @Test
    void createTeacher_whenNull_returnsException() {
        ResponseEntity<TeacherDTO> response = restTemplate.postForEntity("/teacher/save", null, null);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void findAllTeacher() {
        ResponseEntity<Void> response = restTemplate.getForEntity("/teachers", Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);    }

    @Test
    void deleteTeacher() {
        Integer id = teacherRepository.findAll().get(0).getId();
        ResponseEntity<Void> response = restTemplate
                .exchange("/teachers/delete/" + id, HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void addStudentToTeacher_success() {

        String url = "/teacher/{teacher-id}/student/{student-id}";
        String resolved = url.replace("{teacher-id}", String.valueOf(teacherRepository.findAll().get(0).getId()))
                .replace("{student-id}", String.valueOf(studentRepository.findAll().get(0).getId()));

        ResponseEntity<Void> response = this.restTemplate.postForEntity(resolved,null,Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @ParameterizedTest
    @CsvSource({
            "/teacher/1/student/, NOT_FOUND",
            "/teacher/234/student/Test, BAD_REQUEST",
            "/teacher/1/student/32, BAD_REQUEST",
            "/teacher//student/, NOT_FOUND",
    })
    void addStudentToTeacher_failure(String path, HttpStatus expected) {

        String resolved = path.replace("{id}", String.valueOf(teacherRepository.findAll().get(0).getId()));

        ResponseEntity<TeacherDTO> response = this.restTemplate.postForEntity(resolved,null,TeacherDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "/teacher/1/subject/, NOT_FOUND",
            "/teacher/234/subject/Test, BAD_REQUEST",
            "/teacher/1/subject/32, BAD_REQUEST",
            "/teacher//subject/, NOT_FOUND",
    })
    void addSubjectToTeacher_failure(String path, HttpStatus expected) {

        ResponseEntity<Void> response = this.restTemplate.postForEntity(path,null,Void.class);
        assertThat(response.getStatusCode()).isEqualTo(expected);
    }

    @Test
    void addSubjectToTeacher_success() {

        String url = "/teacher/{id}/subject/Test";
        String resolved = url.replace("{id}", String.valueOf(teacherRepository.findAll().get(0).getId()));

        ResponseEntity<Void> response = this.restTemplate.postForEntity(resolved,null,Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

}