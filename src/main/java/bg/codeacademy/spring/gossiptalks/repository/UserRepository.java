package bg.codeacademy.spring.gossiptalks.repository;

import bg.codeacademy.spring.gossiptalks.model.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource
public interface UserRepository extends JpaRepository<User, Long> {


  //  Page<User> findByFollowTrue(Pageable pageable);
//
//  Page<User> findByFollowTrueAndUsernameContainingIgnoreCase(String username, Pageable pageable);
//
  Page<User> findByUsernameContainingIgnoreCase(String username, Pageable pageable);

  Optional<User> findByUsername(String username);
}
