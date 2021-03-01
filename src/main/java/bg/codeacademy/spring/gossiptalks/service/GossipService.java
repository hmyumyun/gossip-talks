package bg.codeacademy.spring.gossiptalks.service;

import bg.codeacademy.spring.gossiptalks.model.Gossip;
import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.repository.GossipRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GossipService {

  private GossipRepository gossipRepository;

  public GossipService(GossipRepository gossipRepository) {
    this.gossipRepository = gossipRepository;
  }


}
