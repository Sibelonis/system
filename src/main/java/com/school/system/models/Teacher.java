package com.school.system.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
    private String firstName;
    private String lastName;
    private String title;
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

    public void addStudent(Student student) {
        students.add(student);
        student.setTeacher(this);
    }

    public void addSubject(Subject subject) {
        this.subject = subject;
        subject.setTeacher(this);
    }
}
