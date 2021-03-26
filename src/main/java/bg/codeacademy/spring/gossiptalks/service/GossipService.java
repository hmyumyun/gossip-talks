package bg.codeacademy.spring.gossiptalks.service;

import bg.codeacademy.spring.gossiptalks.model.Gossip;
import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.repository.GossipRepository;
import bg.codeacademy.spring.gossiptalks.repository.UserRepository;
import java.time.OffsetDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GossipService {

  private GossipRepository gossipRepository;
  private UserRepository userRepository;

  public GossipService(GossipRepository gossipRepository, UserRepository userRepository) {
    this.gossipRepository = gossipRepository;
    this.userRepository = userRepository;
  }

  public Gossip createGossip(User user, String content) {
    Gossip gossip = new Gossip()
        .setContent(content)
        .setDateTime(OffsetDateTime.now())
        .setAuthor(user);
    user.incrementGossipsCounter();
    userRepository.save(user);
    return gossipRepository.save(gossip);
  }

  public Page<Gossip> getAllGossipsOfFriends(User current, Pageable pageable) {
    long currentUserId = current.getId();
    return gossipRepository.findAllGossipsOfFriends(currentUserId, pageable);

  }

  public Page<Gossip> getAllGossipFromGivenUser(String username, Pageable pageable) {
    return gossipRepository.findByAuthorUsername(username, pageable);
  }
}
