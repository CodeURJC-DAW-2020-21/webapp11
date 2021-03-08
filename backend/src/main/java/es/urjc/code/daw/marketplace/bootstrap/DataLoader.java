package es.urjc.code.daw.marketplace.bootstrap;

import es.urjc.code.daw.marketplace.domain.Product;
import es.urjc.code.daw.marketplace.domain.Role;
import es.urjc.code.daw.marketplace.domain.User;
import es.urjc.code.daw.marketplace.repository.ProductRepository;
import es.urjc.code.daw.marketplace.repository.RoleRepository;
import es.urjc.code.daw.marketplace.repository.UserRepository;
import es.urjc.code.daw.marketplace.security.SecurityProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner {

    private static final String[] roles = { "CLIENT", "ADMIN" };

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
        final Role defaultRole = authorityRepository.findByName("ADMIN");

        User user = User.builder()
                .email(adminEmail)
                .password(passwordEncoder.encode(adminPassword))
                .role(defaultRole)
                .build();

        userRepository.save(user);

        Product productOne = Product.builder()
                .category("shared")
                .price(5)
                .ram("1 GB")
                .cores("1 vCPU")
                .storage("32 GB")
                .transfer("1 TB")
                .build();

        Product productTwo = Product.builder()
                .category("shared")
                .price(10)
                .ram("2 GB")
                .cores("1 vCPU")
                .storage("64 GB")
                .transfer("1 TB")
                .build();

        Product productThree = Product.builder()
                .category("shared")
                .price(20)
                .ram("4 GB")
                .cores("2 vCPU")
                .storage("128 GB")
                .transfer("2 TB")
                .build();

        productRepository.save(productOne);
        productRepository.save(productTwo);
        productRepository.save(productThree);

    }

}
