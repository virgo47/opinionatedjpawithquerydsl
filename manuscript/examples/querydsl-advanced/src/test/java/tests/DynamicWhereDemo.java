package tests;

import com.querydsl.core.types.dsl.BooleanTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import modeladv.Dog;
import modeladv.QDog;

import java.time.LocalDate;

public class DynamicWhereDemo {

  public static void main(String[] args) {
    JPAQuery<Dog> query = dogQuery();

    query.where(QDog.dog.name.eq("Rex"));
    query.where(QDog.dog.birthdate.isNull()
      .or(QDog.dog.birthdate.goe(LocalDate.now())));

    System.out.println("query = " + query);

    System.out.println("\nthe same condition fluently (WRONG):\n" + dogQuery()
      .where(QDog.dog.name.eq("Rex")
        .and(QDog.dog.birthdate.isNull())
        .or(QDog.dog.birthdate.goe(LocalDate.now()))));

    System.out.println("\nthe same condition fluently:\n" + dogQuery()
      .where(QDog.dog.name.eq("Rex")
        .and(QDog.dog.birthdate.isNull()
          .or(QDog.dog.birthdate.goe(LocalDate.now())))));

    System.out.println("\nthe same condition, different order:\n" + dogQuery()
      .where(QDog.dog.birthdate.isNull()
        .or(QDog.dog.birthdate.goe(LocalDate.now()))
        .and(QDog.dog.name.eq("Rex"))));

    System.out.println("\nthe same condition, different order, explicit redundant parenthesis:\n" + dogQuery()
      .where(
        (QDog.dog.birthdate.isNull()
          .or(QDog.dog.birthdate.goe(LocalDate.now()))
        ).and(QDog.dog.name.eq("Rex"))));

    BooleanTemplate e1 = Expressions.booleanTemplate("e1");
    BooleanTemplate e2 = Expressions.booleanTemplate("e2");
    BooleanTemplate e3 = Expressions.booleanTemplate("e3");
    BooleanTemplate e4 = Expressions.booleanTemplate("e4");
    System.out.println("\ne1.or(e2).and(e3).or(e4) = " +
      e1.or(e2).and(e3).or(e4));
    System.out.println("\ne1.or(e2).and(e3).or(e4) = " + new JPAQuery<>()
      .where(e1.or(e2).and(e3).or(e4)));

    BooleanTemplate e5 = Expressions.booleanTemplate("e5");
    BooleanTemplate e6 = Expressions.booleanTemplate("e6");
    System.out.println("\ne1.or(e2).and(e3.or(e4)).or(e5.and(e6)) = " +
      new JPAQuery<>()
        .where(e1.or(e2).and(e3.or(e4)).or(e5.and(e6))));
  }

  private static JPAQuery<Dog> dogQuery() {
    return new JPAQuery<>()
      .select(QDog.dog)
      .from(QDog.dog);
  }
}
