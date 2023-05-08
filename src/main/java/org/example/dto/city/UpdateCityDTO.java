package org.example.dto.city;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springdoc.api.annotations.ParameterObject;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ParameterObject
public class UpdateCityDTO {
    private String name;
    private String country;
    private boolean deleted;
}
