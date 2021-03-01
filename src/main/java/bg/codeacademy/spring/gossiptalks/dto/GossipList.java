package bg.codeacademy.spring.gossiptalks.dto;

import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class GossipList {

  @Size(min = 0)
  @NotNull
  private int pageNumber;
  @NotNull
  private int pageSize;
  @NotNull
  private int count;
  @NotNull
  private int total;
  @NotNull
  private List<Gossip> content;

  public int getPageNumber() {
    return pageNumber;
  }

  public GossipList setPageNumber(int pageNumber) {
    this.pageNumber = pageNumber;
    return this;
  }

  public int getPageSize() {
    return pageSize;
  }

  public GossipList setPageSize(int pageSize) {
    this.pageSize = pageSize;
    return this;
  }

  public int getCount() {
    return count;
  }

  public GossipList setCount(int count) {
    this.count = count;
    return this;
  }

  public int getTotal() {
    return total;
  }

  public GossipList setTotal(int total) {
    this.total = total;
    return this;
  }

  public List<Gossip> getContent() {
    return content;
  }

  public GossipList setContent(List<Gossip> content) {
    this.content = content;
    return this;
  }
}
