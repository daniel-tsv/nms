package com.notehub.notehub.note;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.notehub.notehub.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "notes")
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = { "uuid" })
@ToString(exclude = { "contents", "user" })
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false)
    private UUID uuid;

    @NotBlank(message = "Title cannot be empty or consist only of whitespace characters")
    @Column(unique = true, nullable = false, length = 255)
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;

    @Lob
    private String contents;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_uuid")
    private User user;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    public Note(String title, User user) {
        this.title = title;
        this.user = user;
    }
}