package com.school.system.models;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class School {

    @Id
    @GeneratedValue
    private Integer schoolId;
    private String schoolName;
    private String schoolAddress; //prerobit ako objekt
    private String degree;
    private int studentCount;
    @OneToMany(mappedBy = "school")
    @JsonManagedReference("school-students")
    private List<Student> students;
    @OneToMany(mappedBy = "school")
    @JsonManagedReference("school-teachers")
    private List<Teacher> teachers;


    public void addStudent(Student student) {
        students.add(student);
        student.setSchool(this);
        studentCount = students.size();
    }

    public void addTeacher(Teacher teacher) {
        teachers.add(teacher);
        teacher.setSchool(this);
    }
}
