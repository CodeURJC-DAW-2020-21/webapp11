package es.urjc.code.daw.marketplace.repository;

import es.urjc.code.daw.marketplace.domain.User;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    User findUserById(Long id);

    User findUserByEmail(String email);

}
