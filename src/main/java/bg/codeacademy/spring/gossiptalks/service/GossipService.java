package bg.codeacademy.spring.gossiptalks.service;

import bg.codeacademy.spring.gossiptalks.model.Gossip;
import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.repository.GossipRepository;
import bg.codeacademy.spring.gossiptalks.repository.UserRepository;
import bg.codeacademy.spring.gossiptalks.validation.NoHtml;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class GossipService {

  private GossipRepository gossipRepository;
  private UserRepository userRepository;

  public GossipService(GossipRepository gossipRepository,
      UserRepository userRepository) {
    this.gossipRepository = gossipRepository;
    this.userRepository = userRepository;
  }

  public Gossip createGossip(User user, String content) {
    Gossip gossip = new Gossip();
    gossip.setContent(content);
    gossip.setDateTime(OffsetDateTime.now());
    gossip.setUser(user);
    user.incrementGossipsCounter();
    userRepository.save(user);
    return gossipRepository.save(gossip);
  }

  public Page<Gossip> getAllGossipsOfFriends(User current, Pageable pageable) {
    long currentUserId = current.getId();
    return gossipRepository.findAllGossipsOfFriends(currentUserId, pageable);

  }

  public Page<Gossip> getAllGossipFromGivenUser(String username, Pageable pageable) {
    Pageable paging = PageRequest
        .of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("dateTime").descending());
    return gossipRepository.findByUserUsername(username, paging);
  }
}
