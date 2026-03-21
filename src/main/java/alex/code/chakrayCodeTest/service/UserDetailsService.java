package alex.code.chakrayCodeTest.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import alex.code.chakrayCodeTest.model.User;
import alex.code.chakrayCodeTest.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String taxId) throws UsernameNotFoundException {
        User user = userRepository.findByTaxId(taxId)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + taxId));

        return org.springframework.security.core.userdetails.User.builder()
            .username(user.getTaxId())
            .password(user.getPassword())
            .roles("USER")
            .build();
    }
}
