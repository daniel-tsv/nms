package com.notehub.notehub.user;

import java.util.List;
import java.util.UUID;

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
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = { "uuid" })
@ToString(exclude = { "notes" })
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false)
    private UUID uuid;

    @NotBlank(message = "Username cannot be empty or consist only of whitespace characters")
    @Column(unique = true, nullable = false, length = 255)
    @Size(min = 2, max = 255, message = "Username length must be between 2 and 255 characters")
    private String username;

    @NotBlank(message = "Password cannot be empty or consist only of whitespace characters")
    @Column(nullable = false, length = 255)
    private String password;

    @Email
    @NotBlank(message = "Email cannot be empty or consist only of whitespace characters")
    @Column(unique = true, nullable = false, length = 255)
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Note> notes;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
