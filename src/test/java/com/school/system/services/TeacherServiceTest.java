package com.school.system.services;

import com.school.system.Difficulty;
import com.school.system.maps.StudentMapper;
import com.school.system.maps.SubjectMapper;
import com.school.system.maps.TeacherMapper;
import com.school.system.models.Student;
import com.school.system.models.Subject;
import com.school.system.models.Teacher;
import com.school.system.modelsDTO.StudentDTO;
import com.school.system.modelsDTO.SubjectDTO;
import com.school.system.modelsDTO.TeacherDTO;
import com.school.system.repositories.StudentRepository;
import com.school.system.repositories.SubjectRepository;
import com.school.system.repositories.TeacherRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotEmpty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoInteractions;

@AutoConfigureMockMvc
class TeacherServiceTest {
    @Mock
    private TeacherRepository teacherRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private SubjectRepository subjectRepository;
    @Mock
    private TeacherMapper teacherMapper;
    @Mock
    private StudentMapper studentMapper;
    @Mock
    private SubjectMapper subjectMapper;

    @InjectMocks
    private TeacherService teacherService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTeacher_returnsDto_WhenProvidedDtoProperties() {

        TeacherDTO teacherDTO = new TeacherDTO("Majster", "Trieska", "ninja", null);
        Teacher teacher = new Teacher();
        Teacher savedTeacher = new Teacher();
        TeacherDTO expectedTeacherDTO = new TeacherDTO("Majster", "Trieska", "ninja", null);

        when(teacherMapper.toEntity(teacherDTO)).thenReturn(teacher);
        when(teacherRepository.save(teacher)).thenReturn(savedTeacher);
        when(teacherMapper.toDto(savedTeacher)).thenReturn(teacherDTO);

        TeacherDTO actualTeacherDTO = teacherService.create(teacherDTO);

        assertNotNull(actualTeacherDTO);
        assertEquals(expectedTeacherDTO, actualTeacherDTO);

        verify(teacherMapper).toEntity(teacherDTO);
        verify(teacherMapper).toDto(savedTeacher);
        verify(teacherRepository).save(teacher);
        verify(teacherRepository).save(savedTeacher);

    }

    @Test
    void createTeacher_returnsNull_whenNullTeacherDTO() {

        TeacherDTO teacherDTO = new TeacherDTO(null, null, null, null);

        TeacherDTO result = teacherService.create(teacherDTO);

        assertNull(result);
        verifyNoInteractions(studentMapper);
        verifyNoInteractions(studentRepository);
    }

    @Test
    void findAll_returnsDtoList() {
        Teacher teacher1 = new Teacher();
        Teacher teacher2 = new Teacher();
        teacher1.setFirstName("Majster");
        teacher2.setFirstName("Sugon");
        List<Teacher> entities = List.of(teacher1, teacher2);
        TeacherDTO teacherDTO1 = new TeacherDTO("Majster", "Trieska", "Ninja", null);
        TeacherDTO teacherDTO2 = new TeacherDTO("Sugon", "Deez", "4", null);

        when(teacherRepository.findAll()).thenReturn(entities);
        when(teacherMapper.toDto(teacher1)).thenReturn(teacherDTO1);
        when(teacherMapper.toDto(teacher2)).thenReturn(teacherDTO2);

        List<TeacherDTO> actualTeacherDto = teacherService.findAll();

        assertNotNull(actualTeacherDto);
        assertNotSame(teacherDTO1, teacherDTO2);
        assertNotSame(teacher1, teacher2);
        assertEquals(List.of(teacherDTO1, teacherDTO2), actualTeacherDto);

        verify(teacherRepository).findAll();
        verify(teacherMapper).toDto(teacher1);
        verify(teacherMapper).toDto(teacher2);

    }

    @Test
    void deleteById_whenPresent() {
        int id = 1;
        Teacher teacher = new Teacher();
        teacher.setId(id);
        when(teacherRepository.findById(id)).thenReturn(Optional.of(teacher));

        teacherService.deleteById(id);

        verify(teacherRepository).deleteById(id);
        verify(teacherRepository).findById(id);
    }

