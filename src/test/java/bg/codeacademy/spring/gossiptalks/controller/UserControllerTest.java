package bg.codeacademy.spring.gossiptalks.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.repository.UserRepository;
import bg.codeacademy.spring.gossiptalks.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@ContextConfiguration
public class UserControllerTest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private UserService userService;
  @Autowired
  private UserRepository userRepository;


  private User testUser;
  String pass = "UserTest1!";

  @BeforeEach
  void setUp() {
    testUser = userService.register("user@abv.bg", "User User", "userTest", pass, pass, false);
  }

  @AfterEach
  void tearDown() {
    userRepository.deleteAll();
  }

  // ask Valyo ???
  @Test
  @WithAnonymousUser
  void given_mismatching_passwords_When_register_user_Then_fail() throws Exception {
    mvc.perform(post("/api/v1/users")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON_VALUE)
        .content(
            "{\"email\":\"haki@abv.bg\",\"fullName\":\"Hakan Myumyun\", \"username\":\"haki97\","
                + "\"password\":\"Haki123!\",\"passwordConfirmation\":\"Haki1234!\""))
        .andDo(print())
        .andExpect(status().isInternalServerError());
  }
}
