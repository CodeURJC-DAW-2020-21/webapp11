package es.urjc.code.daw.marketplace.repository;

import es.urjc.code.daw.marketplace.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>, PagingAndSortingRepository<User, Long> {

    User findUserById(Long id);

    User findUserByEmail(String email);

    @Query("select u from User u order by u.id asc")
    List<User> findAllUsers(Pageable pageable);

}
