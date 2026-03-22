package alex.code.chakrayCodeTest.config;

import java.util.List;

import org.jasypt.encryption.StringEncryptor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import alex.code.chakrayCodeTest.model.Address;
import alex.code.chakrayCodeTest.model.User;
import alex.code.chakrayCodeTest.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final StringEncryptor encryptor;

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.count() > 0) return;

        User user1 = User.builder()
                .email("user1@mail.com")
                .name("user1")
                .phone("+1 55 555 555 55")
                .password(encryptor.encrypt("123456"))
                .taxId("AARR990101XXX")
                .build();

        Address addr1 = Address.builder().name("workaddress").street("street No. 1").countryCode("UK").user(user1).build();
        Address addr2 = Address.builder().name("homeaddress").street("street No. 2").countryCode("AU").user(user1).build();
        user1.setAddresses(List.of(addr1, addr2));

        User user2 = User.builder()
                .email("user2@mail.com")
                .name("user2")
                .phone("+1 55 666 666 66")
                .password(encryptor.encrypt("123456"))
                .taxId("BBRR990202YYY")
                .build();

        Address addr3 = Address.builder().name("workaddress").street("street No. 3").countryCode("MX").user(user2).build();
        user2.setAddresses(List.of(addr3));

        userRepository.saveAll(List.of(user1, user2));
    }
}
