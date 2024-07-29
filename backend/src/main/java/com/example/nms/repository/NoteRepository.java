package com.example.nms.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.nms.entity.Note;
import com.example.nms.entity.User;

@Repository
public interface NoteRepository extends JpaRepository<Note, UUID> {

    Optional<Note> findByTitleAndUser(String title, User user);

    int countByUser(User user);

    void deleteByTitleAndUser(String title, User user);

    Page<Note> findAllByUser(Pageable pageable, User user);

    boolean existsByTitleAndUser(String title, User user);

    Page<Note> findByTitleContainingIgnoreCaseAndUser(String term, Pageable pageable, User user);

    Page<Note> findByContentsContainingIgnoreCaseAndUser(String term, Pageable pageable, User user);
}
