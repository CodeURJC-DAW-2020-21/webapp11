package es.urjc.code.daw.marketplace.service;

import es.urjc.code.daw.marketplace.domain.User;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface UserService {

    User registerUser(User user);

    User updateUser(User user);

    User enableUser(Long id);

    User disableUser(Long id);

    boolean existsUserById(Long id);

    User findUserById(Long id);

    User saveUser(User user);

    User findUserByEmail(String email);

    List<User> findAllUsers(Pageable pageable);

    User deleteUserById(Long id);

}
