package app_rest_libraryfinalproject.model;



import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "person_security")
@NoArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "age")
    Integer age;

    @Column(name = "phoneNumber")
    String phoneNumber;

    @Column(name = "year_of_birth")
    private Integer yearOfBirth;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;

    @Column(name = "email")
    private String email;

    @Column(name = "createdAt")
    LocalDateTime createdAt;

    @Column(name = "removedAt")
    LocalDateTime removedAt;

    @Column(name = "createdPerson")
    String createdPerson;

    @Column(name = "removedPerson")
    String removedPerson;

    @Column(name = "removed")
    Boolean removed;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Book> books;



}
