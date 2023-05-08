package org.example.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.lang.NonNull;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@ParameterObject
public class CreateUserDTO implements Serializable {
    @NonNull
    private String username;
    @NonNull
    private String password;
    @NonNull
    private String confirmPassword;
    @NonNull
    private String name;
    @NonNull
    private String surname;
}
