package org.example.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springdoc.api.annotations.ParameterObject;

@Setter
@Getter
@AllArgsConstructor
@ParameterObject
public class SubscribeToCityDTO {
    @NonNull
    private String username;
    @NonNull
    private String cityName;
    @NonNull
    private String countryName;
}
