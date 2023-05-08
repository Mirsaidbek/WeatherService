package org.example.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.example.enums.Role;
import org.springdoc.api.annotations.ParameterObject;

@Setter
@Getter
@AllArgsConstructor
@ParameterObject
public class UpdateUserDTO {
    @NonNull
    private String username;
    @NonNull
    private Role role;
    @NonNull
    private boolean active;
}
