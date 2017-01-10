package tests;

import com.querydsl.jpa.impl.JPAQuery;
import modeladv.Dog;
import modeladv.QDog;

import java.time.LocalDate;

public class DynamicWhereDemo {

  public static void main(String[] args) {
    JPAQuery<Dog> query = new JPAQuery<>()
      .select(QDog.dog)
      .from(QDog.dog);

    query.where(QDog.dog.name.eq("Rex"));
    query.where(QDog.dog.birthdate.isNull()
      .or(QDog.dog.birthdate.goe(LocalDate.now())));

    System.out.println("query = " + query);
  }
}
