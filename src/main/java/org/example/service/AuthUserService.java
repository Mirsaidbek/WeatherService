package org.example.service;


import lombok.RequiredArgsConstructor;
import org.example.config.jwt.JwtUtils;
import org.example.domain.AuthUser;
import org.example.dto.token.RefreshTokenRequest;
import org.example.dto.token.TokenRequest;
import org.example.dto.token.TokenResponse;
import org.example.dto.user.CreateUserDTO;
import org.example.dto.user.UpdateUserDTO;
import org.example.enums.Role;
import org.example.repository.AuthUserRepository;
import org.example.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import static org.example.enums.TokenType.REFRESH;

@Service
@Component
@RequiredArgsConstructor
public class AuthUserService {

    private final AuthUserRepository authUserRepository;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    public void register(CreateUserDTO dto) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new RuntimeException("Passwords are not equal");
        }
        AuthUser authUser = AuthUser.childBuilder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .active(true)
                .role(Role.USER)
                .build();

        authUserRepository.save(authUser);

        System.out.println("save.getId() = " + authUser.getId());
        System.out.println("save.getUsername() = " + authUser.getUsername());
        System.out.println(authUser);
        AuthUser authUser1 = authUserRepository.save(authUser);
        userService.createUser(authUser1.getId(), dto.getName(), dto.getSurname());
    }


    public TokenResponse login(String username, String password) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password
                )
        );

        AuthUser user = authUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found "));

        TokenResponse tokenResponse = jwtUtils.generateToken(user.getUsername(), user.getRole());

        return TokenResponse.builder()
                .accessToken(tokenResponse.getAccessToken())
                .accessTokenExpiry(tokenResponse.getAccessTokenExpiry())
                .refreshToken(tokenResponse.getRefreshToken())
                .refreshTokenExpiry(tokenResponse.getRefreshTokenExpiry())
                .build();
    }

    public TokenResponse generateToken(TokenRequest tokenRequest) {
        String username = tokenRequest.getUsername();
        String password = tokenRequest.getPassword();
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(username, password);
        authenticationManager.authenticate(authentication);
        AuthUser user = authUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found "));
        Role role = user.getRole();
        return jwtUtils.generateToken(username, role);
    }

    public TokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        if (!jwtUtils.isValid(refreshToken, REFRESH))
            throw new CredentialsExpiredException("Token is invalid");
        String username = jwtUtils.getUsername(refreshToken, REFRESH);
        AuthUser authUser = authUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found "));
        Role role = authUser.getRole();

        TokenResponse tokenResponse = TokenResponse.builder()
                .refreshToken(refreshToken)
                .refreshTokenExpiry(jwtUtils.getExpiry(refreshToken, REFRESH))
                .build();
        return jwtUtils.generateAccessToken(username, tokenResponse, role);
    }

    public void editUser(UpdateUserDTO dto) {
        AuthUser authUser = authUserRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("User with this username not found"));
        authUserRepository.updateAuthUser(dto.getUsername(), dto.getRole(), dto.isActive());
    }
}
