package prince.aktiveskochbuch.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import prince.aktiveskochbuch.adapter.db.ConfirmationRepository;
import prince.aktiveskochbuch.adapter.db.TokenRepository;
import prince.aktiveskochbuch.adapter.db.UserRepository;
import prince.aktiveskochbuch.domain.dtos.AuthenticationRequest;
import prince.aktiveskochbuch.domain.dtos.AuthenticationResponse;
import prince.aktiveskochbuch.domain.dtos.RegisterRequest;
import prince.aktiveskochbuch.application.config.JwtService;
import prince.aktiveskochbuch.application.exceptions.EMailSend;
import prince.aktiveskochbuch.application.exceptions.UserAlreadyExists;
import prince.aktiveskochbuch.domain.models.Confirmation;
import prince.aktiveskochbuch.domain.models.Token;
import prince.aktiveskochbuch.domain.models.TokenType;
import prince.aktiveskochbuch.domain.models.User;
import prince.aktiveskochbuch.domain.usecases.SendEmailUseCase;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService {
    private final UserRepository repository;
    private final ConfirmationRepository confirmationRepository;
    private final SendEmailUseCase emailService;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        validateUserDoesNotExist(user);
        var savedUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        user.setEnabled(true);
        createConfirmation(user);
        sendConfirmationEmail(user);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailIgnoreCase(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .tokenKey(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(Math.toIntExact(user.getId()));
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.repository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    private void validateUserDoesNotExist(User user) {
        if (existsByMail(user).equals(true)) {
            throw new UserAlreadyExists("Email already exists");
        }
    }

    private Boolean existsByMail(User user) {
        return userRepository.existsByEmail(user.getEmail());
    }

    private void createConfirmation(User user) {

        Confirmation confirmation = new Confirmation(user);
        confirmationRepository.save(confirmation);
    }

    private void sendConfirmationEmail(User user) throws EMailSend {
        emailService.sendSimpleMessage(user.getName(), user.getEmail(), generateConfirmationToken(user));
    }

    private String generateConfirmationToken(User user) {
        return confirmationRepository.findByUser(user).getToken();
    }
}
