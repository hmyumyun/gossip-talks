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


  public Collection<User> listAllUsers(Pageable pageable, String name, boolean follow,
      User currentUser) {
    Pageable paging = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
        Sort.by("gossipsCounter").descending());
    Stream<User> matching;
    if (follow) {
      matching = listUsersWithGivenNameAndFollowTrue(name, currentUser);
    } else {
      matching = listUserWithGivenNameAndFollowFalse(name, currentUser, paging);
    }
    return matching
        .sorted(Comparator
            .comparingLong(User::getGossipsCounter).reversed()
            .thenComparing(User::getUsername))
        .skip((long) pageable.getPageNumber() * pageable.getPageSize())
        .limit(pageable.getPageSize())
        .collect(Collectors.toList());
  }

  public Stream<User> listUsersWithGivenNameAndFollowTrue(String username, User currentUser) {
    Stream<User> matching;
    if (username == null) {
      matching = currentUser.getFriendList().stream();
    } else {
      matching = currentUser.getFriendList().stream()
          .filter(user ->
              user.getUsername().toLowerCase().contains(username.toLowerCase()) ||
                  user.getFullName().toLowerCase().contains(username.toLowerCase())
          );
    }
    return matching;
  }

  public Stream<User> listUserWithGivenNameAndFollowFalse(String username, User currentUser,
      Pageable pageable) {
    Stream<User> matching;
    if (username == null) {
      matching = userRepository.findAll(pageable).stream().
          filter(user -> !user.getUsername().equals(currentUser.getUsername()));
    } else {
      matching = userRepository.findByUsernameContainingIgnoreCase(username, pageable).stream();
    }
    return matching;
  }

  public User register(@NotEmpty String email, @Valid String fullName, @NotEmpty String username,
      @NotEmpty String password,
      @NotEmpty String passwordConfirmation) {
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