    @Test
    void deleteById_whenNotPresent() {
        int id = 1;
        Teacher teacher = new Teacher();
        teacher.setId(id);

        when(teacherRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> teacherService.deleteById(id));

        verify(teacherRepository).findById(id);
        verify(teacherRepository, never()).deleteById(id);
    }

    @Test
    void addStudentToTeacher_success() {
        Student student = new Student();
        student.setId(1);
        student.setFirstName("Michale");

        List<StudentDTO> studentsDtoList = new ArrayList<>();
        studentsDtoList.add(studentMapper.toDTO(student));

        Teacher teacher = new Teacher();
        teacher.setId(1);
        teacher.setFirstName("Professor Elm");

        teacher.addStudent(student);

        TeacherDTO teacherDTO = new TeacherDTO("Professor Elm", "Lock hearth", "Black Magic", studentsDtoList);


        when(teacherRepository.findById(teacher.getId())).thenReturn(Optional.of(teacher));
        when(studentRepository.findById(student.getId())).thenReturn(Optional.of(student));
        when(teacherService.addStudentToTeacher(teacher.getId(), student.getId())).thenReturn(teacherDTO);

        assertEquals("Professor Elm", teacher.getFirstName());
        assertEquals("Michale", teacher.getStudents().getFirst().getFirstName());
        assertEquals(student, teacher.getStudents().getFirst());

        verify(teacherRepository).save(any(Teacher.class));
        verify(studentRepository).findById(student.getId());
    }

    @Test
    void addStudentToTeacher_studentNotFound_throws() {
        int teacherId = 1;
        int missingStudentId = 999;



        Teacher teacher = new Teacher();
        teacher.setId(teacherId);
        teacher.setFirstName("Professor Elm");
        teacher.setStudents(new LinkedList<>());

        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));
        when(studentRepository.findById(missingStudentId)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> teacherService.addStudentToTeacher(teacherId, missingStudentId));

        assertEquals("Student not found", ex.getMessage());

        verify(teacherRepository, never()).save(any());
        verify(studentRepository).findById(missingStudentId);
    }

    @Test
    void addSubjectToTeacher_success() {

        Subject subject = new Subject();
        subject.setId(1);
        subject.setName("Alchemy");
        subject.setDescription("Transmute things");
        subject.setDifficulty(Difficulty.HARD);


        Teacher teacher = new Teacher();
        teacher.setId(1);
        teacher.setFirstName("Professor Elm");


        teacher.addSubject(new Subject());
        teacher.addSubject(subject);

        TeacherDTO teacherDTO = new TeacherDTO("Professor Elm", "Lock hearth", "Black Magic", null);

        when(teacherRepository.findById(teacher.getId())).thenReturn(Optional.of(teacher));
        when(subjectRepository.findByName(subject.getName())).thenReturn(Optional.of(subject));
        when(teacherService.addSubjectToTeacher(teacher.getId(), subject.getName())).thenReturn(teacherDTO);

        assertEquals("Alchemy", subject.getName());
        assertEquals("Alchemy", teacher.getSubject().getName());
        assertEquals(subject, teacher.getSubject());

        verify(teacherRepository).save(any(Teacher.class));
        verify(subjectRepository).findByName(subject.getName());
        verify(teacherRepository).findById(teacher.getSubject().getId());
    }

    @Test
    void addSubjectToTeacher_subjectNotFound_throws() {
        int teacherId = 1;
        String missingSubjectName = "Nonexistent";

        Teacher teacher = new Teacher();
        teacher.setId(teacherId);
        teacher.setFirstName("Professor Elm");
        teacher.addSubject(new Subject());

        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));
        when(subjectRepository.findByName(missingSubjectName)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> teacherService.addSubjectToTeacher(teacherId, missingSubjectName));

        assertEquals("Subject not found", ex.getMessage());

        verify(teacherRepository, never()).save(any());
        verify(subjectRepository).findByName(missingSubjectName);
    }
}