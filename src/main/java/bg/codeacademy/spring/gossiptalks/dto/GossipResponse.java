package bg.codeacademy.spring.gossiptalks.dto;

import java.time.OffsetDateTime;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class GossipResponse {

  @NotNull
  @Size(max = 255)
  private String text;
  @NotNull
  @Pattern(regexp = "[A-Z0-9]+")
  private String id;
  @NotNull
  @Pattern(regexp = "^[a-z0-8\\.\\-]+$")
  private String username;

  private OffsetDateTime datetime;

  public String getText() {
    return text;
  }

  public GossipResponse setText(String text) {
    this.text = text;
    return this;
  }
//convert id to 36-base format
  public GossipResponse setIdFromGossipEntity(long id) {
    this.id = Long.toString(id, 36);
    return this;
  }

  public String getId() {
    return id;
  }

  public GossipResponse setId(String id) {
    this.id = id;
    return this;
  }

  public String getUsername() {
    return username;
  }

  public GossipResponse setUsername(String username) {
    this.username = username;
    return this;
  }

  public OffsetDateTime getDatetime() {
    return datetime;
  }

  public GossipResponse setDatetime(OffsetDateTime datetime) {
    this.datetime = datetime;
    return this;
  }
}
