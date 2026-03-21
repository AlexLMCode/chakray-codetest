package alex.code.chakrayCodeTest.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.jasypt.encryption.StringEncryptor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import alex.code.chakrayCodeTest.dto.UserRequestDTO;
import alex.code.chakrayCodeTest.dto.UserResponseDTO;
import alex.code.chakrayCodeTest.dto.UserUpdateDTO;
import alex.code.chakrayCodeTest.model.Address;
import alex.code.chakrayCodeTest.model.User;
import alex.code.chakrayCodeTest.repository.UserRepository;
import alex.code.chakrayCodeTest.specification.UserSpecification;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final StringEncryptor encryptor;

    public List<UserResponseDTO> getAllUsers(String sortedBy, String filter) {
        Sort sort = (sortedBy != null && !sortedBy.isBlank()) ? Sort.by(snakeToCamel(sortedBy)) : Sort.unsorted();

        Specification<User> spec = UserSpecification.buildFilter(filter);

        return userRepository.findAll(spec, sort).stream().map(this::toDTO).collect(Collectors.toList());
    }

    private UserResponseDTO toDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setPhone(user.getPhone());
        dto.setTax_id(user.getTaxId().toUpperCase());
        dto.setCreatedAt(user.getCreatedAt());

        List<UserResponseDTO.AddressDTO> addressDTOs = user.getAddresses()
                .stream()
                .map(a -> {
                    UserResponseDTO.AddressDTO adto = new UserResponseDTO.AddressDTO();
                    adto.setId(a.getId());
                    adto.setName(a.getName());
                    adto.setStreet(a.getStreet());
                    adto.setCountryCode(a.getCountryCode());
                    return adto;
                }).collect(Collectors.toList());

        dto.setAddresses(addressDTOs);
        return dto;
    }

    public UserResponseDTO createUser(UserRequestDTO dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setPhone(dto.getPhone());
        user.setPassword(encryptor.encrypt(dto.getPassword()));
        user.setTaxId(dto.getTax_id().toUpperCase());

        if (dto.getAddresses() != null) {
            List<Address> addresses = dto.getAddresses()
                    .stream()
                    .map(addrDto -> {
                        Address address = new Address();
                        address.setName(addrDto.getName());
                        address.setStreet(addrDto.getStreet());
                        address.setCountryCode(addrDto.getCountryCode());
                        address.setUser(user);
                        return address;
                    }).collect(Collectors.toList());

            user.setAddresses(addresses);
        }

        User savedUser = userRepository.save(user);

        return toDTO(savedUser);
    }

    public UserResponseDTO updateUser(UUID id, UserUpdateDTO dto) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (dto.getName() != null) {
            existingUser.setName(dto.getName());
        }
        if (dto.getEmail() != null) {
            existingUser.setEmail(dto.getEmail());
        }
        if (dto.getPhone() != null) {
            existingUser.setPhone(dto.getPhone());
        }
        if (dto.getAddresses() != null) {
            List<Address> newAddresses = dto.getAddresses().stream()
                    .map(addrDto -> {
                        Address address = new Address();
                        address.setName(addrDto.getName());
                        address.setStreet(addrDto.getStreet());
                        address.setCountryCode(addrDto.getCountryCode());
                        address.setUser(existingUser);
                        return address;
                    }).collect(Collectors.toList());
            existingUser.getAddresses().addAll(newAddresses);
        }

        User updatedUser = userRepository.save(existingUser);

        return toDTO(updatedUser);

    }

    public UserResponseDTO deleteUser(UUID id) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        userRepository.delete(existingUser);

        return toDTO(existingUser);
    }

    private String snakeToCamel(String snake) {
        String[] words = snake.split("_");
        StringBuilder sb = new StringBuilder(words[0]);
        for (int i = 1; i < words.length; i++) {
            sb.append(Character.toUpperCase(words[i].charAt(0)));
            sb.append(words[i].substring(1));
        }
        return sb.toString();
    }
}
