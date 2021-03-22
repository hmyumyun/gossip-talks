package bg.codeacademy.spring.gossiptalks.validation;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

class NoHtmlValidatorTest {

  private NoHtmlValidator noHtmlValidator = new NoHtmlValidator();
  private ConstraintValidatorContext context;

  @BeforeEach
  void setUp() {
    context = Mockito.mock(ConstraintValidatorContext.class);
    Mockito.when(context.buildConstraintViolationWithTemplate(Mockito.anyString()))
        .thenReturn(Mockito.mock(ConstraintViolationBuilder.class));
  }

  @ParameterizedTest
  @MethodSource("provideValuesForIsValid")
  void is_Valid(String input, boolean expected) {
    assertEquals(expected, noHtmlValidator.isValid(input, context));
  }

  private static Stream<Arguments> provideValuesForIsValid() {
    return Stream.of(
        Arguments.of("Hello world", true),
        Arguments.of("group1", true),
        Arguments.of("",false),
        Arguments.of("Contains single tag <tag/>", false),
        Arguments.of("<body id=\"wpdiscuz_5.3.5\">This is a body</body>", false),
        Arguments.of("There is <openTag></openTag> inside.", false),
        Arguments.of("And a single tag <singleTagWithAttr attr=\"alabala\"/> with attribute", false)
    );
  }


}