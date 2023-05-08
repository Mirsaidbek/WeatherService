package org.example.dto.token;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenRequest {
    @NotBlank
    String username;
    @NotBlank
    String password;

}