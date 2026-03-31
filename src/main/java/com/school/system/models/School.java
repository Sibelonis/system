package com.school.system.models;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"students", "teachers"})
@Entity
public class School {

    @Id
    @GeneratedValue
    private Integer schoolId;
    private String schoolName;
    @Embedded
    private Address schoolAddress;
    private String degree;
    private int studentCount;
    @OneToMany(mappedBy = "school")
    @JsonManagedReference("school-students")
    private List<Student> students;
    @OneToMany(mappedBy = "school")
    @JsonManagedReference("school-teachers")
    private List<Teacher> teachers;

    public School(String schoolName, Address schoolAddress, String degree) {
        this.schoolName = schoolName;
        this.schoolAddress = schoolAddress;
        this.degree = degree;

    }


    public void addStudent(Student student) {
        if (students == null) {
            students = new ArrayList<>();
        }
        students.add(student);
        student.setSchool(this);
        studentCount = students.size();
    }

    public void addTeacher(Teacher teacher) {
        if (teachers == null) {
            teachers = new ArrayList<>();
        }

        teachers.add(teacher);
        teacher.setSchool(this);
    }

}
