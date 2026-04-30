package com.school.system.services;

import com.school.system.maps.SubjectMapper;
import com.school.system.maps.TeacherMapper;
import com.school.system.models.Subject;
import com.school.system.models.Teacher;
import com.school.system.modelsDTO.SubjectDTO;
import com.school.system.modelsDTO.TeacherDTO;
import com.school.system.repositories.SubjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@AutoConfigureMockMvc
class SubjectServiceTest {
    @Mock
    private SubjectRepository subjectRepository;
    @Mock
    private SubjectMapper subjectMapper;
    @Mock
    private TeacherMapper teacherMapper;

    @InjectMocks
    private SubjectService subjectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_shouldSaveAndReturnSubject() {
        TeacherDTO teacher = new TeacherDTO("","","",null);

        SubjectDTO subjectDTO = new SubjectDTO("Math","");
        Subject subject = new Subject();
        Subject savedSubject = new Subject();
        SubjectDTO expectedSubjectDTO = new SubjectDTO("Math","");

        when(subjectMapper.toEntity(subjectDTO)).thenReturn(subject);
        when(subjectRepository.save(subject)).thenReturn(savedSubject);
        when(subjectMapper.toDto(savedSubject)).thenReturn(expectedSubjectDTO);

        SubjectDTO actualSubjectDTO = subjectService.create(subjectDTO);

        assertNotNull(actualSubjectDTO);
        assertEquals(expectedSubjectDTO,actualSubjectDTO);

        verify(subjectMapper).toEntity(subjectDTO);
        verify(subjectMapper).toDto(savedSubject);
        verify(subjectRepository).save(subject);


    }
    @Test
    void createWithNullSubject_shouldThrowException() {
        SubjectDTO subjectDTO = new SubjectDTO(null,null);

        SubjectDTO resultSubjectDTO = subjectService.create(subjectDTO);

        assertNull(resultSubjectDTO);
        verifyNoInteractions(subjectMapper, subjectRepository);
    }

    @Test
    void findAll_shouldReturnMappedDtoList() {
        Subject s1 = new Subject();
        s1.setId(1);
        s1.setName("A");
        Subject s2 = new Subject();
        s2.setId(2);
        s2.setName("B");

        Teacher teacher = new Teacher();
        Teacher teacher2 = new Teacher();

        SubjectDTO subjectDto1 = new SubjectDTO("A","desc");

        SubjectDTO subjectDto2 = new SubjectDTO("B","descss");

        when(subjectRepository.findAll()).thenReturn(Arrays.asList(s1, s2));
        when(subjectMapper.toDto(s1)).thenReturn(subjectDto1);
        when(subjectMapper.toDto(s2)).thenReturn(subjectDto2);

        List<SubjectDTO> actualDto = subjectService.findAll();

        verify(subjectRepository, times(1)).findAll();
        verify(subjectMapper, times(1)).toDto(s1);
        verify(subjectMapper, times(1)).toDto(s2);

        assertEquals(2, actualDto.size());
        assertEquals(Arrays.asList(subjectDto1, subjectDto2), actualDto);
    }

    @Test
    void deleteById_shouldDeleteSubject() {
        int id = 1;
        Subject subject = new Subject();

        when(subjectRepository.findById(anyInt())).thenReturn(Optional.of(subject));

        subjectService.deleteById(1);

        verify(subjectRepository).findById(id);
        verify(subjectRepository).deleteById(id);
    }

    @Test
    void deleteById_notFound_throws() {
        when(subjectRepository.findById(999)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> subjectService.deleteById(999));
        assertTrue(ex.getMessage().contains("Subject not found"));
        verify(subjectRepository, never()).deleteById(anyInt());
    }

}