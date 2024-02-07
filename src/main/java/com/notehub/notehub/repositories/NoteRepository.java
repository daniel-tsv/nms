package com.notehub.notehub.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import entities.Note;

@Repository
public interface NoteRepository extends JpaRepository<Note, UUID> {
    Optional<Note> findByTitle(String title);
}
