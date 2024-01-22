package com.notehub.notehub.user;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import com.notehub.notehub.note.Note;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "users")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@EqualsAndHashCode(of = "uuid")
@ToString(exclude = { "notes", "password" })
public class User {

    @Id
    @Column(updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID uuid;

    @NotBlank
    @Size(min = 2, max = 255)
    @Column(unique = true, nullable = false, length = 255)
    String username;

    @NotBlank
    @Column(nullable = false, length = 255)
    String password;

    @Email
    @NotBlank
    @Column(unique = true, nullable = false, length = 255)
    String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<Note> notes;

    @CreationTimestamp
    @Column(updatable = false)
    Instant createdAt;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email.toLowerCase();
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }
}
