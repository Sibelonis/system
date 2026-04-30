package com.school.system.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.school.system.Difficulty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
    @NotEmpty
    @NotNull
    private String name;
    @Embedded
    private Difficulty difficulty;

    private String description;

    @ManyToMany(mappedBy = "subjects",cascade = CascadeType.PERSIST)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private List<Student> students;

    @OneToOne(mappedBy = "subject",cascade = CascadeType.PERSIST)
    @JsonManagedReference("teacher-subject")
    private Teacher teacher;

    public Subject(String name, Difficulty difficulty, String description, List<Student> student, Teacher teacher) {
        this.name = name;
        this.difficulty = difficulty;
        this.description = description;
        this.students = new ArrayList<>();
        this.teacher = new Teacher();

    }

    public void setStudentsList(List<Student> students) {
        this.students = students;
    }

    public void addStudent(Student student) {
        if (students == null) students = new ArrayList<>();
        if (!students.contains(student)) {
            students.add(student);
            if (student.getSubjects() == null) student.setSubjects(new ArrayList<>());
            if (!student.getSubjects().contains(this)) student.getSubjects().add(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subject)) return false;
        Subject subject = (Subject) o;
        return id != null && id.equals(subject.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
