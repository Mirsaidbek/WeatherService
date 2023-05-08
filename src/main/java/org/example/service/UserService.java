package org.example.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.example.domain.AuthUser;
import org.example.domain.City;
import org.example.domain.User;
import org.example.dto.user.GetUserDetailsDTO;
import org.example.dto.user.SubscribeToCityDTO;
import org.example.dto.weather.GetWeatherInfoDTO;
import org.example.repository.AuthUserRepository;
import org.example.repository.CityRepository;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final AuthUserRepository authUserRepository;
    private final UserRepository userRepository;
    private final CityRepository cityRepository;

    public GetUserDetailsDTO getUserDetails(@NonNull String username) {
        Optional<AuthUser> authUser = authUserRepository.findByUsername(username);
        authUser.orElseThrow(() ->
                new RuntimeException("User with this username not found")
        );

        Optional<User> user = userRepository.findById(authUser.get().getId());
        user.orElseThrow(() ->
                new RuntimeException("User with this id not found")
        );

        return GetUserDetailsDTO.builder()
                .username(username)
                .name(user.get().getName())
                .surname(user.get().getSurname())
                .cities(user.get().getCities())
                .role(authUser.orElseThrow().getRole())
                .active(authUser.get().isActive())
                .build();
    }

    public List<City> getCityList() {
        return cityRepository.getAllActiveCities().orElseThrow(() ->
                new RuntimeException("No cities found")
        );
    }

    public City subscribeToCity(SubscribeToCityDTO dto) {

        User user = userRepository.findByUsername(dto.getUsername()).orElseThrow(() ->
                new RuntimeException("User not found"));

        City city = cityRepository.findActiveCityByName(dto.getCityName(), dto.getCountryName()).orElseThrow(() ->
                new RuntimeException("City not found"));

        if (user.getCities().contains(city)) {
            throw new RuntimeException("You already subscribed to this city");
        }

        user.getCities().add(city);

        userRepository.save(user);

        return city;
    }

    public List<GetWeatherInfoDTO> getSubscriptions(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User with " + username + " username not found"));

        List<GetWeatherInfoDTO> dtoList = new ArrayList<>();

        List<City> cities = user.getCities();

        for (City city : cities) {
            dtoList.add(GetWeatherInfoDTO.builder()
                    .cityName(city.getName())
                    .countryName(city.getCountry())
                    .condition(city.getWeather().getCondition())
                    .minTemp(city.getWeather().getMinTemp())
                    .maxTemp(city.getWeather().getMaxTemp())
                    .windSpeed(city.getWeather().getWindSpeed())
                    .windDirection(city.getWeather().getWindDirection())
                    .build());
        }

        return dtoList;
    }

    //    @Async
    public void editCityOfUser(City city) {
        Integer cityId = city.getId();
        List<User> users = userRepository.findAll();

        for (User user : users) {
            if (user.getCities().isEmpty()) continue;
            for (City userCity : user.getCities()) {
                if (Objects.equals(userCity.getId(), cityId) && userCity.isDeleted()) {
                    user.getCities().remove(city);
                    userRepository.save(user);
                }
                break;
            }
        }

    }

    public void createUser(Integer id, String name, String surname) {
        User user = User.builder()
                .authUserId(id)
                .name(name)
                .surname(surname)
                .build();

        userRepository.save(user);
    }
}
