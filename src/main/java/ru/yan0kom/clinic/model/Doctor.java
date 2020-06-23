package ru.yan0kom.clinic.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"full_name", "specialization_id"})})
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", length = 150, nullable = false)
    private String fullName;

    @OneToOne
    @JoinColumn(name = "specialization_id", nullable = false)
    private Specialization specialization;
}
