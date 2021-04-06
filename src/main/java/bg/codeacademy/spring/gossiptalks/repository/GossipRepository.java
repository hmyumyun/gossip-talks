package bg.codeacademy.spring.gossiptalks.repository;

import bg.codeacademy.spring.gossiptalks.model.Gossip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface GossipRepository extends JpaRepository<Gossip, Long> {

  @Query(value = "SELECT * FROM GOSSIP g "
      + "WHERE g.Author_ID IN (SELECT FRIEND_LIST_ID "
      + "FROM USERS_TABLE_FRIEND_LIST uf "
      + "WHERE uf.USER_ID = :user_id) "
      + "ORDER BY g.DATE_TIME DESC", nativeQuery = true)
  Page<Gossip> findAllGossipsOfFriends(@Param("user_id") Long user_id, Pageable pageable);


  Page<Gossip> findByAuthorUsername(String username, Pageable pageable);
}
