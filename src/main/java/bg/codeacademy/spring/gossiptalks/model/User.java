package bg.codeacademy.spring.gossiptalks.model;

import java.time.OffsetDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Entity
public class User {

  @Id
  @GeneratedValue
  private String id;
  private String fullName;

//  @NotNull
//  @Size(min = 5, max = 10)
//  @Pattern(regexp = "[a-zA-Z0-9]+")
//  @Column(unique = true)
//  private String username;
//
//  @NotNull
//  @Size(max = 1024)
//  private String password;      //password hash
//  private OffsetDateTime lastLoginTime;
//  @NotNull
//  private OffsetDateTime registrationTime;
//  private String email; // validations !!!
//
//  private List<User> friendList;

  }
