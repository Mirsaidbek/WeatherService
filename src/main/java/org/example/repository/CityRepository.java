package org.example.repository;

import org.example.domain.City;
import org.example.domain.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {
    Optional<City> findByNameIgnoreCaseAndCountryIgnoreCase(String name, String country);

    @Query("SELECT c FROM City c WHERE c.name = ?1 AND c.country = ?2")
    Optional<City> findByName(String name, String country);

    @Query("SELECT c FROM City c WHERE c.deleted = false")
    Optional<List<City>> getAllActiveCities();

    @Query("SELECT c FROM City c WHERE c.name = ?1 AND c.country = ?2 AND c.deleted = false")
    Optional<City> isActive(String cityName, String countryName);

    @Query("SELECT c FROM City c WHERE c.name = ?1 AND c.country = ?2 AND c.deleted = false")
    Optional<City> findActiveCityByName(String cityName, String countryName);

    @Modifying
    @Transactional
    @Query("update City c set c.weather = ?1 where c.id = ?2")
    Integer updateCityWeather(Weather weather, Integer id);

}
