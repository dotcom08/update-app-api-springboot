package com.example.app.services;

import com.example.app.dtos.LoginRequest;
import com.example.app.dtos.LoginResponse;
import com.example.app.dtos.RegisterRequest;
import com.example.app.dtos.UserLoggedDto;
import com.example.app.jwt.CookieUtil;
import com.example.app.jwt.JwtTokenProvider;
import com.example.app.mappers.UserMapper;
import com.example.app.models.Role;
import com.example.app.models.Token;
import com.example.app.models.User;
import com.example.app.repositories.RoleRepository;
import com.example.app.repositories.TokenRepository;
import com.example.app.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class AuthService {


    @Value("${jwt.access.duration.second}")
    private long accessTokenDurationSecond;
    @Value("${jwt.access.duration.minute}")
    private long accessTokenDurationMinute;

    @Value("${jwt.refresh.duration.second}")
    private long refreshTokenDurationSecond;
    @Value("${jwt.refresh.duration.day}")
    private long refreshTokenDurationDay;

    private final UserService userService;
    private final TokenRepository tokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieUtil cookieUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    private void addAccessTokenCookie(HttpHeaders headers, Token token) {
        headers.add(HttpHeaders.SET_COOKIE,
                cookieUtil.createAccessTokenCookie(token.getValue(), accessTokenDurationSecond).toString());
    }

    private void addRefreshTokenCookie(HttpHeaders headers, Token token) {
        headers.add(HttpHeaders.SET_COOKIE,
                cookieUtil.createRefreshTokenCookie(token.getValue(), refreshTokenDurationSecond).toString()); // Было
        // accessTokenDurationSecond
    }

    private void revokeAllTokensOfUser(User user) {
        user.getTokens().forEach(t -> {
            if (t.getExpiryDate().isBefore(LocalDateTime.now())) {
                tokenRepository.delete(t);
            } else if (t.isDisabled()) {
                t.setDisabled(true);
                tokenRepository.save(t);
            }
        });
    }

    @Transactional
    public ResponseEntity<LoginResponse> register(RegisterRequest registerRequest) {

        if (userService.existsByUsername(registerRequest.username())) {
            throw new RuntimeException("Username already exists");
        }



        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Default role USER not found"));


        User user = new User();
        user.setUsername(registerRequest.username());
        user.setPassword(passwordEncoder.encode(registerRequest.password()));
        user.setRole(userRole);

        User savedUser = userRepository.save(user);

        HttpHeaders headers = new HttpHeaders();

        Token accessToken = jwtTokenProvider.generateAccessToken(
                Map.of("role", savedUser.getRole().getAuthority()),
                accessTokenDurationMinute, ChronoUnit.MINUTES, savedUser);
        accessToken.setUser(savedUser);

        Token refreshToken = jwtTokenProvider.generateRefreshToken(
                refreshTokenDurationDay, ChronoUnit.DAYS, savedUser);
        refreshToken.setUser(savedUser);

        tokenRepository.save(refreshToken);

        addAccessTokenCookie(headers, accessToken);
        addRefreshTokenCookie(headers, refreshToken);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                savedUser, null, savedUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return ResponseEntity.status(201)
                .headers(headers)
                .body(new LoginResponse(true, savedUser.getRole().getName()));
    }

    public ResponseEntity<LoginResponse> login(
            LoginRequest loginRequest,
            String access,
            String refresh) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));
        String username = loginRequest.username();
        User user = userService.getByUsername(username);
        boolean accessTokenValid = jwtTokenProvider.validateToken(access);
        boolean refreshTokenValid = jwtTokenProvider.validateToken(refresh);
        HttpHeaders headers = new HttpHeaders();
        Token newAccess, newRefresh;
        revokeAllTokensOfUser(user);
        if (!accessTokenValid) {
            newAccess = jwtTokenProvider.generateAccessToken(
                    Map.of("role", user.getRole().getAuthority()),
                    accessTokenDurationMinute, ChronoUnit.MINUTES, user);
            newAccess.setUser(user);
            addAccessTokenCookie(headers, newAccess);
        }
        if (!refreshTokenValid || accessTokenValid) {
            newRefresh = jwtTokenProvider.generateRefreshToken(refreshTokenDurationDay, ChronoUnit.DAYS, user);
            newRefresh.setUser(user);
            addRefreshTokenCookie(headers, newRefresh);
            tokenRepository.save(newRefresh);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return ResponseEntity.ok().headers(headers).body(new LoginResponse(true, user.getRole().getName()));
    }


    public ResponseEntity<LoginResponse> refresh(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken))
            throw new RuntimeException("Invalid refresh token");
        User user = userService.getByUsername(jwtTokenProvider.getUsername(refreshToken));
        HttpHeaders headers = new HttpHeaders();
        Token newAccess = jwtTokenProvider.generateAccessToken(Map.of("role", user.getRole().getAuthority()),
                accessTokenDurationMinute, ChronoUnit.MINUTES, user);
        addAccessTokenCookie(headers, newAccess);
        return ResponseEntity.ok().headers(headers).body(new LoginResponse(true, user.getRole().getName()));
    }

    public ResponseEntity<LoginResponse> logout(String accessToken) {
        SecurityContextHolder.clearContext();
        User user = userService.getByUsername(jwtTokenProvider.getUsername(accessToken));
        revokeAllTokensOfUser(user);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE,
                cookieUtil.deleteAccessTokenCookie().toString());
        headers.add(HttpHeaders.SET_COOKIE,
                cookieUtil.deleteRefreshTokenCookie().toString());
        return ResponseEntity.ok().headers(headers).body(new LoginResponse(false, null));

    }

    public UserLoggedDto getInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication instanceof AnonymousAuthenticationToken) {
            throw new RuntimeException("anonymous user");
        }
        User user = userService.getByUsername(authentication.getName());
        return UserMapper.userToUserLoggedDto(user);
    }

}
