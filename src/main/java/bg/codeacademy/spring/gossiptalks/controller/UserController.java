package bg.codeacademy.spring.gossiptalks.controller;

import bg.codeacademy.spring.gossiptalks.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class UserController {
private  final UserService service;

  public UserController(UserService service) {
    this.service = service;
  }
// @RequestMapping("/api/v1/users")
//  @GetMapping
}
