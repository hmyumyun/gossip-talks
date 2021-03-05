package bg.codeacademy.spring.gossiptalks.service;

import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.repository.UserRepository;
import java.security.Principal;
import java.time.OffsetDateTime;
import java.util.Optional;
import javax.validation.constraints.NotEmpty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService //implements UserDetailsService {
{

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  public UserService(PasswordEncoder passwordEncoder,
      UserRepository userRepository) {
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
  }
//i don't know how to implement logic here
//  @Override
//  public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
//    Optional<User> user = userRepository.findByUsername(s);
//    User realUser=user.get();
//    realUser.setLastLoginTime(OffsetDateTime.now());
//    return realUser;
//  }
  //  връща всички user

  public Page<User> listAllUsers(int pageNo, int pageSize, boolean follow) {
    // sorting - wrong !!!
    Pageable paging = PageRequest.of(pageNo, pageSize);

    if (follow) {
      return userRepository.findByFollowTrue(paging);
    } else {
      return userRepository.findAll(paging);
    }
  }

  public Page<User> listMatchingUsers(int pageNo, int pageSize, @NotEmpty String name,
      boolean follow) {
    // sorting - wrong !!!
    Pageable paging = PageRequest.of(pageNo, pageSize);

    if (follow) {
      return userRepository.findByFollowTrueAndUsernameContainingIgnoreCase(name, paging);
    } else {
      return userRepository.findByUsernameContainingIgnoreCase(name, paging);
    }
  }

  public User register(@NotEmpty String email, String fullName, @NotEmpty String username,
      @NotEmpty String password,
      @NotEmpty String passwordConfirmation, boolean follow) {
    if (!password.equals(passwordConfirmation)) {
      throw new IllegalArgumentException("The passwords doesn't match");
    }
    if (userRepository.findByUsername(username).isPresent()) {
      throw new IllegalArgumentException("The username already exist");
    }
    User user = new User();
    user.setEmail(email);
    user.setFullName(fullName);
    user.setUsername(username);
    user.setPassword(passwordEncoder.encode(password));
    user.setFollow(follow);
    user.setRegistrationTime(OffsetDateTime.now());
    return userRepository.save(user);
  }

  public User getCurrentUser(String username) {
    return userRepository.findByUsername(username).get();

  }

  public User changePassword(User user, String oldPassword, String newPassword) {
    String currentHash = user.getPassword();
    if (!passwordEncoder.matches(oldPassword, newPassword)) {
      throw new IllegalArgumentException("The password doesn't match");
    }
    if (passwordEncoder.matches(currentHash, newPassword)) {
      throw new IllegalArgumentException("The passwords are the same");
    }
    user.setPassword(passwordEncoder.encode(newPassword));
    return userRepository.save(user);
  }
//  public void createGossip(User user, String content) {
//  }
//
//  //дпбавя user2 въвg friendList на user1
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
