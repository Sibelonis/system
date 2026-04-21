package com.school.system.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"school", "students", "subject"})
@Entity
public class Teacher {

    @Id
    @GeneratedValue
    private Integer id;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    private String title;
    @NotNull
    private int age;
    private String homeRoom;

    @OneToMany(mappedBy = "teacher")
    @JsonManagedReference("students-teachers")
    private List<Student> students;

    @ManyToOne
    @JoinColumn(name = "school_id")
    @JsonBackReference("school-teachers")
    private School school;

    @OneToOne
    @JoinColumn(name = "subject_id")
    @JsonBackReference("teachers-subject")
    private Subject subject;

    public Teacher(String firstName, String lastName, String title, String homeRoom, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.title = title;
        this.homeRoom = homeRoom;
        this.age = age;
    }

    public void addStudent(Student student) {
        if (students == null) students = new ArrayList<>();
        students.add(student);
        student.setTeacher(this);
    }

    public void addSubject(Subject subject) {
        this.subject = subject;
        subject.setTeacher(this);
    }
}
