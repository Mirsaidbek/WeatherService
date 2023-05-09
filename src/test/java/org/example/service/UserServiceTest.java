package org.example.service;

import org.example.domain.AuthUser;
import org.example.domain.City;
import org.example.domain.User;
import org.example.domain.Weather;
import org.example.dto.user.GetUserDetailsDTO;
import org.example.dto.user.SubscribeToCityDTO;
import org.example.enums.Role;
import org.example.enums.WindDirection;
import org.example.repository.AuthUserRepository;
import org.example.repository.CityRepository;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {UserService.class})
@ExtendWith(SpringExtension.class)
class UserServiceTest {
    @MockBean
    private AuthUserRepository authUserRepository;

    @MockBean
    private CityRepository cityRepository;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    void testGetUserDetails() {
        AuthUser authUser = AuthUser.childBuilder()
                .active(true)
                .createdAt(LocalDateTime.now())
                .createdBy(1)
                .deleted(true)
                .id(1)
                .password("iloveyou")
                .role(Role.USER)
                .updatedAt(LocalDateTime.now())
                .updatedBy(1)
                .username("user")
                .build();

        when(authUserRepository.findByUsername(any())).thenReturn(Optional.of(authUser));

        User user = User.builder()
                .authUserId(1)
                .cities(new ArrayList<>())
                .name("Name")
                .surname("Userbekov")
                .build();


        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(user));

        GetUserDetailsDTO actualUserDetails = userService.getUserDetails("user");

        assertTrue(actualUserDetails.getCities().isEmpty());
        assertTrue(actualUserDetails.isActive());
        assertEquals("user", actualUserDetails.getUsername());
        assertEquals("Userbekov", actualUserDetails.getSurname());
        assertEquals(Role.USER, actualUserDetails.getRole());
        assertEquals("Name", actualUserDetails.getName());

        verify(authUserRepository).findByUsername(any());
        verify(userRepository).findById(Mockito.<Integer>any());
    }

    @Test
    void testGetUserDetails2() {
        User user = User.builder()
                .authUserId(1)
                .cities(new ArrayList<>())
                .name("Name")
                .surname("Userbekov")
                .build();

        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class, () -> userService.getUserDetails("user"));
        verify(authUserRepository).findByUsername(any());
    }


    @Test
    void testGetCityList() {
        ArrayList<City> cityList = new ArrayList<>();

        when(cityRepository.getAllActiveCities()).thenReturn(Optional.of(cityList));

        List<City> actualCityList = userService.getCityList();

        assertSame(cityList, actualCityList);
        assertTrue(actualCityList.isEmpty());

        verify(cityRepository).getAllActiveCities();
    }


    @Test
    void testGetCityList2() {
        when(cityRepository.getAllActiveCities()).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getCityList());

        verify(cityRepository).getAllActiveCities();
    }

    @Test
    void testSubscribeToCity() {
        User user = User.builder()
                .authUserId(1)
                .cities(new ArrayList<>())
                .name("Name")
                .surname("Userbekov")
                .build();

        User user2 = User.builder()
                .authUserId(1)
                .cities(new ArrayList<>())
                .name("Name")
                .surname("Userbekov")
                .build();

        when(userRepository.save(any())).thenReturn(user2);
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));

        Weather weather = Weather.childBuilder()
                .condition("Runny")
                .createdAt(LocalDateTime.now())
                .createdBy(1)
                .currentTemp(10.0)
                .deleted(true)
                .id(1)
                .maxTemp(10.0)
                .minTemp(10.0)
                .updatedAt(LocalDateTime.now())
                .updatedBy(1)
                .windDirection(WindDirection.NORTH)
                .windSpeed(10.0)
                .build();

        City city = City.childBuilder()
                .country("UZ")
                .createdAt(LocalDateTime.now())
                .createdBy(1)
                .deleted(true)
                .id(1)
                .name("Name")
                .updatedAt(LocalDateTime.now())
                .updatedBy(1)
                .weather(weather)
                .build();

        when(cityRepository.findActiveCityByName(any(), any())).thenReturn(Optional.of(city));

        assertSame(city, userService.subscribeToCity(new SubscribeToCityDTO("user", "Tashkent", "UZ")));

        verify(userRepository).save(any());
        verify(userRepository).findByUsername(any());
        verify(cityRepository).findActiveCityByName(any(), any());
    }

    @Test
    void testGetSubscriptions() {
        User user = User.builder()
                .authUserId(1)
                .cities(new ArrayList<>())
                .name("Name")
                .surname("Userbekov")
                .build();

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
        assertTrue(userService.getSubscriptions("user").isEmpty());
        verify(userRepository).findByUsername(any());
    }

    @Test
    void testGetSubscriptions2() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

        User user = mock(User.class);

        when(user.getCities()).thenReturn(new ArrayList<>());

        doNothing().when(user).setAuthUserId(any());
        doNothing().when(user).setCities(any());
        doNothing().when(user).setName(any());
        doNothing().when(user).setSurname(any());

        user.setAuthUserId(1);
        user.setCities(new ArrayList<>());
        user.setName("Name");
        user.setSurname("Userbekov");

        assertThrows(RuntimeException.class, () -> userService.getSubscriptions("user"));

        verify(userRepository).findByUsername(any());
        verify(user).setAuthUserId(any());
        verify(user).setCities(any());
        verify(user).setName(any());
        verify(user).setSurname(any());
    }

    @Test
    void testEditCityOfUser() {
        when(userRepository.findAll()).thenReturn(new ArrayList<>());

        Weather weather = Weather.childBuilder()
                .condition("Runny")
                .createdAt(LocalDateTime.now())
                .createdBy(1)
                .currentTemp(10.0)
                .deleted(true)
                .id(1)
                .maxTemp(10.0)
                .minTemp(10.0)
                .updatedAt(LocalDateTime.now())
                .updatedBy(1)
                .windDirection(WindDirection.NORTH)
                .windSpeed(10.0)
                .build();

        City city = City.childBuilder()
                .country("UZ")
                .createdAt(LocalDateTime.now())
                .createdBy(1)
                .deleted(true)
                .id(1)
                .name("Name")
                .updatedAt(LocalDateTime.now())
                .updatedBy(1)
                .weather(weather)
                .build();

        userService.editCityOfUser(city);

        verify(userRepository).findAll();
        assertEquals("UZ", city.getCountry());
        assertFalse(city.isDeleted());
        assertSame(weather, city.getWeather());
        assertEquals(1, city.getUpdatedBy().intValue());
        assertEquals(1, city.getId().intValue());
        assertEquals("Name", city.getName());
        assertEquals(1, city.getCreatedBy().intValue());
    }

    @Test
    void testCreateUser() {
        User user = User.builder()
                .authUserId(1)
                .cities(new ArrayList<>())
                .name("Name")
                .surname("Userbekov")
                .build();

        when(userRepository.save(any())).thenReturn(user);
        assertSame(user, userService.createUser(1, "Name", "Userbekov"));
        verify(userRepository).save(any());
    }

}

