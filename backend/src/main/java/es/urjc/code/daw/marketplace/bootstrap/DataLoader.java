package es.urjc.code.daw.marketplace.bootstrap;

import es.urjc.code.daw.marketplace.domain.Product;
import es.urjc.code.daw.marketplace.domain.Role;
import es.urjc.code.daw.marketplace.domain.User;
import es.urjc.code.daw.marketplace.repository.ProductRepository;
import es.urjc.code.daw.marketplace.repository.RoleRepository;
import es.urjc.code.daw.marketplace.repository.UserRepository;
import es.urjc.code.daw.marketplace.security.SecurityProperties;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner {

    private static final String[] roles = { "ROLE_CLIENT", "ROLE_ADMIN" };

    private final SecurityProperties securityProperties;
    private final RoleRepository authorityRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(SecurityProperties securityProperties,
                      RoleRepository authorityRepository,
                      UserRepository userRepository,
                      ProductRepository productRepository,
                      PasswordEncoder passwordEncoder) {
        this.securityProperties = securityProperties;
        this.authorityRepository = authorityRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        Arrays.stream(roles).forEach(role -> authorityRepository.save(Role.builder().name(role).build()));

        final String adminEmail = securityProperties.getAdminEmail();
        final String adminPassword = securityProperties.getAdminPassword();
        final Role defaultRole = authorityRepository.findByName("ROLE_CLIENT");
        final Role adminRole = authorityRepository.findByName("ROLE_ADMIN");

        for(int i = 0; i<25; i++) {
            User random = User.builder()
                    .firstName(RandomStringUtils.randomAlphabetic(10))
                    .surname(RandomStringUtils.randomAlphabetic(10))
                    .email(RandomStringUtils.randomAlphabetic(5) + "@" + RandomStringUtils.randomAlphabetic(5) + ".ru")
                    .password(passwordEncoder.encode("FUCK"))
                    .isEnabled(RandomUtils.nextInt(0, 2) == 0)
                    .role(RandomUtils.nextInt(0, 2) == 0 ? defaultRole : adminRole)
                .build();
            userRepository.save(random);
        }


        User user = User.builder()
                .firstName("Default")
                .surname("Administrator")
                .email(adminEmail)
                .password(passwordEncoder.encode(adminPassword))
                .role(adminRole)
            .build();

        userRepository.save(user);


    }

}
