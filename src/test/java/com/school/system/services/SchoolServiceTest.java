package com.school.system.services;

import com.school.system.maps.SchoolMapper;
import com.school.system.models.Address;
import com.school.system.models.School;
import com.school.system.models.Student;
import com.school.system.models.Teacher;
import com.school.system.modelsDTO.SchoolDTO;
import com.school.system.modelsDTO.StudentDTO;
import com.school.system.modelsDTO.TeacherDTO;
import com.school.system.repositories.SchoolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SchoolServiceTest {

    @InjectMocks
    private SchoolService schoolService;
    @Mock
    SchoolRepository schoolRepository;
    @Mock
    SchoolMapper schoolMapper;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldAddStudentToSchool() {
    }

    @Test
    void shouldAddTeacherToSchool() {
    }

    @Test
    void shouldCreate() {
    }

    @Test
    void shouldFindAll() {
        List<School> schools = new ArrayList<>();
        List<StudentDTO> students = new ArrayList<>();
        List<TeacherDTO> teachers = new ArrayList<>();
        Address address = new Address();
        schools.add(new School(
                "MIT",
                address,
                "High School"
        ));
        schools.add(new School(
                "MIT",
                address,
                "Middle School"
        ));
        schools.add(new School(
                "JWT",
                address,
                "Kindergarten"
        ));

        when(schoolRepository.findAll()).thenReturn(schools);
        when(schoolMapper.toDto(any(School.class)))
                .thenReturn(new SchoolDTO(
                        "",
                        "",
                        students,
                        teachers
                ));
        List<SchoolDTO> schoolDTOs = schoolService.findAll();

        assertEquals(schools.size(), schoolDTOs.size());
        verify(schoolRepository, times(1)).findAll();
    }

    @Test
    void shouldDeleteById() {
    }
}