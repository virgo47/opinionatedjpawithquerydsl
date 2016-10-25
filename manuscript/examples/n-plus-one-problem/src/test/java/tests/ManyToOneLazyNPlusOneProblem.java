package tests;

import com.querydsl.jpa.impl.JPAQuery;
import nplusone.DogLazy;
import nplusone.QDogLazy;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class ManyToOneLazyNPlusOneProblem {

  public static void main(String[] args) {
    run("demo-el");
    run("demo-hib");
  }

  private static void run(String persistenceUnitName) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnitName);
    try {
      EntityManager em = emf.createEntityManager();
      NPlusOne.prepareData(em);

      // to get any caching out of the picture
      emf.getCache().evictAll();
      em.clear();

      List<DogLazy> dogs = new JPAQuery<>(em)
        .select(QDogLazy.dogLazy)
        .from(QDogLazy.dogLazy)
        .fetch();
      System.out.println("\nAfter selects if LAZY not honoured");
      System.out.println("\ndogs = " + dogs);

      em.close();
    } finally {
      emf.close();
    }
  }
}
