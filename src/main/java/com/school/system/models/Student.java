package com.school.system.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
@ToString(exclude = {"school", "teacher", "subjects"})
@Entity
public class Student {

    @Id
    @GeneratedValue
    private Integer id;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    private String homeRoom;
    @NotNull
    private int age;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "teacher_id")
    @JsonBackReference("students-teachers")
    private Teacher teacher;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "school_id")
    @JsonBackReference("school-students")
    private School school;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "student_subjects",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"
            )
    )
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private List<Subject> subjects;

    public Student(String firstName, String lastName, String homeRoom, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.homeRoom = homeRoom;
        this.age = age;
    }


    public void addSubject(Subject subject) {
        if (subjects == null) subjects = new ArrayList<>();
        if (!subjects.contains(subject)) {
            subjects.add(subject);
            if (subject.getStudents() == null) subject.setStudentsList(new ArrayList<>());
            if (!subject.getStudents().contains(this)) subject.getStudents().add(this);
        }
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student s = (Student) o;
        return id != null && id.equals(s.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
