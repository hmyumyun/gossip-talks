package bg.codeacademy.spring.gossiptalks.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.oneOf;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import bg.codeacademy.spring.gossiptalks.repository.UserRepository;
import io.restassured.RestAssured;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

  private static final String DEFAULT_PASS = "User123!";

  // get the random port, used by the spring application
  @LocalServerPort
  int port;

  @Autowired
  UserRepository userRepository;

  @BeforeEach
  public void beforeEachTest() {
    // init port and logging
    RestAssured.port = port;
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
  }

  @AfterEach
  public void afterEachTest() {
    RestAssured.reset();
    userRepository.deleteAll();
  }


  @Test
  // invalid username
  // OK.value-200
  // CREATED.value - 201
  public void createUser_with_InvalidUsername_should_fail() {
    given()
        .multiPart("email", "user@abv.bg")
        .multiPart("username", "user97")
        .multiPart("password", DEFAULT_PASS)
        .multiPart("passwordConfirmation", DEFAULT_PASS)
        .when()
        .post("api/v1/users")
        .then()
        .statusCode(not(oneOf(OK.value(), CREATED.value())));
  }

  @Test
  // create valid user
  public void createUser_with_ValidUsername_should_success() {
    given()
        .multiPart("email", "user@abv.bg")
        .multiPart("username", "user11")
        .multiPart("password", DEFAULT_PASS)
        .multiPart("passwordConfirmation", DEFAULT_PASS)
        .when()
        .post("api/v1/users")
        .then()
        .statusCode(OK.value());

  }

  @Test
  // create Invalid user
  public void createUser_with_ExistingUsername_should_fail() {

    //first user - success
    given()
        .multiPart("email", "testexistinguser@abv.bg")
        .multiPart("username", "testexistinguser")
        .multiPart("password", DEFAULT_PASS)
        .multiPart("passwordConfirmation", DEFAULT_PASS)
        .when()
        .post("api/v1/users")
        .then()
        .statusCode((oneOf(OK.value(), CREATED.value())));

    given()
        .multiPart("email", "testexistinguser@abv.bg")
        .multiPart("username", "testexistinguser")
        .multiPart("password", DEFAULT_PASS)
        .multiPart("passwordConfirmation", DEFAULT_PASS)
        .when()
        .post("api/v1/users")
        .then()
        .statusCode(not(oneOf(OK.value(), CREATED.value())));
  }

  @Test
  //missmatching password and passwordConfirmation
  public void createUser_with_MismatchingPassword_should_fail() {
    given()
        .multiPart("email", "haki@abv.bg")
        .multiPart("username", "user2")
        .multiPart("password", DEFAULT_PASS.concat("a"))
        .multiPart("passwordConfirmation", DEFAULT_PASS)
        .when()
        .post("api/v1/users")
        .then()
        .statusCode(not(oneOf(OK.value(), CREATED.value())));
  }

  @Test
  public void changePasswordOfCurrentUser_when_Authenticated_should_Pass() {
    createValidUser("fiko");
    given()
        .auth()
        .basic("fiko", DEFAULT_PASS)
        .multiPart("password", "Fiko123!")
        .multiPart("passwordConfirmation", "Fiko123!")
        .multiPart("oldPassword", DEFAULT_PASS)
        .when()
        .post("api/v1/users/me")
        .then()
        .statusCode(OK.value());

  }

  @Test
  // mismatcging password and passwordConfirmation
  public void changePasswordOfCurrentUser_when_MismatchingPassword_should_Fail() {
    createValidUser("niko");
    given()
        .auth()
        .basic("niko", DEFAULT_PASS)
        .multiPart("password", "Niko123!")
        .multiPart("passwordConfirmation", "Niko1234!")
        .multiPart("oldPassword", DEFAULT_PASS)
        .when()
        .post("api/v1/users/me")
        .then()
        .statusCode(not(oneOf(OK.value(), CREATED.value())));

  }

  @Test
  // mismatcging oldPassword
  public void changePasswordOfCurrentUser_when_MismatchingOldPassword_should_Fail() {
    createValidUser("ivan1");
    given()
        .auth()
        .basic("ivan1", DEFAULT_PASS)
        .multiPart("password", "Ivan123!")
        .multiPart("passwordConfirmation", "Ivan123!")
        .multiPart("oldPassword", DEFAULT_PASS + "a")
        .when()
        .post("api/v1/users/me")
        .then()
        .statusCode(not(oneOf(OK.value(), CREATED.value())));

  }

  @Test
  // old password and new password are the same
  public void changePasswordOfCurrentUser_when_OldPasswordAndNewPasswordAreTheSame_should_Fail() {
    createValidUser("jhonny");
    given()
        .auth()
        .basic("jhonny", DEFAULT_PASS)
        .multiPart("password", DEFAULT_PASS)
        .multiPart("passwordConfirmation", DEFAULT_PASS)
        .multiPart("oldPassword", DEFAULT_PASS)
        .when()
        .post("api/v1/users/me")
        .then()
        .statusCode(not(oneOf(OK.value(), CREATED.value())));

  }

  private void createValidUser(String name) {
    given()
        // prepare
        .multiPart("email", name + "@mail.com")
        .multiPart("username", name)//'^[a-z0-8\\.\\-]+$'
        .multiPart("name", name)
        .multiPart("password", DEFAULT_PASS)
        .multiPart("passwordConfirmation", DEFAULT_PASS)
        // do
        .when()
        .post("/api/v1/users")
        // test
        .then()
        .statusCode(oneOf(OK.value(), CREATED.value()));
  }
}
