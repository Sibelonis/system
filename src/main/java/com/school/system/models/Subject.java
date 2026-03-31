package com.school.system.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.school.system.Difficulty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Subject {

    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    @Embedded
    private Difficulty difficulty;
    private String description;
    @ManyToMany
    @JoinTable(
            name = "student_subjects"
            , joinColumns = {
                    @JoinColumn(name = "subject_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "student_id")
            }
    )
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private List<Student> students;

    @OneToOne(mappedBy = "subject")
    @JsonManagedReference("teachers-subject")
    private Teacher teacher;

    public void setStudentsList(List<Student> students) { this.students = students; }

    public void addStudent(Student student) {
        if (students == null) students = new ArrayList<>();
        if (!students.contains(student)) {
            students.add(student);
            if (student.getSubjects() == null) student.setSubjects(new ArrayList<>());
            if (!student.getSubjects().contains(this)) student.getSubjects().add(this);
        }
    }
}
