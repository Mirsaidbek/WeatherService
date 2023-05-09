package org.example.service;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {AdminService.class})
@ExtendWith(SpringExtension.class)
public class AdminServiceTest {


    private AdminService adminService;

    @MockBean
    private CityRepository cityRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    @MockBean
    private WeatherRepository weatherRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.adminService = new AdminService(
                cityRepository,
                weatherRepository,
                userRepository,
                userService
        );
    }

    @Test
    void testCreateCity() {

        Weather weather = Weather.childBuilder()
                .condition("Sunny")
                .createdAt(LocalDateTime.now())
                .createdBy(1)
                .currentTemp(10.0)
                .deleted(false)
                .id(1)
                .maxTemp(10.0)
                .minTemp(10.0)
                .updatedAt(LocalDateTime.now())
                .updatedBy(1)
                .windDirection(WindDirection.NORTH)
                .windSpeed(10.0)
                .build();

        City city = City.childBuilder()
                .country("GB")
                .createdAt(LocalDateTime.now())
                .createdBy(1)
                .deleted(false)
                .id(1)
                .name("London")
                .updatedAt(LocalDateTime.now().plusHours(456))
                .updatedBy(1)
                .weather(weather)
                .build();

        when(cityRepository.save(Mockito.any())).thenReturn(city);

        CreateCityDTO dto = new CreateCityDTO("Tashkent", "UZ", "Sunny", 10.0, 10.0, 10.0, 10.0, WindDirection.NORTH);

        Weather weather2 = new Weather();

        Weather.childBuilder()
                .condition("Sunny")
                .createdAt(LocalDateTime.now())
                .createdBy(1)
                .currentTemp(10.0)
                .deleted(false)
                .id(1)
                .maxTemp(10.0)
                .minTemp(10.0)
                .updatedAt(LocalDateTime.now())
                .updatedBy(1)
                .windDirection(WindDirection.NORTH)
                .windSpeed(10.0)
                .build();

        City createCityResult = adminService.createCity(dto, weather2);

        assertEquals("UZ", createCityResult.getCountry());
        assertFalse(createCityResult.isDeleted());
        assertSame(weather2, createCityResult.getWeather());
        assertNull(createCityResult.getUpdatedBy());
        assertNull(createCityResult.getCreatedAt());
        assertNull(createCityResult.getId());
        assertEquals("Tashkent", createCityResult.getName());
        assertNull(createCityResult.getCreatedBy());
        assertNull(createCityResult.getUpdatedAt());
        verify(cityRepository).save(Mockito.any());
    }

    @Test
    void testCreateCityAndWeather() {
        Weather weather = Weather.childBuilder()
                .condition("Sunny")
                .createdAt(LocalDateTime.now())
                .createdBy(1)
                .currentTemp(10.0)
                .deleted(false)
                .id(1)
                .maxTemp(10.0)
                .minTemp(10.0)
                .updatedAt(LocalDateTime.now())
                .updatedBy(1)
                .windDirection(WindDirection.NORTH)
                .build();

        City city = City.childBuilder()
                .country("UZ")
                .createdAt(LocalDateTime.now())
                .createdBy(1)
                .deleted(false)
                .id(1)
                .name("Tashkent")
                .updatedAt(LocalDateTime.now().plusHours(12))
                .updatedBy(1)
                .weather(weather)
                .build();

        Weather weather2 = Weather.childBuilder()
                .condition("Cloudy")
                .createdAt(LocalDateTime.now())
                .createdBy(1)
                .currentTemp(10.0)
                .deleted(false)
                .id(1)
                .maxTemp(10.0)
                .minTemp(10.0)
                .updatedAt(LocalDateTime.now())
                .updatedBy(1)
                .windDirection(WindDirection.NORTH)
                .windSpeed(10.0)
                .build();

        City city2 = City.childBuilder()
                .country("UZ")
                .createdAt(LocalDateTime.now())
                .createdBy(1)
                .deleted(false)
                .id(1)
                .name("Tashkent")
                .updatedAt(LocalDateTime.now().plusHours(8))
                .updatedBy(1)
                .weather(weather2)
                .build();


        when(cityRepository.save(any())).thenReturn(city);
        when(cityRepository.findByName(Mockito.any(), Mockito.any())).thenReturn(Optional.of(city2));

        assertThrows(RuntimeException.class, () -> adminService.createCityAndWeather(
                new CreateCityDTO("Tashkent", "UZ", "Sunny", 10.0d, 10.0d, 10.0d, 10.0d, WindDirection.NORTH)));
        verify(cityRepository).findByName(Mockito.any(), Mockito.any());
    }

    @Test
    void testGetUserList() {
        ArrayList<User> userList = new ArrayList<>();
        when(userRepository.findAll()).thenReturn(userList);
        List<User> actualUserList = adminService.getUserList();
        assertSame(userList, actualUserList);
        assertTrue(actualUserList.isEmpty());
        verify(userRepository).findAll();
    }

    @Test
    void testGetCityList() {
        ArrayList<City> cityList = new ArrayList<>();
        when(cityRepository.findAll()).thenReturn(cityList);
        List<City> actualCityList = adminService.getCityList();
        assertSame(cityList, actualCityList);
        assertTrue(actualCityList.isEmpty());
        verify(cityRepository).findAll();
    }

    @Test
