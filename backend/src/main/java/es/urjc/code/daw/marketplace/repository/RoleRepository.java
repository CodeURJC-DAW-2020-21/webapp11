package es.urjc.code.daw.marketplace.repository;

import es.urjc.code.daw.marketplace.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, Long>, PagingAndSortingRepository<Role, Long> {

    Role findFirstByOrderByIdAsc();

    Role findByName(String name);

    Role findRoleById(Long id);

    @Query("select u.roles from User u where u.id = :id")
    Set<Role> findRolesByUserId(Long id);

}
