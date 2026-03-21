package alex.code.chakrayCodeTest.dto;

import java.util.List;

import lombok.Data;

@Data
public class UserUpdateDTO {
    private String email;
    private String name;
    private String phone;
    private List<AddressDTO> addresses;

    @Data
    public static class AddressDTO {
        private String name;
        private String street;
        private String countryCode;
    }
}
