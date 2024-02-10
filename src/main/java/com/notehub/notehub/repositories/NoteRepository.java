package com.notehub.notehub.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.notehub.notehub.entities.Note;
import com.notehub.notehub.entities.User;

@Repository
public interface NoteRepository extends JpaRepository<Note, UUID> {
    Optional<Note> findByTitle(String title);

    List<Note> findByUser(User user);

    int countByUser(User user);
}
