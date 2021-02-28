package bg.codeacademy.spring.gossiptalks.dto;

import java.time.OffsetDateTime;

public class UserResponse {

  private String id;
  private String username;
  private OffsetDateTime lastLoginTime;
  private OffsetDateTime registrationTime;

  public String getId() {
    return id;
  }

  public UserResponse setId(String id) {
    this.id = id;
    return this;
  }

  public String getUsername() {
    return username;
  }

  public UserResponse setUsername(String username) {
    this.username = username;
    return this;
  }

  public OffsetDateTime getLastLoginTime() {
    return lastLoginTime;
  }

  public UserResponse setLastLoginTime(OffsetDateTime lastLoginTime) {
    this.lastLoginTime = lastLoginTime;
    return this;
  }

  public OffsetDateTime getRegistrationTime() {
    return registrationTime;
  }

  public UserResponse setRegistrationTime(OffsetDateTime registrationTime) {
    this.registrationTime = registrationTime;
    return this;
  }
}
