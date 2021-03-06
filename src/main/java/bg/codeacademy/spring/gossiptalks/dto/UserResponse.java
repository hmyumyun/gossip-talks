package bg.codeacademy.spring.gossiptalks.dto;

import bg.codeacademy.spring.gossiptalks.model.User;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class UserResponse {


  @NotEmpty
  private String email;
  @NotEmpty
  @Pattern(regexp = "^[a-z0-8\\\\.\\\\-]+$")
  private String username;
  private String name;
  private boolean following;

  public String getEmail() {
    return email;
  }


  public UserResponse setFollowing(User current, User target) {
    if (current.getFriendList().contains(target)) {
      this.following = true;
    } else {
      this.following = false;
    }
    return this;
  }

  public UserResponse setEmail(String email) {
    this.email = email;
    return this;
  }

  public String getUsername() {
    return username;
  }

  public UserResponse setUsername(String username) {
    this.username = username;
    return this;
  }

  public String getName() {
    return name;
  }

  public UserResponse setName(String name) {
    this.name = name;
    return this;
  }

  public boolean isFollowing() {
    return following;
  }

  public UserResponse setFollowing(boolean following) {
    this.following = following;
    return this;
  }
}
