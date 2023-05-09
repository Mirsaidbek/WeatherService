package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.example.domain.City;
import org.example.dto.user.SubscribeToCityDTO;
import org.example.dto.weather.GetWeatherInfoDTO;
import org.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "User", description = "User API")
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;


    @Operation(summary = "This API is used for getting the list of all cities",
            description = "This API is used for getting the list of all cities")
    @GetMapping("/cities-list")
    public ResponseEntity<List<City>> citiesList() {
        return ResponseEntity.ok(userService.getCityList());
    }


    @Operation(summary = "This API is used for subscribing to a city",
            description = "This API is used for subscribing to a city")
    @PutMapping("/subscribe-to-city")
    public ResponseEntity<City> subscribeToCity(
            @NonNull @Valid SubscribeToCityDTO dto
    ) {
        return ResponseEntity.ok(userService.subscribeToCity(dto));
    }

    @Operation(summary = "This API is used for unsubscribing from a city",
            description = "This API is used for unsubscribing from a city")
    @PutMapping("/unsubscribe-from-city")
    public ResponseEntity<City> unsubscribeFromCity(
            @NonNull @Valid SubscribeToCityDTO dto
    ) {
        return ResponseEntity.ok(userService.unsubscribeToCity(dto));
    }

    @Operation(summary = "This API is used for getting the information about the weather in subscribed cities ",
            description = "This API is used for getting the information about the weather in subscribed cities")
    @GetMapping("/get-subscriptions/{username}")
    public ResponseEntity<List<GetWeatherInfoDTO>> getSubscriptions(
            @NonNull @NotBlank @PathVariable String username
    ) {
        return ResponseEntity.ok(userService.getSubscriptions(username));
    }

}

//Методы клиента
//register - получение постоянного токена по логину и паролю
//cities-list - список городов
//subscribe-to-city - подписка на город
//get-subscriptions - получить данные о погоде по подписанным городам (использовать реактивность)
