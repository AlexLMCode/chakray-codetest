package alex.code.chakrayCodeTest.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import alex.code.chakrayCodeTest.dto.UserRequestDTO;
import alex.code.chakrayCodeTest.dto.UserResponseDTO;
import alex.code.chakrayCodeTest.dto.UserUpdateDTO;
import alex.code.chakrayCodeTest.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getUsers(
            @RequestParam(required = false) String sortedBy, // ?sortedBy=email
            @RequestParam(required = false) String filter // ?filter=name+co+user
    ) {
        if (filter != null && filter.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Filter cannot be empty");
        }
        return ResponseEntity.ok(userService.getAllUsers(sortedBy, filter));
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable UUID id, @RequestBody UserUpdateDTO dto) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userService.updateUser(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponseDTO> deleteUser(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userService.deleteUser(id));
    }
}
