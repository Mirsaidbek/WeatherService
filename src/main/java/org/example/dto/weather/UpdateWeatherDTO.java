package org.example.dto.weather;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.example.enums.WindDirection;
import org.springdoc.api.annotations.ParameterObject;

@Setter
@Getter
@AllArgsConstructor
@ParameterObject
public class UpdateWeatherDTO {

    @NonNull
    private String cityName;
    @NonNull
    private String countryName;

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
