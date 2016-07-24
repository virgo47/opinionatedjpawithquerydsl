package tests;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Tools {

  private static final StringWriter result = new StringWriter();
  private static PrintWriter out = new PrintWriter(result);

  private static String prefix = "";

  public static void setPrefix(String prefix) {
    Tools.prefix = prefix;
  }

  public static void println() {
    println("");
  }

  public static void println(Object string) {
    System.out.println(prefix + string);
    out.println(prefix + string);
  }

  public static void printResult() {
    System.out.println(result.toString());
  }
}
