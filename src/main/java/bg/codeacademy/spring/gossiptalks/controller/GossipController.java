package bg.codeacademy.spring.gossiptalks.controller;

import bg.codeacademy.spring.gossiptalks.dto.GossipList;
import bg.codeacademy.spring.gossiptalks.dto.GossipResponse;
import bg.codeacademy.spring.gossiptalks.model.Gossip;
import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.service.GossipService;
import bg.codeacademy.spring.gossiptalks.service.UserService;
import bg.codeacademy.spring.gossiptalks.validation.NoHtml;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1")
@Validated
public class GossipController {

  private final GossipService gossipService;
  private final UserService userService;

  public GossipController(GossipService gossipService, UserService userService) {
    this.gossipService = gossipService;
    this.userService = userService;
  }


  @PostMapping(value = "/gossips", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public GossipResponse publishGossip(
      @NotEmpty @NoHtml @RequestParam(name = "text") String content, Principal principal
  ) {
    User currentUser = userService.getGivenUser(principal.getName());
    Gossip gossip = gossipService.createGossip(currentUser, content);
    return toGossipDto(gossip, currentUser);

  }


  @GetMapping(value = "/gossips")
  public GossipList getGossipOfFriends(
      @Min(0) @RequestParam(name = "pageNo", required = false, defaultValue = "0") int pageNumber,
      @Min(0) @Max(50) @RequestParam(name = "pageSize", required = false, defaultValue = "20") int pageSize,
      Principal principal) {
    User currentUser = userService.getGivenUser(principal.getName());
    Pageable pageable = PageRequest.of(pageNumber, pageSize);
    Page<Gossip> gossips = gossipService
        .getAllGossipsOfFriends(currentUser, pageable);
    return toGossiplistDto(pageable, gossips);
  }

  @GetMapping(value = "/users/{username}/gossips")
  public GossipList getGossipOfGivenUsername(
      @RequestParam(name = "pageNo", required = false, defaultValue = "0") int pageNumber,
      @RequestParam(name = "pageSize", required = false, defaultValue = "20") int pageSize,
      @PathVariable("username") String username) {

    Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("dateTime").descending());
    Page<Gossip> gossips = gossipService.getAllGossipFromGivenUser(username, pageable);
    return toGossiplistDto(pageable, gossips);
  }

  public static GossipResponse toGossipDto(Gossip gossip, User user) {
    return new GossipResponse()
        .setText(gossip.getContent())
        .setIdFromGossipEntity(gossip.getId())
        .setUsername(user.getUsername())
        .setDatetime(gossip.getDateTime());
  }

  public static GossipList toGossiplistDto(Pageable pageable, Page<Gossip> gossips) {
    List<GossipResponse> gossipResponses = gossips.stream()
        .map(gossip -> toGossipDto(gossip, gossip.getAuthor()))
        .collect(Collectors.toList());

    return new GossipList()
        .setPageNumber(pageable.getPageNumber())
        .setPageSize(pageable.getPageSize())
        .setTotal((int) (gossips.getTotalElements()))
        .setCount(gossips.getNumberOfElements())
        .setContent(gossipResponses);
  }
}