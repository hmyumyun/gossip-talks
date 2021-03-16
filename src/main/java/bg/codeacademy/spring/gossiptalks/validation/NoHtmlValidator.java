package bg.codeacademy.spring.gossiptalks.validation;

import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NoHtmlValidator implements ConstraintValidator<NoHtml, String> {


  // adapted from re posted by Phil Haack and modified to match better
  public final static String tagStart =
      "\\<\\w+((\\s+\\w+(\\s*\\=\\s*(?:\".*?\"|'.*?'|[^'\"\\>\\s]+))?)+\\s*|\\s*)\\>";
  public final static String tagEnd =
      "\\</\\w+\\>";
  public final static String tagSelfClosing =
      "\\<\\w+((\\s+\\w+(\\s*\\=\\s*(?:\".*?\"|'.*?'|[^'\"\\>\\s]+))?)+\\s*|\\s*)/\\>";
  public final static String htmlEntity =
      "&[a-zA-Z][a-zA-Z0-9]+;";
  public final static Pattern htmlPattern = Pattern.compile(
      "(" + tagStart + ".*" + tagEnd + ")|(" + tagSelfClosing + ")|(" + htmlEntity + ")",
      Pattern.DOTALL
  );

  public static boolean isHtml(String s) {
    boolean result = false;
    if (s != null) {
      result = htmlPattern.matcher(s).find();
    }
    return result;
  }


  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {

    if (value == null || value.isEmpty()) {
      return false;
    }
    return !isHtml(value);
  }

}
