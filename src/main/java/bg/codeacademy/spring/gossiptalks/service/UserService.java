package bg.codeacademy.spring.gossiptalks.service;

import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.repository.UserRepository;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    User realUser = getGivenUser(s);
    realUser.setLastLoginTime(OffsetDateTime.now());
    return userRepository.save(realUser);
  }

  //listUserWithoutCurrent
  // FIXME: GossipService, accepts Pageable, here we use pageNo, pageSize
  // pick one of the ways, and use it consistently
  // FIXME: too complex logic, split in more methods
  public Collection<User> listAllUsers(int pageNo, int pageSize, String name, boolean follow,
      User currentUser) {
    Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by("gossipsCounter").descending());
    Stream<User> matching;
    if (follow) {
      if (name == null) {
        matching = currentUser.getFriendList().stream();
        // add thenComparing username !!!
      } else {
        matching = currentUser.getFriendList().stream()
            .filter(user ->
                user.getUsername().toLowerCase().contains(name.toLowerCase()) ||
                    user.getFullName().toLowerCase().contains(name.toLowerCase())
            );
      }
    } else {
      if (name == null) {
        //ignore current User // ask valyo
        matching = userRepository.findAll(paging).stream().
            filter(user -> !user.getUsername().equals(currentUser.getUsername()));
      } else {
        matching = userRepository.findByUsernameContainingIgnoreCase(name, paging).stream();
      }
    }

    return matching
        .sorted(Comparator
            .comparingLong(User::getGossipsCounter).reversed()
            .thenComparing(User::getUsername))
        .skip((long) pageNo * pageSize)
        .limit(pageSize)
        .collect(Collectors.toList());
  }

  //FIXME :remove following flag
  public User register(@NotEmpty String email, @Valid String fullName, @NotEmpty String username,
      @NotEmpty String password,
      @NotEmpty String passwordConfirmation, boolean follow) {
    if (!password.equals(passwordConfirmation)) {
      throw new IllegalArgumentException("The passwords doesn't match");
    }
    if (userRepository.findByUsername(username).isPresent()) {
      throw new IllegalArgumentException("The username already exist");
    }
    User user = new User()
        .setEmail(email)
        .setFullName(fullName)
        .setRegistrationTime(OffsetDateTime.now())
        .setPassword(passwordEncoder.encode(password))
        .setUsername(username);
    return userRepository.save(user);
  }

  public User getGivenUser(String username) {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("Not found"));
  }

  public User changePassword(User user, String oldPassword, String newPassword,
      String newPasswordConfirmation) {
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
    User target = getGivenUser(username);
    if (toFollow) {
      current.getFriendList().add(target);
    } else {
      current.getFriendList().remove(target);
    }
    userRepository.save(current);
    return target;

  }
}