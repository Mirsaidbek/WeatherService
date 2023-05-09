package org.example.service;

import org.example.config.jwt.JwtUtils;
import org.example.domain.AuthUser;
import org.example.domain.User;
import org.example.dto.token.RefreshTokenRequest;
import org.example.dto.token.TokenRequest;
import org.example.dto.token.TokenResponse;
import org.example.dto.user.CreateUserDTO;
import org.example.dto.user.UpdateUserDTO;
import org.example.enums.Role;
import org.example.repository.AuthUserRepository;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {AuthUserService.class})
@ExtendWith(SpringExtension.class)
class AuthUserServiceTest {
    @MockBean
    private AuthUserRepository authUserRepository;

    @Autowired
    private AuthUserService authUserService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;


    @Test
    void testRegister() {
        AuthUser authUser = AuthUser.childBuilder()
                .active(true)
                .createdAt(LocalDateTime.now())
                .createdBy(1)
                .deleted(true)
                .id(1)
                .password("123")
                .role(Role.USER)
                .updatedAt(null)
                .updatedBy(1)
                .username("user")
                .build();

        when(authUserRepository.save(any())).thenReturn(authUser);

        User user = User.builder()
                .authUserId(1)
                .cities(new ArrayList<>())
                .name("Userbek")
                .surname("Userbekov")
                .build();

        when(userService.createUser(any(), any(), any())).thenReturn(user);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");

        authUserService.register(new CreateUserDTO("user", "123", "123", "Userbek", "Userbekov"));

        verify(authUserRepository, atLeast(1)).save(any());
        verify(userService).createUser(any(), any(), any());
        verify(passwordEncoder).encode(any());
    }


    @Test
    void testLogin() throws AuthenticationException {
        AuthUser authUser = AuthUser.childBuilder()
                .active(true)
                .createdAt(LocalDateTime.now())
                .createdBy(1)
                .deleted(true)
                .id(1)
                .password("123")
                .role(Role.USER)
                .updatedAt(LocalDateTime.now())
                .updatedBy(1)
                .username("user")
                .build();

        when(authUserRepository.findByUsername(any())).thenReturn(Optional.of(authUser));
        when(jwtUtils.generateToken(any(), any())).thenReturn(new TokenResponse());

        TokenResponse actualLoginResult = authUserService.login("user", "123");

        assertNull(actualLoginResult.getAccessToken());
        assertNull(actualLoginResult.getRefreshTokenExpiry());
        assertNull(actualLoginResult.getRefreshToken());
        assertNull(actualLoginResult.getAccessTokenExpiry());

        verify(authUserRepository).findByUsername(any());
        verify(jwtUtils).generateToken(any(), any());
        verify(authenticationManager).authenticate(any());
    }

    @Test
    void testGenerateToken() throws AuthenticationException {
        AuthUser authUser = AuthUser.childBuilder()
                .active(true)
                .createdAt(LocalDateTime.now())
                .createdBy(1)
                .deleted(true)
                .id(1)
                .password("123")
                .role(Role.USER)
                .updatedAt(LocalDateTime.now().plusHours(6))
                .updatedBy(1)
                .username("user")
                .build();

        when(authUserRepository.findByUsername(any())).thenReturn(Optional.of(authUser));

        TokenResponse tokenResponse = new TokenResponse();

        when(jwtUtils.generateToken(any(), any())).thenReturn(tokenResponse);

        assertSame(tokenResponse, authUserService.generateToken(new TokenRequest("user", "123")));

        verify(authUserRepository).findByUsername(any());
        verify(jwtUtils).generateToken(any(), any());
        verify(authenticationManager).authenticate(any());
    }

    @Test
    void testRefreshToken() {
        AuthUser authUser = AuthUser.childBuilder()
                .active(true)
                .createdAt(LocalDateTime.now())
                .createdBy(1)
                .deleted(true)
                .id(1)
                .password("123")
                .role(Role.USER)
                .updatedAt(LocalDateTime.now().plusHours(2))
                .updatedBy(1)
                .username("user")
                .build();


        when(authUserRepository.findByUsername(any())).thenReturn(Optional.of(authUser));
        when(jwtUtils.getUsername(any(), any())).thenReturn("user");

        TokenResponse tokenResponse = new TokenResponse();

        when(jwtUtils.generateAccessToken(any(), any(), any())).thenReturn(tokenResponse);
        when(jwtUtils.isValid(any(), any())).thenReturn(true);

        assertSame(tokenResponse, authUserService.refreshToken(new RefreshTokenRequest("ABC123")));

        verify(authUserRepository).findByUsername(any());
        verify(jwtUtils).isValid(any(), any());
        verify(jwtUtils).getUsername(any(), any());
        verify(jwtUtils).getExpiry(any(), any());
        verify(jwtUtils).generateAccessToken(any(), any(), any());
    }


    @Test
    void testEditUser() {
        AuthUser authUser = AuthUser.childBuilder()
                .active(true)
                .createdAt(LocalDateTime.now())
                .createdBy(1)
                .deleted(true)
                .id(1)
                .password("123")
                .role(Role.USER)
                .updatedAt(LocalDateTime.now().plusHours(2))
                .updatedBy(1)
                .username("user")
                .build();

        when(authUserRepository.findByUsername(any())).thenReturn(Optional.of(authUser));

        UpdateUserDTO dto = new UpdateUserDTO("user", Role.USER, true);

        authUserService.editUser(dto);

        verify(authUserRepository).findByUsername(any());
        verify(authUserRepository).updateAuthUser(any(), any(), anyBoolean());

        assertEquals(Role.USER, dto.getRole());
        assertTrue(dto.isActive());
        assertEquals("user", dto.getUsername());
    }

    @Test
    void testEditUser2() {
        doNothing().when(authUserRepository).updateAuthUser(any(), any(), anyBoolean());
        when(authUserRepository.findByUsername(any())).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class,
                () -> authUserService.editUser(new UpdateUserDTO("user", Role.USER, true)));
        verify(authUserRepository).findByUsername(any());
    }

}

