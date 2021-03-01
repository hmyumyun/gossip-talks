package bg.codeacademy.spring.gossiptalks.service;

import bg.codeacademy.spring.gossiptalks.model.Gossip;
import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.repository.UserRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserService {


  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  //  връща всички user
  public Page<User> listUsers(Pageable pageable, String name, boolean follow) {
    if (follow) {
      //??????? for Valyo
      return (Page<User>) userRepository.findByUsername(name).getFriendList();
    } else {
      return userRepository.findAll(pageable);
    }
  }

//  //Създава госип
//  public void createGossip(User user, String content) {
//  }
//
//  //дпбавя user2 във friendList на user1
//  public void followUser(User user1, User user2)
//
//  //връща всички госипи от даден user
//  public List<Gossip> readAllGossips(User user) {
//  }
//
//  //всички госипи на всички приятели
//  public List<Gossip> readAllFriendsGossips() {
//  }
}
