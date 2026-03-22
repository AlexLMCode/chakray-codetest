package alex.code.chakrayCodeTest.dto;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDTO {
    private UUID id;
    private String email;
    private String name;
    private String phone;
    private String tax_id;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private ZonedDateTime createdAt;

    private List<AddressDTO> addresses;

    @Data
    public static class AddressDTO {
        private Long id;
        private String name;
        private String street;
        private String countryCode;
    }
}
