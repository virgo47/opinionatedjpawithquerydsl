package tests;

import com.querydsl.core.types.dsl.BooleanTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import modeladv.Dog;
import modeladv.QDog;

import java.time.LocalDate;

public class WhereDemo {

  public static void main(String[] args) {
    JPAQuery<Dog> query = dogQuery();

    addDynamicConditions(query, new DogFilterDto("Rex", LocalDate.now()));

    System.out.println("query = " + query);

    System.out.println("\nboth conditions fluently (WRONG):\n" + dogQuery()
      .where(QDog.dog.name.eq("Rex")
        .and(QDog.dog.birthdate.isNull())
        .or(QDog.dog.birthdate.goe(LocalDate.now()))));

    System.out.println("\nboth conditions fluently:\n" + dogQuery()
      .where(QDog.dog.name.eq("Rex")
        .and(QDog.dog.birthdate.isNull()
          .or(QDog.dog.birthdate.goe(LocalDate.now())))));

    System.out.println("\nboth conditions, different order:\n" + dogQuery()
      .where(QDog.dog.birthdate.isNull()
        .or(QDog.dog.birthdate.goe(LocalDate.now()))
        .and(QDog.dog.name.eq("Rex"))));

    System.out.println("\nboth conditions, different order," +
      " explicit redundant parenthesis:\n" +
      dogQuery().where(
        (QDog.dog.birthdate.isNull()
          .or(QDog.dog.birthdate.goe(LocalDate.now()))
        ).and(QDog.dog.name.eq("Rex"))));

    System.out.println("\nCoalesce: " + QDog.dog.birthdate
      .coalesce(LocalDate.MIN).asDate().goe(LocalDate.now()));

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

  private static void addDynamicConditions(
    JPAQuery<Dog> query, DogFilterDto filter)
  {
    if (filter.name != null) {
      query.where(QDog.dog.name.eq(filter.name));
    }
    if (filter.birthdate != null) {
      // coalesce possible too, but with converted type it fails on EclipseLink
//    query.where(QDog.dog.birthdate.coalesce(LocalDate.MIN).asDate()
//      .goe(filter.birthdate));
      query.where(QDog.dog.birthdate.isNull()
        .or(QDog.dog.birthdate.goe(filter.birthdate)));
    }
  }

  private static class DogFilterDto {
    public String name;
    public LocalDate birthdate;

    public DogFilterDto(String name, LocalDate birthdate) {
      this.name = name;
      this.birthdate = birthdate;
    }
  }

  private static JPAQuery<Dog> dogQuery() {
    return new JPAQuery<>()
      .select(QDog.dog)
      .from(QDog.dog);
  }
}
