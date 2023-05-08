package org.example.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
//@Builder
@ToString()
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class City extends Auditable<Integer> {


    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String country;

    @Column(columnDefinition = "boolean default 'f'")
    private boolean deleted;


    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Weather weather;

    @Builder(builderMethodName = "childBuilder")
    public City(Integer id, LocalDateTime createdAt, LocalDateTime updatedAt, Integer createdBy, Integer updatedBy, boolean deleted, String name, String country, Weather weather) {
        super(id, createdAt, updatedAt, createdBy, updatedBy, deleted);
        this.name = name;
        this.country = country;
        this.weather = weather;
    }


}
