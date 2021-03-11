package bg.codeacademy.spring.gossiptalks.repository;

import bg.codeacademy.spring.gossiptalks.model.Gossip;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource
public interface GossipRepository extends JpaRepository<Gossip, Long> {

  @Query(value = "SELECT * FROM GOSSIP g "
      + "WHERE USER_ID IN (SELECT FRIEND_LIST_ID "
      + "FROM USER_FRIEND_LIST uf "
      + "WHERE uf.USER_ID = :user_id) "
      + "ORDER BY g.DATE_TIME DESC", nativeQuery = true)
  Page<Gossip> findAllGossipsOfFriends(@Param("user_id") Long user_id, Pageable pageable);

//  @Query(value = "SELECT * FROM USER u JOIN GOSSIP g "
//      + "ON u.ID=g.USER_ID "
//      + "WHERE u.USERNAME = :username "
//      + "ORDER BY g.DATE_TIME DESC", nativeQuery = true)
//  Page<Gossip> findGossipWithGivenUsername(@Param("username") String name, Pageable pageable);
  Page<Gossip> findByUserUsername(String username,Pageable pageable);
}
