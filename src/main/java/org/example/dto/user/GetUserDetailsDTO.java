package org.example.dto.user;

import lombok.*;
import org.example.domain.City;
import org.example.enums.Role;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetUserDetailsDTO implements Serializable {
    private String username;
    private String name;
    private String surname;
    private List<City> cities;
    private Role role;
    private boolean active;
}
