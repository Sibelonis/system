package com.school.system.services;

import com.school.system.Difficulty;
import com.school.system.errorHandling.BadRequestException;
import com.school.system.maps.StudentMapper;
import com.school.system.maps.SubjectMapper;
import com.school.system.models.Student;
import com.school.system.models.Subject;
import com.school.system.modelsDTO.StudentDTO;
import com.school.system.modelsDTO.SubjectDTO;
import com.school.system.repositories.StudentRepository;
import com.school.system.repositories.SubjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private StudentMapper studentMapper;

    @Mock
    private SubjectMapper subjectMapper;

    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void createStudent_returnsDto_whenProvidedDtoProperties() {

        StudentDTO studentDTO = new StudentDTO("Michale","Human","",null);
        Student student = new Student();
        Student savedStudent = new Student();
        StudentDTO expectedStudentDTO = new StudentDTO("Michale","Human","",null);

        when(studentMapper.toEntity(studentDTO)).thenReturn(student);
        when(studentRepository.save(student)).thenReturn(savedStudent);
        when(studentMapper.toDTO(savedStudent)).thenReturn(expectedStudentDTO);

        StudentDTO actualStudentDTO = studentService.create(studentDTO);

        assertNotNull(actualStudentDTO);
        assertEquals(expectedStudentDTO,actualStudentDTO);

        verify(studentMapper).toEntity(studentDTO);
        verify(studentMapper).toDTO(savedStudent);
        verify(studentRepository).save(student);

    }
    @Test
    void createStudent_returnsNull_whenNullStudentDTO() {

        StudentDTO studentDTO = new StudentDTO(null,null,null,null);

        assertThrows(BadRequestException.class, () -> studentService.create(studentDTO));
        verifyNoInteractions(studentMapper);
        verifyNoInteractions(studentRepository);
    }

    @Test
    void findAll_returnsDtoList() {
        Student  student1 = new Student();
        Student  student2 = new Student();
        student1.setFirstName("Michale");
        student2.setFirstName("Human");
        List<Student> entities = List.of(student1,student2);
        StudentDTO studentDTO1 = new StudentDTO("Michaleeee","NotHuman","2",null);
        StudentDTO studentDTO2 = new StudentDTO("Michale","Human","4",null);

        when(studentRepository.findAll()).thenReturn(entities);
        when(studentMapper.toDTO(student1)).thenReturn(studentDTO1);
        when(studentMapper.toDTO(student2)).thenReturn(studentDTO2);

        List<StudentDTO> actualStudentDTO = studentService.findAll();

        assertNotNull(actualStudentDTO);
        assertNotSame(studentDTO1,studentDTO2);
        assertNotSame(studentDTO1,studentDTO2);
        assertEquals(List.of(studentDTO1,studentDTO2),actualStudentDTO);

        verify(studentRepository).findAll();
        verify(studentMapper).toDTO(student1);
        verify(studentMapper).toDTO(student2);
    }

    @Test
    void deleteById_whenPresent() {
        int id = 1;
        Student  student = new Student();
        student.setId(id);
        when(studentRepository.findById(id)).thenReturn(Optional.of(student));

        studentService.deleteById(id);

        verify(studentRepository).deleteById(id);
        verify(studentRepository).findById(id);
    }
    @Test
    void deleteById_whenNotPresent() {
        int id = 1;

        when(studentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> studentService.deleteById(id));

        verify(studentRepository).findById(id);
        verify(studentRepository, never()).deleteById(id);
    }

    @Test
    void addSubjectToStudent_success() {
        Subject subject = new Subject();
        subject.setId(1);
        subject.setName("League");
        subject.setDescription("We will teach you why YOU should help your JUNGLER!!!!!");
        subject.setDifficulty(Difficulty.HARD);

        List<SubjectDTO> subjects = new ArrayList<>();
        subjects.add(subjectMapper.toDto(subject));

        Student student = new Student();
        student.setId(1);

        student.addSubject(subject);

        StudentDTO studentDTO1 = new StudentDTO("Michale","Human","2",subjects);

        when(studentRepository.findById(student.getId())).thenReturn(Optional.of(student));
        when(subjectRepository.findByName(subject.getName())).thenReturn(Optional.of(subject));
        when(studentService.addSubjectToStudent(student.getId(), subject.getName())).thenReturn(studentDTO1);

        assertEquals("League",subject.getName());
        assertEquals("League", student.getSubjects().getFirst().getName());
        assertEquals(subject,student.getSubjects().getFirst());

        verify(studentRepository).save(any(Student.class));
        verify(subjectRepository).findByName(student.getSubjects().getFirst().getName());
    }

    @Test
    void addSubjectToStudent_fail() {
        String name = "League";
        Integer studentId = 1;

        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());
        when(subjectRepository.findByName(name)).thenReturn(Optional.empty());
        BadRequestException ex = assertThrows(BadRequestException.class,
                () -> studentService.addSubjectToStudent(studentId, name));

        assertEquals("Invalid student: " + studentId + "\n Please use real student ID.", ex.getMessage());

        verify(studentRepository).findById(studentId);
        verifyNoInteractions(subjectRepository,studentMapper,subjectMapper);

    }
}