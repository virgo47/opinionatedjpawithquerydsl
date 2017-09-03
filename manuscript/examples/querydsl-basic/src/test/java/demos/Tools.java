package demos;

import model00.Breed;
import model00.Dog;

import javax.persistence.EntityManager;
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

  static Breed breed(EntityManager em, String name) {
    Breed collie = new Breed();
    collie.setName(name);
    em.persist(collie);
    return collie;
  }

  static Dog dog(EntityManager em, String name, Breed breed, int age) {
    Dog dog = new Dog();
    dog.setName(name);
    dog.setBreed(breed);
    dog.setAge(age);
    em.persist(dog);
    return dog;
  }
}
