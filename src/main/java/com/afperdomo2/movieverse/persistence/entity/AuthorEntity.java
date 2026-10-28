package com.afperdomo2.movieverse.persistence.entity;

import java.time.LocalDate;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "authors", uniqueConstraints = @UniqueConstraint(
        name = "uk_authors_name_last_name",
        columnNames = { "name", "last_name" }))
@Getter
@Setter
@NoArgsConstructor
public class AuthorEntity extends AuditableEntity {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(length = 100)
    private String nationality;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(length = 1000)
    private String biography;

    @Column(name = "is_active", nullable = false)
    @ColumnDefault("true")
    private boolean active = true;
}
