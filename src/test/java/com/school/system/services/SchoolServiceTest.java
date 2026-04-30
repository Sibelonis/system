package com.school.system.services;

import com.school.system.errorHandling.BadRequestException;
import com.school.system.maps.SchoolMapper;
import com.school.system.maps.TeacherMapper;
import com.school.system.models.School;
import com.school.system.models.Student;
import com.school.system.models.Teacher;
import com.school.system.modelsDTO.SchoolDTO;
import com.school.system.modelsDTO.TeacherDTO;
import com.school.system.repositories.SchoolRepository;
import com.school.system.repositories.StudentRepository;
import com.school.system.repositories.TeacherRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;

@AutoConfigureMockMvc
class SchoolServiceTest {

    @Mock
    private SchoolRepository schoolRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private TeacherMapper teacherMapper;

    @Mock
    private SchoolMapper schoolMapper;

    @InjectMocks
    private SchoolService schoolService;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_returnsDto_whenNameAndDegreePresent() {
        // given
        SchoolDTO inputDto = new SchoolDTO("My School", "High" ,null,null);
        School entity = new School();
        School savedEntity = new School();

        SchoolDTO expectedDto = new SchoolDTO("My School", "High",null,null);
        // when
        when(schoolMapper.toEntity(inputDto)).thenReturn(entity);
        when(schoolRepository.save(entity)).thenReturn(savedEntity);
        when(schoolMapper.toDto(savedEntity)).thenReturn(expectedDto);

        SchoolDTO result = schoolService.create(inputDto);

        // then
        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(schoolMapper).toEntity(inputDto);
        verify(schoolRepository).save(entity);
        verify(schoolMapper).toDto(savedEntity);
    }

    @Test
    void create_returnsNull_whenBothNull() {
        SchoolDTO inputDto = new SchoolDTO(null,null,null, null);

        assertThrows(BadRequestException.class, () -> schoolService.create(inputDto));
        verifyNoInteractions(schoolMapper, schoolRepository);

    }

    @Test
    void findAll_returnsDtoList() {
        School s1 = new School();
        s1.setSchoolName("My School");
        School s2 = new School();
        s2.setSchoolName("High");
        List<School> entities = List.of(s1, s2);
        SchoolDTO d1 = new SchoolDTO("A","deg",null,null);
        SchoolDTO d2 = new SchoolDTO("B","asdw",null,null);

        when(schoolRepository.findAll()).thenReturn(entities);
        when(schoolMapper.toDto(s1)).thenReturn(d1);
        when(schoolMapper.toDto(s2)).thenReturn(d2);

        List<SchoolDTO> result = schoolService.findAll();

        assertNotEquals(s1, s2);
        assertEquals(List.of(d1, d2), result);
        verify(schoolRepository).findAll();
        verify(schoolMapper).toDto(s1);
        verify(schoolMapper).toDto(s2);
    }
    @Test
    void deleteById_deletesWhenPresent() {
        int id = 1;
        School school = new School();
        when(schoolRepository.findById(id)).thenReturn(Optional.of(school));

        schoolService.deleteById(id);

        verify(schoolRepository).findById(id);
        verify(schoolRepository).deleteById(id);
    }

    @Test
    void deleteById_throwsWhenNotFound() {
        int id = 2;
        when(schoolRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> schoolService.deleteById(id));

        verify(schoolRepository).findById(id);
        verify(schoolRepository, never()).deleteById(anyInt());
    }

    @Test
    void addStudentToSchool_success() {
        Integer schoolId = 1;
        Integer studentId = 5;

        School school = new School();

        Student student = new Student();
        student.setId(studentId);

        School savedSchool = new School();



        SchoolDTO dto = new SchoolDTO("School","degree",null,null);


        when(schoolRepository.findById(schoolId)).thenReturn(Optional.of(school));
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(schoolRepository.save(any(School.class))).thenReturn(savedSchool);
        when(schoolMapper.toDto(savedSchool)).thenReturn(dto);

        SchoolDTO result = schoolService.addStudentToSchool(schoolId, studentId);

        assertNotNull(result);


        ArgumentCaptor<School> captor = ArgumentCaptor.forClass(School.class);
        verify(schoolRepository).save(captor.capture());
        School savedArg = captor.getValue();
        assertTrue(savedArg.getStudents().contains(student));

        verify(schoolRepository).findById(schoolId);
        verify(studentRepository).findById(studentId);
        verify(schoolMapper).toDto(savedSchool);
    }

    @Test
    void addStudentToSchool_throwsWhenSchoolNotFound() {
        Integer schoolId = 1;
        Integer studentId = 10;

        when(schoolRepository.findById(schoolId)).thenReturn(Optional.empty());

        BadRequestException ex = assertThrows(BadRequestException.class,
                () -> schoolService.addStudentToSchool(schoolId, studentId));

        assertEquals("Invalid school: " + schoolId + "\n Please use real school ID.", ex.getMessage());
        verify(schoolRepository).findById(schoolId);
        verifyNoInteractions(studentRepository);
        verifyNoMoreInteractions(schoolRepository);
    }

