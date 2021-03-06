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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {


  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  public UserService(PasswordEncoder passwordEncoder,
      UserRepository userRepository) {
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
//    return userRepository.findByUsername(s)
//        .orElseThrow(() ->new UsernameNotFoundException("Not found"));
    Optional<User> user = userRepository.findByUsername(s);
    User realUser = user.get();
    realUser.setLastLoginTime(OffsetDateTime.now());
    userRepository.save(realUser);
    return realUser;
  }
  //  връща всички user

  public Page<User> listAllUsers(int pageNo, int pageSize, String name, boolean follow) {
    // sorting - wrong !!!
    Pageable paging = PageRequest.of(pageNo, pageSize);

    if (name == null) {
      return userRepository.findAll(paging);
    } else {
      return userRepository.findByUsernameContainingIgnoreCase(name, paging);
    }

  }


//  public Page<User> listMatchingUsers(int pageNo, int pageSize, @NotEmpty String name,
//      boolean follow) {
//    // sorting - wrong !!!
//    Pageable paging = PageRequest.of(pageNo, pageSize);
//
//    if (follow) {
//      return userRepository.findByFollowTrueAndUsernameContainingIgnoreCase(name, paging);
//    } else {
//      return userRepository.findByUsernameContainingIgnoreCase(name, paging);
//    }
//  }

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
    user.setRegistrationTime(OffsetDateTime.now());
    return userRepository.save(user);
  }

  public User getCurrentUser(String username) {
    return userRepository.findByUsername(username).get();

  }

  public User changePassword(User user, String oldPassword, String newPassword, String
      newPasswordConfirmation) {
    String currentHash = user.getPassword();
    if (!passwordEncoder.matches(oldPassword, currentHash)) {
      throw new IllegalArgumentException("The current password doesn't match");
    }
    if (passwordEncoder.matches(newPassword, currentHash)) {
      throw new IllegalArgumentException("The passwords are the same");
    }
    if (!newPassword.equals(newPasswordConfirmation)) {
      throw new IllegalArgumentException("The password confirmation doesn't match");
    }
    user.setPassword(passwordEncoder.encode(newPassword));
    return userRepository.save(user);
  }

  public User followUser(User current, String username, boolean toFollow) {
    User user = userRepository.findByUsername(username).get();
    if (user == null) {
      throw new UsernameNotFoundException("Not Found!");
    }
    if (toFollow) {
      // add user to followers
      // ??? password added  to following list !!!!!
      current.getFriendList().add(user);
    } else {
      // remove From followers
      current.getFriendList().remove(user);
    }
    return userRepository.save(current);
  }
}