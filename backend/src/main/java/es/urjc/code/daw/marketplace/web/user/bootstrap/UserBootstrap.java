package es.urjc.code.daw.marketplace.web.user.bootstrap;

import com.github.javafaker.Faker;
import es.urjc.code.daw.marketplace.domain.Product;
import es.urjc.code.daw.marketplace.domain.Role;
import es.urjc.code.daw.marketplace.domain.User;
import es.urjc.code.daw.marketplace.repository.ProductRepository;
import es.urjc.code.daw.marketplace.repository.RoleRepository;
import es.urjc.code.daw.marketplace.repository.UserRepository;
import es.urjc.code.daw.marketplace.security.SecurityProperties;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;

@Component
@org.springframework.core.annotation.Order(1)
public class UserBootstrap implements CommandLineRunner {

    private static final String[] roles = { "ROLE_CLIENT", "ROLE_ADMIN" };

    private final SecurityProperties securityProperties;
    private final RoleRepository authorityRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserBootstrap(SecurityProperties securityProperties,
                         RoleRepository authorityRepository,
                         UserRepository userRepository,
                         PasswordEncoder passwordEncoder) {
        this.securityProperties = securityProperties;
        this.authorityRepository = authorityRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        Arrays.stream(roles).forEach(role -> authorityRepository.save(Role.builder().name(role).build()));

        final Role defaultRole = authorityRepository.findByName("ROLE_CLIENT");
        final Role adminRole = authorityRepository.findByName("ROLE_ADMIN");

        for(int i = 0; i<25; i++) {
            Faker faker = new Faker();

            User random = User.builder()
                    .firstName(faker.name().firstName())
                    .surname(faker.name().lastName())
                    .email(faker.internet().emailAddress())
                    .password(passwordEncoder.encode("password"))
                    .role(defaultRole)
                    .address("Main St. 123, New York")
                .build();

            userRepository.saveAndFlush(random);
        }

        User admin = User.builder()
                .firstName("Administrator")
                .surname(StringUtils.EMPTY)
                .email(securityProperties.getAdminEmail())
                .password(securityProperties.getAdminPassword())
                .role(adminRole)
                .address("Main St. 123, New York")
            .build();

        userRepository.saveAndFlush(admin);

        User client = User.builder()
                .firstName("Peter")
                .surname("Moon")
                .email(securityProperties.getClientEmail())
                .password(securityProperties.getClientPassword())
                .role(defaultRole)
                .address("Main St. 123, New York")
            .build();

        userRepository.saveAndFlush(client);

    }

}
