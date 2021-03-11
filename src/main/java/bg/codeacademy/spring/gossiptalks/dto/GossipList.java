package bg.codeacademy.spring.gossiptalks.dto;

import java.util.List;
import javax.validation.constraints.NotNull;


public class GossipList {

  @NotNull
  private int pageNumber;
  @NotNull
  private int pageSize;
  @NotNull
  private int count;
  @NotNull
  private int total;
  @NotNull
  private List<GossipResponse> content;

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

  public List<GossipResponse> getContent() {
    return content;
  }

  public GossipList setContent(List<GossipResponse> content) {
    this.content = content;
    return this;
  }
}
