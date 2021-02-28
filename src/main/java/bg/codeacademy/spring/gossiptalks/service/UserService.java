package bg.codeacademy.spring.gossiptalks.service;

import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;

@Service
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
    if(userRepository.count() == 0){
      register("admin", "admin", "admin");
    }
    return null;
  }

  public User register(String userName, String userPassword, String passConfirmation) {
    if(!userPassword.equals(passConfirmation)){
      throw new IllegalArgumentException("Password don't match");
    }

    if(userRepository.findByUserName(userName).isPresent()){
      throw new IllegalArgumentException("This username is taken!");
    }

    User user = new User();
    user.setUsername(userName);
    user.setPassword(passwordEncoder.encode(userPassword));
    user.setRegistrationTime(OffsetDateTime.now());

    return userRepository.save(user);
  }
}
