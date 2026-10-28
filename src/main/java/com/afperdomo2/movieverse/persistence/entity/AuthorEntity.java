package com.afperdomo2.movieverse.persistence.entity;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "authors", uniqueConstraints = @UniqueConstraint(
        name = "uk_authors_name_last_name",
        columnNames = { "name", "last_name" }))
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
public class AuthorEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

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

    @Version
    @Column(nullable = false)
    @ColumnDefault("0")
    private Long version;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    @ColumnDefault("now()")
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    @ColumnDefault("now()")
    private Instant updatedAt;
}
