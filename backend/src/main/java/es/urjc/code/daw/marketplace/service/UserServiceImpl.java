package es.urjc.code.daw.marketplace.service;

import es.urjc.code.daw.marketplace.domain.Role;
import es.urjc.code.daw.marketplace.domain.User;
import es.urjc.code.daw.marketplace.repository.RoleRepository;
import es.urjc.code.daw.marketplace.repository.UserRepository;
import es.urjc.code.daw.marketplace.security.user.UserPrincipal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * An implementation of the {@link UserService}.
 */

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final String DEFAULT_ROLE = "ROLE_CLIENT";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository authorityRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role clientRole = roleRepository.findByName(DEFAULT_ROLE);
        Set<Role> roles = new HashSet<>();
        roles.add(Role.builder().id(clientRole.getId()).build());
        user.setRoles(roles);
        return userRepository.saveAndFlush(user);
    }

    @Override
    public User updateUser(User user, boolean isModifierAdmin) {
        User storedUser = userRepository.findUserById(user.getId());
        if(user.getFirstName() != null && StringUtils.isNotEmpty(user.getFirstName())) {
            storedUser.setFirstName(user.getFirstName());
        }
        if(user.getSurname() != null && StringUtils.isNotEmpty(user.getSurname())) {
            storedUser.setSurname(user.getSurname());
        }
        if(user.getAddress() != null && StringUtils.isNotEmpty(user.getAddress())) {
            storedUser.setAddress(user.getAddress());
        }
        if(user.getEmail() != null && StringUtils.isNotEmpty(user.getEmail())) {
            storedUser.setEmail(user.getEmail());
        }
        if(isModifierAdmin) {
            storedUser.setIsEnabled(user.getIsEnabled());
        }

        if(!Objects.isNull(user.getProfilePictureFilename())) {
            storedUser.setProfilePictureFilename(user.getProfilePictureFilename());
        }

        String newPassword = StringUtils.trim(user.getPassword());
        if(StringUtils.isNotEmpty(newPassword) && StringUtils.isNotBlank(newPassword)) {
            String newEncodedPassword = passwordEncoder.encode(user.getPassword());
            storedUser.setPassword(newEncodedPassword);
        }
        userRepository.saveAndFlush(storedUser);
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null) {
            try {
                UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
                if(principal.getUser().getId().longValue() == user.getId().longValue()) {
                    principal.setUser(storedUser);
                }
            } catch (Exception ignored) { }
        }

        return storedUser;
    }

    @Override
    public User enableUser(Long id) {
        User storedUser = userRepository.findUserById(id);
        storedUser.setIsEnabled(true);
        return userRepository.saveAndFlush(storedUser);
    }

    @Override
    public User disableUser(Long id) {
        User storedUser = userRepository.findUserById(id);
        storedUser.setIsEnabled(false);
        return userRepository.saveAndFlush(storedUser);
    }

    @Override
    public boolean existsUserById(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public User findUserById(Long id) {
        try {
            return userRepository.findUserById(id);
        } catch(Exception exception) {
            return null;
        }
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public List<User> findAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).stream().collect(Collectors.toList());
    }

    @Override
    public User deleteUserById(Long id) {
        User user = userRepository.findUserById(id);
        userRepository.deleteById(id);
        return user;
    }

}