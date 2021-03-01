package bg.codeacademy.spring.gossiptalks.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class Gossip {

  @NotNull
  @Size(max = 255)
  private String text;
  @NotNull
  @Pattern(regexp = "[A-Z0-9]+")
  private String id;
  @NotNull
  @Pattern(regexp = "^[a-z0-8\\\\.\\\\-]+$")
  private String username;
  @NotNull
  private String datetime;

  public String getText() {
    return text;
  }

  public Gossip setText(String text) {
    this.text = text;
    return this;
  }

  public String getId() {
    return id;
  }

  public Gossip setId(String id) {
    this.id = id;
    return this;
  }

  public String getUsername() {
    return username;
  }

  public Gossip setUsername(String username) {
    this.username = username;
    return this;
  }

  public String getDatetime() {
    return datetime;
  }

  public Gossip setDatetime(String datetime) {
    this.datetime = datetime;
    return this;
  }
}
