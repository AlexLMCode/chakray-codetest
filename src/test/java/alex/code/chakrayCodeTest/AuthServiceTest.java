package alex.code.chakrayCodeTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import alex.code.chakrayCodeTest.dto.AuthRequestDTO;
import alex.code.chakrayCodeTest.dto.AuthResponseDTO;
import alex.code.chakrayCodeTest.model.User;
import alex.code.chakrayCodeTest.repository.UserRepository;
import alex.code.chakrayCodeTest.service.AuthService;
import alex.code.chakrayCodeTest.service.JwtService;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private StringEncryptor encryptor;

    @Mock
    private JwtService jwtService;

    UUID testId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
    ZonedDateTime testDate = ZonedDateTime.now(ZoneId.of("Indian/Antananarivo"));

    public User USER_PREPARED = User.builder()
            .id(testId)
            .name("Pedrito Perez")
            .email("pedrito@test.com")
            .phone("2721722716")
            .password("pass123")
            .taxId("BBRR990202UUU")
            .createdAt(testDate)
            .addresses(List.of())
            .build();

    @Test
    void login() {
        AuthRequestDTO authRequest = new AuthRequestDTO();
        authRequest.setPassword("pass123");
        authRequest.setUsername(USER_PREPARED.getTaxId());

        when(userRepository.findByTaxId(USER_PREPARED.getTaxId()))
                .thenReturn(Optional.of(USER_PREPARED));

        when(encryptor.decrypt(USER_PREPARED.getPassword()))
                .thenReturn("pass123");

        when(jwtService.generateToken(USER_PREPARED))
                .thenReturn("fake-jwt-token");

        AuthResponseDTO result = authService.login(authRequest);

        assertNotNull(result);
        assertEquals("fake-jwt-token", result.getJwtToken());
    }
}
