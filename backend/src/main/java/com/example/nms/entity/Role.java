package com.example.nms.entity;

import java.util.UUID;

import com.example.nms.constants.MessageConstants;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "roles")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "uuid")
public class Role {

    @Id
    @Column(updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID uuid;

    @NotBlank(message = MessageConstants.ROLE_NAME_EMPTY)
    @Pattern(regexp = "^ROLE_.*", message = MessageConstants.ROLE_NAME_INVALID)
    @Size(min = 6, message = MessageConstants.ROLE_LENGTH)
    @Column(unique = true, nullable = false, length = 255)
    String name;

    public Role(String name) {
        this.name = name.toUpperCase();
    }

    public void setRole(String name) {
        this.name = name.toUpperCase();
    }
}
