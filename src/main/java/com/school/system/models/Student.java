package com.school.system.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
@ToString(exclude = {"school", "teacher", "subjects"})
@Entity
public class Student {

    @Id
    @GeneratedValue
    private Integer id;
    private String firstName;
    private String lastName;
    private String homeRoom;
    private int age;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    @JsonBackReference("students-teachers")
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "school_id")
    @JsonBackReference("school-students")
    private School school;

    @ManyToMany(mappedBy = "students")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private List<Subject> subjects;

    public void addSubject(Subject subject) {
        if (subjects == null) subjects = new ArrayList<>();
        if (!subjects.contains(subject)) {
            subjects.add(subject);
            if (subject.getStudents() == null) subject.setStudentsList(new ArrayList<>());
            if (!subject.getStudents().contains(this)) subject.getStudents().add(this);
        }
    }

}
