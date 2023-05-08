package org.example.dto.weather;

import lombok.*;
import org.example.enums.WindDirection;
import org.springdoc.api.annotations.ParameterObject;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
//@NoArgsConstructor
@ParameterObject
public class CreateWeatherDTO implements Serializable {

    @NonNull
    private String condition;

    @NonNull
    private Double minTemp;

    @NonNull
    private Double maxTemp;

    @NonNull
    private Double windSpeed;

    @NonNull
    private WindDirection windDirection;

}
