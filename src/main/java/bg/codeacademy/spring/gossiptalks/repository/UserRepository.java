package bg.codeacademy.spring.gossiptalks.repository;

import bg.codeacademy.spring.gossiptalks.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.Optional;


@RepositoryRestResource
public interface UserRepository extends JpaRepository<User, String> {
  Optional<User> findByUserName(String username);
}
