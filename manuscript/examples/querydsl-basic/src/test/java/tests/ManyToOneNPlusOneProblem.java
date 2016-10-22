package tests;

import com.querydsl.jpa.impl.JPAQuery;
import model00.Dog;
import model00.QDog;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class ManyToOneNPlusOneProblem {

  public static void main(String[] args) {
    run("demo-el");
    run("demo-hib");
  }

  private static void run(String persistenceUnitName) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnitName);
    try {
      EntityManager em = emf.createEntityManager();
      Subqueries.prepareData(em);

      // to get any caching out of the picture
      emf.getCache().evictAll();
      em.clear();

      List<Dog> dogs = new JPAQuery<>(em)
        .select(QDog.dog)
        .from(QDog.dog)
        .fetch();
      System.out.println("dogs = " + dogs);

      em.close();
    } finally {
      emf.close();
    }
  }
}
