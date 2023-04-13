package ru.clevertec.ecl.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "users")
public class User implements BaseEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @NotBlank(message = "First name must contain at least 1 character")
    @Column(name = "first_name")
    private String firstName;
    @NotBlank(message = "Last name must contain at least 1 character")
    @Column(name = "last_name")
    private String lastName;
    @NotNull(message = "Email cannot be null")
    @Email(message = "Email should be valid")
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy="user")
    private Set<Order> orders = new HashSet<>();
}