    @Test
    void addStudentToSchool_throwsWhenStudentNotFound() {
        Integer schoolId = 1;
        Integer studentId = 10;

        School school = new School();

        when(schoolRepository.findById(schoolId)).thenReturn(Optional.of(school));
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        BadRequestException ex = assertThrows(BadRequestException.class,
                () -> schoolService.addStudentToSchool(schoolId, studentId));

        assertEquals("Invalid student: " + studentId + "\n Please use real student ID.", ex.getMessage());
        verify(schoolRepository).findById(schoolId);
        verify(studentRepository).findById(studentId);
        verifyNoMoreInteractions(schoolRepository);
    }

    @Test
    void addTeacherToSchool_successful() {

        Teacher teacher = new Teacher();
        teacher.setId(1);
        teacher.setFirstName("Janko");
        teacher.setLastName("Mrkvicka");
        teacher.setAge(21);
        List<TeacherDTO> teachers = new ArrayList<>();
        teachers.add(teacherMapper.toDto(teacher));

        School school = new School();
        school.setSchoolId(1);

        school.addTeacher(teacher);

        SchoolDTO expectedDto = new SchoolDTO("School","degree", null, teachers);

        when(schoolRepository.findById(school.getSchoolId())).thenReturn(Optional.of(school));
        when(teacherRepository.findById(teacher.getId())).thenReturn(Optional.of(teacher));
        when(schoolService.addTeacherToSchool(school.getSchoolId(), teacher.getId())).thenReturn(expectedDto);

        assertEquals(1, teacher.getId());
        assertEquals(1,school.getTeachers().getFirst().getId());
        assertEquals(teacher,school.getTeachers().getFirst());


        verify(schoolRepository).save(any(School.class));
        verify(schoolRepository).findById(school.getTeachers().getFirst().getId());
    }

    @Test
    void addTeacherToSchool_throwsWhenSchoolNotFound() {
        Integer schoolId = 1;
        Integer teacherId = 2;

        when(schoolRepository.findById(schoolId)).thenReturn(Optional.empty());

        BadRequestException ex = assertThrows(BadRequestException.class,
                () -> schoolService.addTeacherToSchool(schoolId, teacherId));

        assertEquals("Invalid school: " + schoolId + "\n Please use real school ID.", ex.getMessage());
        verify(schoolRepository).findById(schoolId);
        verifyNoMoreInteractions(teacherRepository, schoolRepository, schoolMapper);
    }

    @Test
    void addTeacherToSchool_throwsWhenTeacherNotFound() {
        Integer schoolId = 1;
        Integer teacherId = 2;

        School school = new School();


        when(schoolRepository.findById(schoolId)).thenReturn(Optional.of(school));
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());

        BadRequestException ex = assertThrows(BadRequestException.class,
                () -> schoolService.addTeacherToSchool(schoolId, teacherId));

