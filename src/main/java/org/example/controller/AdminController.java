package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.example.domain.City;
import org.example.domain.User;
import org.example.domain.Weather;
import org.example.dto.city.CreateCityDTO;
import org.example.dto.city.UpdateCityDTO;
import org.example.dto.user.GetUserDetailsDTO;
import org.example.dto.user.UpdateUserDTO;
import org.example.dto.weather.UpdateWeatherDTO;
import org.example.repository.AuthUserRepository;
import org.example.repository.UserRepository;
import org.example.service.AdminService;
import org.example.service.AuthUserService;
import org.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Admin", description = "Admin API")
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;
    private final AuthUserRepository authUserRepository;
    private final AuthUserService authUserService;
    private final UserRepository userRepository;


    @Operation(summary = "This API is used for creating city and weather for it",
            description = "This API is used for creating city and weather for it")
    @PostMapping("/create-city")
    public ResponseEntity<City> createCity(
            @NonNull @Valid CreateCityDTO city
    ) {
        return ResponseEntity.ok(adminService.createCityAndWeather(city));
    }


    @Operation(summary = "This API is used for getting the list of all users",
            description = "This API is used for getting the list of all users")
    @GetMapping("/user-list")
    public ResponseEntity<List<User>> userList() {
        return ResponseEntity.ok(adminService.getUserList());
    }


    @Operation(summary = "This API is used for getting the all details of a user",
            description = "This API is used for getting the all details of a user")
    @GetMapping("/user-details/{username}")
    public ResponseEntity<GetUserDetailsDTO> userDetails(
            @NonNull @PathVariable String username
    ) {
        return ResponseEntity.ok(userService.getUserDetails(username));
    }


    @Operation(summary = "This API is used for editing user - blocking user",
            description = "This API is used for editing user - blocking user")
    @PutMapping("/edit-user")
    public ResponseEntity<String> editUser(
            @NonNull @Valid UpdateUserDTO dto
    ) {
        authUserService.editUser(dto);

        return ResponseEntity.ok("User Edited successfully");
    }


    @Operation(summary = "This API is used for getting the list of all cities",
            description = "This API is used for getting the list of all cities")
    @GetMapping("/cities-list")
    public ResponseEntity<List<City>> cityList() {
        return ResponseEntity.ok(adminService.getCityList());
    }


    @Operation(summary = "This API is used for editing city - to change city activeness",
            description = "This API is used for editing city - to change city activeness")
    @PutMapping("/edit-city")
    public ResponseEntity<String> editCity(
            @NonNull @Valid UpdateCityDTO dto
    ) {
        return ResponseEntity.ok(adminService.editCity(dto));
    }


    @Operation(summary = "This API is used for updating city weather",
            description = "This API is used for updating city weather")
    @PutMapping("/update-city-weather")
    public ResponseEntity<Weather> editCityWeather(
            @NonNull @Valid UpdateWeatherDTO dto
    ) {
        return ResponseEntity.ok(adminService.editCityWeather(dto));
    }


}


//Методы админа

//user-list - получение списка всех пользователей
//user-details - получения деталей подписки пользователей
//edit-user - редактирование пользователя
//cities-list - список городов
//edit-city - редактирование города
//update-city-weather - обновить погоду города
