package com.school.system.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
    private List<Student> students;

    @ManyToOne
    @JoinColumn(name = "school_id")
    @JsonBackReference("teachers")
    private School school;

    @OneToOne
    @JoinColumn(name = "subject_id")
    @JsonBackReference
    private Subject subject;

}
