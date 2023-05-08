package org.example.dto.weather;

import lombok.*;
import org.example.enums.WindDirection;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetWeatherInfoDTO {
    private String cityName;
    private String countryName;
    private String condition;
    private Double minTemp;
    private Double maxTemp;
    private Double windSpeed;
    private WindDirection windDirection;
}
