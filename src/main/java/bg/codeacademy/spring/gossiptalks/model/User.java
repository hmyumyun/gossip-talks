package bg.codeacademy.spring.gossiptalks.model;

import java.time.OffsetDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Objects;

@Entity
public class User {

  @Id
  @GeneratedValue
  private String id;
  private String fullName;

  @NotNull
  @Size(min = 5, max = 10)
  @Pattern(regexp = "[a-zA-Z0-9]+")
  @Column(unique = true)
  private String username;

  @NotNull
  @Size(max = 1024)
  private String password;      //password hash
  private OffsetDateTime lastLoginTime;
  @NotNull
  private OffsetDateTime registrationTime;
  private String email; // validations !!!

  private List<User> friendList;

  public String getId() {
    return id;
  }

  public User setId(String id) {
    this.id = id;
    return this;
  }

  public String getFullName() {
    return fullName;
  }

  public User setFullName(String fullName) {
    this.fullName = fullName;
    return this;
  }

  public String getUsername() {
    return username;
  }

  public User setUsername(String username) {
    this.username = username;
    return this;
  }

  public String getPassword() {
    return password;
  }

  public User setPassword(String password) {
    this.password = password;
    return this;
  }

  public OffsetDateTime getLastLoginTime() {
    return lastLoginTime;
  }

  public User setLastLoginTime(OffsetDateTime lastLoginTime) {
    this.lastLoginTime = lastLoginTime;
    return this;
  }

  public OffsetDateTime getRegistrationTime() {
    return registrationTime;
  }

  public User setRegistrationTime(OffsetDateTime registrationTime) {
    this.registrationTime = registrationTime;
    return this;
  }

  public String getEmail() {
    return email;
  }

  public User setEmail(String email) {
    this.email = email;
    return this;
  }

  public List<User> getFriendList() {
    return friendList;
  }

  public User setFriendList(List<User> friendList) {
    this.friendList = friendList;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof User))
      return false;
    User user = (User) o;
    return getId().equals(user.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }

  @Override
  public String toString() {
    return "User{" +
        "id='" + id + '\'' +
        ", fullName='" + fullName + '\'' +
        ", email='" + email + '\'' +
        '}';
  }
}
