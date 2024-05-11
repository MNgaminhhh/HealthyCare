package com.hcmute.HealthyCare.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;
    private String caption;

    @ManyToOne
    @JoinColumn(name = "paragraph_id")
    private Paragraph paragraph;
}