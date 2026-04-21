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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
        assertNotSame(s1, s2);
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

        assertThrows(ResponseStatusException.class, () -> schoolService.deleteById(id));

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
}