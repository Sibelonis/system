package com.school.system.models;

import com.fasterxml.jackson.annotation.JsonValue;
import com.school.system.Difficulty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private List<Student> students;
    @OneToOne(mappedBy = "subject")
    private Teacher teacher;
}
