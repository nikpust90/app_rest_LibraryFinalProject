package app_rest_libraryfinalproject.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Entity
@Builder
@Table(name = "book")
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name")
    String name;

    @Column(name = "yearOfProduction")
    Integer yearOfProduction;

    @Column(name = "author")
    String author;

    @Column(name = "annotation", length = 1000)
    String annotation;

    @Column(name = "createdAt")
    LocalDateTime createdAt;

    @Column(name = "updatedAt")
    LocalDateTime updatedAt;

    @Column(name = "removedAt")
    LocalDateTime removedAt;

    @Column(name = "createdPerson")
    String createdPerson;

    @Column(name = "updatedPerson")
    String updatedPerson;

    @Column(name = "removedPerson")
    String removedPerson;

    @Column(name = "isBookDeleted")
    Boolean isBookDeleted;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Person owner;

}