//    @Disabled
    void testEditCity() {
        Weather weather = Weather.childBuilder()
                .condition("Rainy")
                .createdAt(LocalDateTime.now())
                .createdBy(1)
                .currentTemp(18.0)
                .deleted(false)
                .id(1)
                .maxTemp(16.3)
                .minTemp(5.4)
                .updatedAt(LocalDateTime.now())
                .updatedBy(1)
                .windDirection(WindDirection.EAST)
                .windSpeed(1.5)
                .build();

        City city = City.childBuilder()
                .country("UZ")
                .createdAt(LocalDateTime.now())
                .createdBy(1)
                .deleted(false)
                .id(1)
                .name("Samarkand")
                .updatedAt(LocalDateTime.now().plusHours(56))
                .updatedBy(1)
                .weather(weather)
                .build();

        Optional<City> result1 = Optional.of(city);

        Weather weather2 = Weather.childBuilder()
                .condition("Rainy")
                .createdAt(LocalDateTime.now())
                .createdBy(1)
                .currentTemp(36.5)
                .deleted(true)
                .id(1)
                .maxTemp(65.0)
                .minTemp(2.02)
                .updatedAt(LocalDateTime.now().plusHours(55))
                .updatedBy(1)
                .windDirection(WindDirection.NORTH)
                .windSpeed(2.2)
                .build();

        City city2 = City.childBuilder()
                .country("UZ")
                .createdAt(LocalDateTime.now())
                .createdBy(1)
                .deleted(true)
                .id(1)
                .name("Termiz")
                .updatedAt(LocalDateTime.now().plusHours(32))
                .updatedBy(1)
                .weather(weather2)
                .build();

        when(cityRepository.save(Mockito.any())).thenReturn(city2);
        when(cityRepository.findByName(Mockito.any(), Mockito.any())).thenReturn(result1);

        doNothing().when(userService).editCityOfUser(Mockito.any());
        assertEquals("City updated", adminService.editCity(new UpdateCityDTO("Termiz", "UZ", true)));

        verify(cityRepository).save(Mockito.any());
        verify(cityRepository).findByName(Mockito.any(), Mockito.any());
        verify(userService).editCityOfUser(Mockito.any());
    }

    @Test
    void testEditCityWeather() {
        Weather weather = Weather.childBuilder()
                .condition("Cloudy")
                .createdAt(LocalDateTime.now())
                .createdBy(1)
                .currentTemp(33.5)
                .deleted(false)
                .id(1)
                .maxTemp(35.6)
                .minTemp(27.0)
                .updatedAt(null)
                .updatedBy(1)
                .windDirection(WindDirection.NORTH)
                .windSpeed(10.0)
                .build();

        City city = City.childBuilder()
                .country("UZ")
                .createdAt(LocalDateTime.now())
                .createdBy(1)
                .deleted(false)
                .id(1)
                .name("Tashkent")
                .updatedAt(LocalDateTime.now().plusHours(3))
                .updatedBy(1)
                .weather(weather)
                .build();

        Optional<City> result = Optional.of(city);

        when(cityRepository.updateCityWeather(Mockito.any(), Mockito.<Integer>any())).thenReturn(1);
        when(cityRepository.findByName(Mockito.any(), Mockito.any())).thenReturn(result);

        Weather editedWeather = adminService.editCityWeather(
                new UpdateWeatherDTO("Tashkent", "UZ", "Sunny", 10.0, 10.0, 10.0, 10.0, WindDirection.NORTH_WEST));


        assertFalse(editedWeather.isDeleted());

        assertEquals("Sunny", editedWeather.getCondition());
        assertEquals(10.0d, editedWeather.getWindSpeed().doubleValue());
        assertEquals(WindDirection.NORTH_WEST, editedWeather.getWindDirection());

        assertNull(editedWeather.getUpdatedBy());
        assertNull(editedWeather.getUpdatedAt());

        assertEquals(10.0d, editedWeather.getMinTemp().doubleValue());
        assertEquals(10.0d, editedWeather.getMaxTemp().doubleValue());

        assertNull(editedWeather.getId());

        assertEquals(10.0d, editedWeather.getCurrentTemp().doubleValue());

        assertNull(editedWeather.getCreatedBy());
        assertNull(editedWeather.getCreatedAt());

        verify(cityRepository).updateCityWeather(Mockito.any(), Mockito.<Integer>any());
        verify(cityRepository).findByName(Mockito.any(), Mockito.any());
    }

}

