package alex.code.chakrayCodeTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import alex.code.chakrayCodeTest.model.User;
import alex.code.chakrayCodeTest.service.JwtService;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    private JwtService jwtService = new JwtService();

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

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(jwtService, "SECRET_KEY", "9KNAquiKwRXM2D83HnyU2mGqhqJ1Oa9vXdyw0lMwk6S");
        ReflectionTestUtils.setField(jwtService, "expirationTimeInMillis", 3600000L);
    }

    @Test
    void generateToken() {
        String token = jwtService.generateToken(USER_PREPARED);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void extractTaxId() {
        String token = jwtService.generateToken(USER_PREPARED);

        String extractedTaxId = jwtService.extractTaxId(token);

        assertEquals(USER_PREPARED.getTaxId(), extractedTaxId);
    }

    @Test
    void isTokenValid() {
        String validToken = jwtService.generateToken(USER_PREPARED);

        assertTrue(jwtService.isTokenValid(validToken));
        assertFalse(jwtService.isTokenValid("esto.no.es.un.token"));
    }
}
