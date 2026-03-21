package alex.code.chakrayCodeTest.dto;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequestDTO {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Name is required")
    private String name;

    @Pattern(regexp = "^\\+?[\\d\\s]{10,20}$", message = "Invalid phone format")
    private String phone;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotBlank(message = "Tax ID is required")
    @Pattern(regexp = "^[A-ZÑ&a-zñ]{4}\\d{6}[A-Z0-9a-z]{3}$", message = "Invalid RFC format. Expected: AARR990101XXX")
    private String tax_id;

    @NotNull(message = "Addresses cannot be null")
    @NotEmpty(message = "At least one address is required")
    private List<AddressRequestDTO> addresses;
}