        assertEquals("Invalid teacher: " + teacherId + "\n Please use real teacher ID.", ex.getMessage());
        verify(schoolRepository).findById(schoolId);
        verify(teacherRepository).findById(teacherId);
        verifyNoMoreInteractions(schoolRepository, teacherRepository, schoolMapper);
    }



    @ParameterizedTest
    @CsvSource({
            "My School, High",
            "Other School, Primary",
            "Third School, Secondary"
    })
    void create_returnsDto_whenNameAndDegreePresent(String name, String degree) {
        // given
        SchoolDTO inputDto = new SchoolDTO(name, degree ,null,null);
        School entity = new School();
        School savedEntity = new School();

        SchoolDTO expectedDto = new SchoolDTO(name, degree,null,null);
        // when
        when(schoolMapper.toEntity(inputDto)).thenReturn(entity);
        when(schoolRepository.save(entity)).thenReturn(savedEntity);
        when(schoolMapper.toDto(savedEntity)).thenReturn(expectedDto);

        SchoolDTO result = schoolService.create(inputDto);

        // then
        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(schoolMapper).toEntity(inputDto);
        verify(schoolRepository).save(entity);
        verify(schoolMapper).toDto(savedEntity);
    }
    @ParameterizedTest
    @NullSource
    void create_returnsNull_whenBothNull(SchoolDTO inputDto) {

        assertThrows(BadRequestException.class, () -> schoolService.create(inputDto));
        verifyNoInteractions(schoolMapper, schoolRepository);

    }

    private static Stream<Arguments> findAllProvider() {
        School s1 = new School(); s1.setSchoolName("My School");
        School s2 = new School(); s2.setSchoolName("High");
        School s3 = new School(); s3.setSchoolName("Other");

        SchoolDTO d1 = new SchoolDTO("A","deg",null,null);
        SchoolDTO d2 = new SchoolDTO("B","degree",null,null);
        SchoolDTO d3 = new SchoolDTO("C","other",null,null);

        return Stream.of(
                Arguments.of(List.of(s1, s2), List.of(d1, d2)),
                Arguments.of(List.of(s1, s3), List.of(d1, d3)),
                Arguments.of(List.of(), List.of())
        );
    }


    @ParameterizedTest
    @MethodSource("findAllProvider")
    void findAll_returnsDtoList(List<School> entities, List<SchoolDTO> expectedDtos) {
        when(schoolRepository.findAll()).thenReturn(entities);
        for (int i = 0; i < entities.size(); i++) {
            when(schoolMapper.toDto(entities.get(i))).thenReturn(expectedDtos.get(i));
        }

        List<SchoolDTO> result = schoolService.findAll();

        assertEquals(expectedDtos, result);
        verify(schoolRepository).findAll();
        for (School e : entities) verify(schoolMapper).toDto(e);
        reset(schoolRepository, schoolMapper);
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2,12,45})
    void deleteById_deletesWhenPresent(Integer id) {
        School school = new School();
        when(schoolRepository.findById(id)).thenReturn(Optional.of(school));

        schoolService.deleteById(id);

        verify(schoolRepository).findById(id);
        verify(schoolRepository).deleteById(id);
    }


    static Stream<Arguments> deleteByIdProvider() {
        return Stream.of(
                arguments(1, true),
                arguments(2, true),
                arguments(3, false)
        );
    }


    @ParameterizedTest
    @MethodSource("deleteByIdProvider")
    void deleteById_behaviourByPresence(Integer id, boolean present) {
        if (present) {
            when(schoolRepository.findById(id)).thenReturn(Optional.of(new School()));
        } else {
            when(schoolRepository.findById(id)).thenReturn(Optional.empty());
        }

        if (present) {
            schoolService.deleteById(id);
            verify(schoolRepository).findById(id);
            verify(schoolRepository).deleteById(id);
        } else {
            BadRequestException ex = assertThrows(BadRequestException.class, () -> schoolService.deleteById(id));
            assertEquals("School not found: " + id, ex.getMessage());
            verify(schoolRepository).findById(id);
            verify(schoolRepository, never()).deleteById(id);
        }

        reset(schoolRepository);
    }

    private static Stream<Arguments> addStudentToSchoolProvider() {
        School school1 = new School(); school1.setSchoolName("My School"); school1.setSchoolId(1);
        School school2 = new School(); school2.setSchoolName("High");  school2.setSchoolId(2);
        School school3 = new School(); school3.setSchoolName("Other");  school3.setSchoolId(3);


       Student student1 = new Student(); student1.setFirstName("Jakub"); student1.setId(10);
       Student student2 = new Student(); student2.setFirstName("Bulo");  student2.setId(20);
       Student student3 = new Student(); student3.setFirstName("Ignac");  student3.setId(30);

        return Stream.of(
                Arguments.of(school1, student1, true, true),
                Arguments.of(school2, student2, true, true),
                Arguments.of(school3, student3, true, false),
                Arguments.of(null, student1, false, true)
        );
    }

    @ParameterizedTest
    @MethodSource("addStudentToSchoolProvider")
    void addStudentToSchool(School school, Student student,boolean schoolPresent, boolean studentPresent) {
        Integer schoolId = (school != null) ? school.getSchoolId() : 99;
        Integer studentId = student.getId();

        if (schoolPresent) {
            when(schoolRepository.findById(schoolId)).thenReturn(Optional.of(school));
        } else when(schoolRepository.findById(studentId)).thenReturn(Optional.empty());

        if (studentPresent) {
            when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        } else when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        if (schoolPresent && studentPresent) {
            School saved = new School();
            saved.setSchoolId(schoolId);
            when(schoolRepository.save(any(School.class))).thenReturn(saved);
            when(schoolMapper.toDto(saved)).thenReturn(new SchoolDTO("S", "deg", null, null));

            SchoolDTO result = schoolService.addStudentToSchool(schoolId, studentId);
            assertNotNull(result);

            ArgumentCaptor<School> captor = ArgumentCaptor.forClass(School.class);
            verify(schoolRepository).save(captor.capture());
            assertTrue(captor.getValue().getStudents().contains(student));
            verify(schoolMapper).toDto(saved);
        } else {
            assertThrows(BadRequestException.class, () -> schoolService.addStudentToSchool(schoolId, studentId));
            verify(schoolRepository).findById(schoolId);
            if (schoolPresent){
                verify(studentRepository).findById(studentId);
            }
            else {
                verify(studentRepository, never()).findById(any());
            }
            verify(schoolRepository, never()).save(any());
            verify(schoolMapper, never()).toDto(any());

        }
        reset(schoolRepository, studentRepository, schoolMapper);
    }
}