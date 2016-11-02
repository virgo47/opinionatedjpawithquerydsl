package tests;

import com.querydsl.jpa.impl.JPAQuery;
import nplusone.Dog;
import nplusone.QDog;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class ManyToOneEagerNPlusOneProblem {

  public static void main(String[] args) {
    run("demo-el");
    run("demo-hib");
  }

  private static void run(String persistenceUnitName) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnitName);
    try {
      EntityManager em = emf.createEntityManager();
      NPlusOne.prepareData(em);

      queryForDogs(em);
      queryForDogsWithJoin(em);

      em.close();
    } finally {
      emf.close();
    }
  }

  private static void queryForDogs(EntityManager em) {
    System.out.println("\nQuery without join");
    NPlusOne.clear(em);

    List<Dog> dogs = new JPAQuery<>(em)
      .select(QDog.dog)
      .from(QDog.dog)
      .fetch();

    System.out.println("\nALL SELECTS HAPPENED");
    System.out.println("\ndogs = " + dogs);
  }

  private static void queryForDogsWithJoin(EntityManager em) {
    System.out.println("\nQuery with join");
    NPlusOne.clear(em);

    List<Dog> dogs = new JPAQuery<>(em)
      .select(QDog.dog)
      .from(QDog.dog)
      .leftJoin(QDog.dog.owner).fetchJoin() // fetchJoin necessary
      // fails on EclipseLink with: identification variable must be defined for a JOIN expression
//      .leftJoin(QDog.dog.owner)
//      .leftJoin(QDog.dog.owner, QDog.dog) // with alias, joins, but does not really fetch
      // fetchJoin and alias, against spec, but tolerated by both Hibernate and EclipseLink
//      .leftJoin(QDog.dog.owner).fetchJoin()
      .fetch();

    System.out.println("\nALL SELECTS HAPPENED");
    System.out.println("\ndogs = " + dogs);
  }
}
