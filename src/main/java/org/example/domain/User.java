package org.example.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString()
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "users")

public class User {

    @Id
    private Integer authUserId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(name = "user_cities", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "authUserId"), inverseJoinColumns = @JoinColumn(name = "city_id", referencedColumnName = "id"))
    private List<City> cities;

    public User(int authUserId) {
        this.authUserId = authUserId;
    }

    public User(Integer authUserId, String name, String surname, List<City> cities) {
        this.authUserId = authUserId;
        this.name = name;
        this.surname = surname;
        this.cities = cities;
    }

}
