package com.afperdomo2.movieverse.persistence.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.afperdomo2.movieverse.domain.Genre;
import com.afperdomo2.movieverse.persistence.converter.GenreConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "movies")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
public class MovieEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false, unique = true, length = 255)
    private String title;

    @Column(nullable = false)
    private Integer duration;

    @Convert(converter = GenreConverter.class)
    @Column(nullable = false, length = 50)
    private Genre genre;

    @Column(name = "release_date", nullable = false)
    private LocalDate releaseDate;

    @Column(precision = 3, scale = 2)
    private BigDecimal rating;

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
