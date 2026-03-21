package alex.code.chakrayCodeTest.service;

import org.jasypt.encryption.StringEncryptor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import alex.code.chakrayCodeTest.dto.AuthRequestDTO;
import alex.code.chakrayCodeTest.dto.AuthResponseDTO;
import alex.code.chakrayCodeTest.model.User;
import alex.code.chakrayCodeTest.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final StringEncryptor encryptor;
    private final JwtService jwtService;

    public AuthResponseDTO login(AuthRequestDTO dto) {
        User user = userRepository.findByTaxId(dto.getUsername())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        String decryptedPassword = encryptor.decrypt(user.getPassword());
        if (!decryptedPassword.equals(dto.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        AuthResponseDTO response = new AuthResponseDTO();
        response.setJwtToken(jwtService.generateToken(user));
        return response;
    }
}
