package org.example.dto.city;

import lombok.*;
import org.example.domain.Weather;
import org.example.enums.WindDirection;
import org.springdoc.api.annotations.ParameterObject;

@Setter
@Getter
@AllArgsConstructor
@ParameterObject
public class CreateCityDTO {
    @NonNull
    private String name;
    @NonNull
    private String country;

    @NonNull
    private String condition;
    @NonNull
    private Double minTemp;
    @NonNull
    private Double currentTemp;
    @NonNull
    private Double maxTemp;
    @NonNull
    private Double windSpeed;
    @NonNull
    private WindDirection windDirection;

}
