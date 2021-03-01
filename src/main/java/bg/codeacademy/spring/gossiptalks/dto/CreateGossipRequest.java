package bg.codeacademy.spring.gossiptalks.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CreateGossipRequest {

  @NotNull
  @Size(max = 255)
  private String text;

  public String getText() {
    return text;
  }

  public CreateGossipRequest setText(String text) {
    this.text = text;
    return this;
  }
}
