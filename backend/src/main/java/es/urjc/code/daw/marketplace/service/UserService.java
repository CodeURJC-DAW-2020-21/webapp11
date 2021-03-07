package es.urjc.code.daw.marketplace.service;

import es.urjc.code.daw.marketplace.domain.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    boolean existsUserById(Long id);

    User findUserById(Long id);

    User findUserByEmail(String email);

    List<User> findAllUsers(Pageable pageable);

    User registerUser(User user);

    User updateUser(User user, boolean updaterIsOperator);

    User deleteUserById(Long id);

}
