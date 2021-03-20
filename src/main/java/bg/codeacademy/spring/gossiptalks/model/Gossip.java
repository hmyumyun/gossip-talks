package bg.codeacademy.spring.gossiptalks.model;

import bg.codeacademy.spring.gossiptalks.validation.NoHtml;
import java.time.OffsetDateTime;
import java.util.Objects;
import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
public class Gossip {

  @Id
  @GeneratedValue
  private long id;

  @Size(min = 2, max = 255)
  @NoHtml
  private String content;
  private OffsetDateTime dateTime;

  // This create Many-to-One relation to User
  @ManyToOne
  private User user;


  public long getId() {
    return id;
  }


  public String getContent() {
    return content;
  }

  public Gossip setContent(String content) {
    this.content = content;
    return this;
  }

  public OffsetDateTime getDateTime() {
    return dateTime;
  }

  public Gossip setDateTime(OffsetDateTime dateTime) {
    this.dateTime = dateTime;
    return this;
  }

  public User getUser() {
    return user;
  }

  public Gossip setUser(User user) {
    this.user = user;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Gossip gossip = (Gossip) o;
    return id == gossip.id && content.equals(gossip.content) && dateTime.equals(gossip.dateTime)
        && user.equals(gossip.user);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, content, dateTime, user);
  }
}
