package com.afperdomo2.movieverse.persistence.crud;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.afperdomo2.movieverse.persistence.entity.AuthorEntity;

public interface CrudAuthorEntity extends JpaRepository<AuthorEntity, UUID> {
    AuthorEntity findByNameAndLastName(String name, String lastName);
}
