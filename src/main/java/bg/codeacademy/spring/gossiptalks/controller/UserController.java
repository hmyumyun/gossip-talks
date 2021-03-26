package bg.codeacademy.spring.gossiptalks.controller;

import bg.codeacademy.spring.gossiptalks.dto.UserResponse;
import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.service.UserService;
import bg.codeacademy.spring.gossiptalks.validation.ValidPassword;
import java.security.Principal;
import java.util.Collection;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1")
@Validated
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/users")
  public List<UserResponse> listUsers(
      @Min(0) @RequestParam(name = "page", required = false, defaultValue = "0") int pageNumber,
      @Min(0) @Max(50) @RequestParam(name = "size", required = false, defaultValue = "20") int pageSize,
      @RequestParam(name = "name", required = false) String name,
      @RequestParam(name = "f", defaultValue = "false") boolean follow, Principal principal) {

    User currentUser = userService.getGivenUser(principal.getName());
    Pageable paging = PageRequest.of(pageNumber, pageSize);
    Collection<User> matching = userService
        .listAllUsers(paging, name, follow, currentUser);

    return matching.stream()
        .map(user -> convertToUserResponse(user, (currentUser.getFriendList().contains(user))))
        .collect(Collectors.toList());
  }

  @PostMapping(value = "/users", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public User registerUser(
      @NotEmpty @Pattern(regexp = "^[a-z0-8\\\\.\\\\-]+$")
      @RequestParam(name = "username") String username,
      @RequestParam(name = "name", required = false) String name,
      @NotEmpty @Email @RequestParam(name = "email") String email,
      @NotEmpty @ValidPassword @RequestParam(name = "password") String password,
      @NotEmpty @RequestParam(name = "passwordConfirmation") String passwordConfirmation

  ) {

    return userService.register(email, name, username, password, passwordConfirmation);
  }


  @PostMapping(value = "/users/{username}/follow", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public UserResponse followUser(@PathVariable("username") String username,
      @RequestParam(name = "follow") boolean follow,
      Principal principal) {
    User current = userService.getGivenUser(principal.getName());
    User target = userService.followUser(current, username, follow);
    return convertToUserResponse(target, follow);
  }

  @GetMapping("/users/me")
  public UserResponse getCurrentUser(Principal principal) {
    User user = userService.getGivenUser(principal.getName());
    return convertToUserResponse(user, false);
  }


  @PostMapping(value = "/users/me", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public UserResponse changePasswordCurrentUser(
      @NotEmpty @ValidPassword @RequestParam(name = "password") String password,//new password
      @NotEmpty @RequestParam(name = "passwordConfirmation") String passwordConfirmation,
      @NotEmpty @RequestParam(name = "oldPassword") String oldPassword, Principal principal) {
    User current = userService.getGivenUser(principal.getName());
    userService.changePassword(current, oldPassword, password, passwordConfirmation);

    return convertToUserResponse(current, false);
  }

  public static UserResponse convertToUserResponse(User user, boolean follow) {
    return new UserResponse()
        .setEmail(user.getEmail())
        .setUsername(user.getUsername())
        .setName(user.getFullName())
        .setFollowing(follow);
  }
}