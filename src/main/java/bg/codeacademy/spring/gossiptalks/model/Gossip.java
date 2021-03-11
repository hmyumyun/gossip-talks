package bg.codeacademy.spring.gossiptalks.model;

import java.time.OffsetDateTime;
import java.util.Objects;
import javax.persistence.*;

@Entity
public class Gossip {

  @Id
  @GeneratedValue
  private long id;
  private String content; // validate content ???, max.length=255 and  !!! no HTML
  private OffsetDateTime dateTime;

  // This create Many-to-One relation to User
  @ManyToOne
  private User user;


  public long getId() {
    return id;
  }

  public Gossip setId(long id) {
    this.id = id;
    return this;
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
    if (!(o instanceof Gossip)) {
      return false;
    }
    Gossip gossip = (Gossip) o;
    return getId() == (gossip.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }

  @Override
  public String toString() {
    return "Gossip{" +
        "id='" + id + '\'' +
        ", content='" + content + '\'' +
        ", dateTime=" + dateTime +
        ", user=" + user +
        '}';
  }
}
