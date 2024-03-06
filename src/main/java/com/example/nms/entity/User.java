package com.example.nms.entity;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "users")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "uuid")
@ToString(exclude = { "notes", "password" })
@AllArgsConstructor
@NoArgsConstructor
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

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    Set<Role> roles = new HashSet<>();

    @Column(nullable = false)
    Boolean isAccountNonExpired = Boolean.TRUE;

    @Column(nullable = false)
    Boolean isAccountNonLocked = Boolean.TRUE;

    @Column(nullable = false)
    Boolean isCredentialsNonExpired = Boolean.TRUE;

    @Column(nullable = false)
    Boolean isEnabled = Boolean.TRUE;

    public User(String username, String password, String email, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.email = email != null ? email.toLowerCase() : email;
        this.roles = roles;
    }

    public void setEmail(String email) {
        this.email = email != null ? email.toLowerCase() : email;
    }
}
