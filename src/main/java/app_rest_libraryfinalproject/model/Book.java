package app_rest_libraryfinalproject.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Entity
@Table(name = "book")
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "yearOfProduction")
    private Integer yearOfProduction;

    @Column(name = "author")
    private String author;

    @Column(name = "annotation", length = 1000)
    private String annotation;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @Column(name = "removedAt")
    private LocalDateTime removedAt;

    @Column(name = "createdPerson")
    private String createdPerson;

    @Column(name = "updatedPerson")
    private String updatedPerson;

    @Column(name = "removedPerson")
    private String removedPerson;

    @Column(name = "isBookDeleted")
    private Boolean isBookDeleted;

    @Column(name = "coverPath")
    private String coverPath;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Person owner;
}
