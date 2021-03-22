package bg.codeacademy.spring.gossiptalks.model;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Objects;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String fullName;

  @NotNull
  @Pattern(regexp = "^[a-z0-8\\\\.\\\\-]+$")
  @Column(unique = true)
  private String username;

  @NotNull
  @Size(max = 1024)
  private String password;      //password hash

  private OffsetDateTime lastLoginTime;

  @NotNull
  private OffsetDateTime registrationTime;

  private long gossipsCounter;

  private String email;

  @ManyToMany
  private Set<User> friendList;

  public long getGossipsCounter() {
    return gossipsCounter;
  }

  public User incrementGossipsCounter() {
    gossipsCounter++;
    return this;
  }


  public long getId() {
    return id;
  }

  public User setId(long id) {
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


  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public User setUsername(String username) {
    this.username = username;
    return this;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {

    return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

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

  public Set<User> getFriendList() {
    return friendList;
  }

  public User setFriendList(Set<User> friendList) {
    this.friendList = friendList;
    return this;
  }


  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return id == user.id && gossipsCounter == user.gossipsCounter && fullName.equals(user.fullName)
        && username.equals(user.username) && password.equals(user.password) && Objects
        .equals(lastLoginTime, user.lastLoginTime) && Objects
        .equals(registrationTime, user.registrationTime) && email.equals(user.email)
        && Objects.equals(friendList, user.friendList);
  }
}
