package blog.hashmade.zeromq.util;

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public final class StringHelper {

  public static final String SPACE = " ";
  public static final String COMMA = ",";
  public static final String DASH = "-";
  public static final String UNDERSCORE = "_";
  public static final String EMPTY_STRING = "";
  public static final String NEW_LINE_STRING = "\n";
  public static final String NEW_LINE_WINDOWS_STRING = "\r\n";
  public static final String NEW_LINE_HTML_STRING = "<br/>";

  public static final String ARRAY_DELIMITER = ";";

  private StringHelper(){
  }
  
  public static String buildString(final String separator, final Object... args) {
    StringBuilder buffer = new StringBuilder();
    int i = 0;
    for (Object arg : args) {
      buffer.append((arg != null) ? arg.toString() : "");
      if (i != args.length - 1)
        buffer.append(separator);
      i++;
    }
    return buffer.toString();
  }

  public static String buildStringNoSep(final Object... args) {
    StringBuilder buffer = new StringBuilder();
    for (Object arg : args) {
      buffer.append((arg != null) ? arg.toString() : "");
    }
    return buffer.toString();
  }

  public static boolean isStringEmpty(String string) {
    return string == null || string.length() == 0;
  }

  public static String[] splitString(final String inputString, final String delimiter) {
    StringTokenizer tokenizer = new StringTokenizer(inputString, delimiter);
    List<String> strings = new LinkedList<String>();

    while (tokenizer.hasMoreElements()) {
      strings.add(tokenizer.nextElement().toString());
    }
    String[] stringArrays = new String[strings.size()];
    return strings.toArray(stringArrays);
  }

  public static String buildStringFromArray(Object[] objects) {
    return buildString(ARRAY_DELIMITER, objects);
  }

  

}
