package bg.codeacademy.spring.gossiptalks.model;

import java.time.OffsetDateTime;
import javax.persistence.*;

@Entity
public class Gossip {

  @Id
 // @GeneratedValue
  private String id;
  private String content; // validate content ???, max.length=255 and  !!! no HTML
  private OffsetDateTime dateTime;

  // This create Many-to-One relation to User
  @ManyToOne
  private User user;
// ???private long likeCounter;

}
