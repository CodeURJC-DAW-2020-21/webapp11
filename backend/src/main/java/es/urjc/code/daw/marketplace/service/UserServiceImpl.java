package es.urjc.code.daw.marketplace.service;

import es.urjc.code.daw.marketplace.domain.Role;
import es.urjc.code.daw.marketplace.domain.User;
import es.urjc.code.daw.marketplace.repository.RoleRepository;
import es.urjc.code.daw.marketplace.repository.UserRepository;
import org.mapstruct.ap.internal.util.Strings;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final String DEFAULT_ROLE = "CLIENT";

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
        user.getRoles().add(Role.builder().id(clientRole.getId()).build());
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        User storedUser = userRepository.findUserById(user.getId());
        storedUser.setFirstName(user.getFirstName());
        storedUser.setSurname(user.getSurname());
        storedUser.setAddress(user.getAddress());
        storedUser.setProfilePictureUrl(user.getProfilePictureUrl());
        storedUser.setEmail(user.getEmail());
        if(Strings.isNotEmpty(user.getPassword())) {
            String newEncodedPassword = passwordEncoder.encode(user.getPassword());
            storedUser.setPassword(newEncodedPassword);
        }
        return userRepository.save(user);
    }

    @Override
    public User enableUser(Long id) {
        User storedUser = userRepository.findUserById(id);
        storedUser.setEnabled(true);
        return userRepository.save(storedUser);
    }

    @Override
    public User disableUser(Long id) {
        User storedUser = userRepository.findUserById(id);
        storedUser.setEnabled(false);
        return userRepository.save(storedUser);
    }

    @Override
    public boolean existsUserById(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findUserById(id);
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