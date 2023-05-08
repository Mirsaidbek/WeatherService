package org.example.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.example.enums.Role;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
//@Builder(builderMethodName = "childBuilder")
@ToString()
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AuthUser extends Auditable<Integer> {


    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private boolean active;


    @Builder(builderMethodName = "childBuilder")
    public AuthUser(Integer id, LocalDateTime createdAt, LocalDateTime updatedAt, Integer createdBy, Integer updatedBy, boolean deleted, String username, String password, Role role, boolean active) {
        super(id, createdAt, updatedAt, createdBy, updatedBy, deleted);
        this.username = username;
        this.password = password;
        this.role = role;
        this.active = active;
    }

}
