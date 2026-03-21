package alex.code.chakrayCodeTest.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class UserResponseDTO {
    private UUID id;
    private String email;
    private String name;
    private String phone;
    private String password;
    private String tax_id;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    private List<AddressDTO> addresses;

    @Data
    public static class AddressDTO {
        private Long id;
        private String name;
        private String street;
        private String countryCode;
    }
}
