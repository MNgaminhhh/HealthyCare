package com.hcmute.HealthyCare.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Disease")
public class Disease {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 5000)
    private String disease;
    private List<String> photo;

    @ManyToMany
    @JoinTable(
        name = "Manifestation",
        joinColumns = @JoinColumn(name = "disease_id"),
        inverseJoinColumns = @JoinColumn(name = "manifestation_id")
    )
    @JsonManagedReference
    private List<Manifestation> manifestations;

}
