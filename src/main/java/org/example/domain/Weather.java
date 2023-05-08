package org.example.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.example.enums.WindDirection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@Builder
@ToString()
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Weather extends Auditable<Integer> {


    @Column(nullable = false)
    private String condition;

    @Column(nullable = false)
    private Double minTemp;

    @Column(nullable = false)
    private Double currentTemp;

    @Column(nullable = false)
    private Double maxTemp;

    @Column(nullable = false)
    private Double windSpeed;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WindDirection windDirection;

    @Builder(builderMethodName = "childBuilder")
    public Weather(Integer id, LocalDateTime createdAt, LocalDateTime updatedAt, Integer createdBy, Integer updatedBy, boolean deleted, String condition, Double minTemp, Double currentTemp, Double maxTemp, Double windSpeed, WindDirection windDirection) {
        super(id, createdAt, updatedAt, createdBy, updatedBy, deleted);
        this.condition = condition;
        this.minTemp = minTemp;
        this.currentTemp = currentTemp;
        this.maxTemp = maxTemp;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
    }

}
