package bg.codeacademy.spring.gossiptalks.controller;

import bg.codeacademy.spring.gossiptalks.dto.UserResponse;
import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.service.UserService;
import java.security.Principal;
import java.util.Set;
import javax.validation.constraints.NotEmpty;
import org.springframework.http.MediaType;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.http.Multipart;


@RestController
@RequestMapping("/api/v1")
@Validated
public class UserController {

  private final UserService userService;

  public UserController(
      UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/users")
  public List<UserResponse> listUsers(
      @PositiveOrZero @RequestParam(name = "page", required = false, defaultValue = "0") int pageNumber,
      @Min(5) @Max(100) @RequestParam(name = "size", required = false, defaultValue = "20") int pageSize,
      @RequestParam(name = "name", required = false) String name,
      @RequestParam(name = "f", defaultValue = "false") boolean follow, Principal principal) {

    User currentUser = userService.getCurrentUser(principal.getName());

    if (follow) {
      if (name == null) {
        Set<User> matching = currentUser.getFriendList();
        return matching.stream().map(user -> new UserResponse()
            .setEmail(user.getEmail())
            .setUsername(user.getUsername())
            .setName(user.getFullName())
            .setFollowing(currentUser, user)).collect(Collectors.toList());

      }
      List<User> matching = currentUser.getFriendList().stream()
          .filter(user -> user.getUsername().toLowerCase().contains(name.toLowerCase()))
          ///sorted should be implemented
          .skip(pageNumber * pageSize)
          .limit(pageSize)
          .collect(Collectors.toList());

      return matching.stream().map(user -> new UserResponse()
          .setEmail(user.getEmail())
          .setUsername(user.getUsername())
          .setName(user.getFullName())
          .setFollowing(currentUser, user)).collect(Collectors.toList());

    } else {
      Page<User> users = userService.listAllUsers(pageNumber, pageSize, name, follow);
      return users.stream().map(user -> new UserResponse()
          .setEmail(user.getEmail())
          .setUsername(user.getUsername())
          .setName(user.getFullName())
          .setFollowing(currentUser, user)).collect(Collectors.toList());
    }
  }

  @Multipart
  @PostMapping(value = "/users",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public User registerUser(
      @NotEmpty @RequestParam(name = "username") String username,
      @RequestParam(name = "name", required = false) String name,
      @NotEmpty @RequestParam(name = "email") String email,
      @NotEmpty @RequestParam(name = "password") String password,
      @NotEmpty @RequestParam(name = "passwordConfirmation") String passwordConfirmation,
      @RequestParam(name = "following", required = false, defaultValue = "false") boolean following
  ) {

    return userService.register(email, name, username, password, passwordConfirmation, following);
  }

  @GetMapping("/users/me")
  public UserResponse getCurrentUser(Principal principal) {
    User user = userService.getCurrentUser(principal.getName());
    return new UserResponse()
        .setEmail(user.getEmail())
        .setUsername(user.getUsername())
        .setName(user.getFullName())
        //always false, because don't follow himself
        .setFollowing(false);
  }

  @Multipart
  @PostMapping(value = "users/{username}/follow", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public UserResponse followUser(@PathVariable("username") String username,
      @RequestParam(name = "follow") boolean follow,
      Principal principal) {
    User current = userService.getCurrentUser(principal.getName());
    User target = userService.getCurrentUser(username);
    userService.followUser(current, username, follow);
    return new UserResponse()
        .setEmail(target.getEmail())
        .setUsername(target.getUsername())
        .setName(target.getFullName())
        .setFollowing(follow);

  }

  @Multipart
  @PostMapping(value = "users/me", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public UserResponse changePasswordCurrentUser(
      @NotEmpty @RequestParam(name = "password") String password,//new password
      @NotEmpty @RequestParam(name = "passwordConfirmation") String passwordConfirmation,
      @NotEmpty @RequestParam(name = "oldPassword") String oldPassword, Principal principal) {
    User current = userService.getCurrentUser(principal.getName());
    userService.changePassword(current, oldPassword, password, passwordConfirmation);
    return new UserResponse()
        .setEmail(current.getEmail())
        .setUsername(current.getUsername())
        .setName(current.getFullName())
        .setFollowing(false);
  }
}