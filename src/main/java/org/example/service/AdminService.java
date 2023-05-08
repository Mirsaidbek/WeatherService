package org.example.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.example.domain.City;
import org.example.domain.User;
import org.example.domain.Weather;
import org.example.dto.city.CreateCityDTO;
import org.example.dto.city.UpdateCityDTO;
import org.example.dto.weather.UpdateWeatherDTO;
import org.example.enums.WindDirection;
import org.example.repository.CityRepository;
import org.example.repository.UserRepository;
import org.example.repository.WeatherRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final CityRepository cityRepository;
    private final WeatherRepository weatherRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public City createCity(CreateCityDTO dto, Weather weather) {
        City city = City.childBuilder()
                .name(dto.getName())
                .country(dto.getCountry())
                .deleted(false)
                .weather(weather)
                .build();
        cityRepository.save(city);
        return city;
    }

    public City createCityAndWeather(@NonNull CreateCityDTO cityDTO) {

        if (!cityRepository.findByName(cityDTO.getName(), cityDTO.getCountry())
                .isPresent()) {
            WindDirection windDirection = cityDTO.getWindDirection();
            Weather weather = Weather.childBuilder()
                    .condition(cityDTO.getCondition())
                    .minTemp(cityDTO.getMinTemp())
                    .currentTemp(cityDTO.getCurrentTemp())
                    .maxTemp(cityDTO.getMaxTemp())
                    .windSpeed(cityDTO.getWindSpeed())
                    .windDirection(windDirection)
                    .build();

            weatherRepository.save(weather);

            return createCity(cityDTO, weather);
        } else throw new RuntimeException("City already exists");
    }

    public List<User> getUserList() {
        return userRepository.findAll();
    }

    public List<City> getCityList() {
        return cityRepository.findAll();
    }

    public String editCity(UpdateCityDTO dto) {
        City city = cityRepository.findByName(dto.getName(), dto.getCountry())
                .orElseThrow(() -> new RuntimeException("City with this name and country not found"));
        city.setDeleted(dto.isDeleted());

        if (dto.isDeleted()) {
            userService.editCityOfUser(city);
        }

        cityRepository.save(city);

        return "City updated";
    }

    public Weather editCityWeather(UpdateWeatherDTO dto) {
        City city = cityRepository.findByName(dto.getCityName(), dto.getCountryName())
                .orElseThrow(() -> new RuntimeException("City with this name and country name not found"));

        Weather weather = Weather.childBuilder()
                .condition(dto.getCondition())
                .minTemp(dto.getMinTemp())
                .currentTemp(dto.getCurrentTemp())
                .maxTemp(dto.getMaxTemp())
                .windSpeed(dto.getWindSpeed())
                .windDirection(dto.getWindDirection())
                .build();

        city.setWeather(weather);
        cityRepository.updateCityWeather(city.getWeather(), city.getId());

        return weather;
    }
}
