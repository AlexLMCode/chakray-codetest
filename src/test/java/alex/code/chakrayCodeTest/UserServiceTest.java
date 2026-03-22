package alex.code.chakrayCodeTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import alex.code.chakrayCodeTest.dto.AddressRequestDTO;
import alex.code.chakrayCodeTest.dto.UserRequestDTO;
import alex.code.chakrayCodeTest.dto.UserResponseDTO;
import alex.code.chakrayCodeTest.dto.UserUpdateDTO;
import alex.code.chakrayCodeTest.model.Address;
import alex.code.chakrayCodeTest.model.User;
import alex.code.chakrayCodeTest.repository.UserRepository;
import alex.code.chakrayCodeTest.service.UserService;

import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private StringEncryptor encryptor;

    @InjectMocks
    private UserService userService;

    UUID testId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
    ZonedDateTime testDate = ZonedDateTime.now(ZoneId.of("Indian/Antananarivo"));
    Address address = Address.builder()
            .id(1L)
            .name("Home")
            .street("123 Main St")
            .countryCode("US")
            .build();

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

    public UserUpdateDTO USER_UPDATE_PREPARED = UserUpdateDTO.builder()
            .name("Juanito Perez")
            .email("Juanito@test.com")
            .phone("2292241062")
            .build();

    public User USER_MODIFIED_PREPARED = User.builder()
            .id(testId)
            .name("Juanito Perez")
            .email("Juanito@test.com")
            .phone("2292241062")
            .password("pass123")
            .taxId("BBRR990202UUU")
            .createdAt(testDate)
            .addresses(List.of())
            .build();

    @Test
    void updateUser() {
        when(userRepository.findById(testId)).thenReturn(Optional.of(USER_PREPARED));
        when(userRepository.save(any(User.class))).thenReturn(USER_MODIFIED_PREPARED);

        userService.updateUser(testId, USER_UPDATE_PREPARED);

        assertEquals("Juanito Perez", USER_MODIFIED_PREPARED.getName());
    }

    @Test
    void getAllUsers() {
        List<User> usersMock = new ArrayList<>(List.of(USER_PREPARED));

        when(userRepository.findAll(any(Specification.class), any(Sort.class))).thenReturn(usersMock);
        List<UserResponseDTO> users = userService.getAllUsers("", "");

        assertThat(users).hasSize(1);
        assertThat(users.get(0).getName()).isEqualTo("Pedrito Perez");
        assertThat(users).isNotEmpty();
    }

    @Test
    void createUser() {
        UserRequestDTO request = UserRequestDTO.builder()
                .email(USER_PREPARED.getEmail())
                .name(USER_PREPARED.getName())
                .phone(USER_PREPARED.getPhone())
                .password("pass1234")
                .tax_id(USER_PREPARED.getTaxId())
                .addresses(List.of(
                        AddressRequestDTO.builder()
                                .name("Home")
                                .street("123 Main St")
                                .countryCode("US")
                                .build()))
                .build();

        when(encryptor.encrypt(any())).thenReturn("encryptedPass");
        when(userRepository.save(any(User.class))).thenReturn(USER_PREPARED);

        UserResponseDTO result = userService.createUser(request);

        assertThat(result.getEmail()).isEqualTo(USER_PREPARED.getEmail());
        assertThat(result.getName()).isEqualTo(USER_PREPARED.getName());
    }

    @Test
    void deleteUser() {
        when(userRepository.findById(testId)).thenReturn(Optional.of(USER_PREPARED));

        userService.deleteUser(testId);

        verify(userRepository, times(1)).delete(USER_PREPARED);
    }
}
