package tests;

import com.querydsl.jpa.impl.JPAQuery;
import model00.Dog;
import model00.QDog;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Map;

import static com.querydsl.core.group.GroupBy.groupBy;

/**
 * See http://www.querydsl.com/static/querydsl/4.1.3/reference/html_single/#d0e2228 and
 * https://github.com/querydsl/querydsl/blob/master/querydsl-collections/src/test/java/com/querydsl/collections/GroupByTest.java
 */
public class ResultTransformation {

  public static void main(String[] args) {
    run("demo-el");
    run("demo-hib");
  }

  private static void run(String persistenceUnitName) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnitName);
    try {
      EntityManager em = emf.createEntityManager();
      prepareData(em);

      Map<Integer, Dog> breedsById = new JPAQuery<>(em)
        .from(QDog.dog)
        .transform(groupBy(QDog.dog.id).as(QDog.dog));
      System.out.println("breedsById = " + breedsById);

      Map<String, Long> countByName = new JPAQuery<>(em)
        .from(QDog.dog)
        .groupBy(QDog.dog.name)
        .transform(groupBy(QDog.dog.name).as(QDog.dog.id.count()));
      System.out.println("countByName = " + countByName);

      em.close();
    } finally {
      emf.close();
    }

  }

  public static void prepareData(EntityManager em) {
    em.getTransaction().begin();

    Dog rexo = new Dog();
    rexo.setName("Rex");
    em.persist(rexo);

    Dog ben = new Dog();
    ben.setName("Ben");
    em.persist(ben);

    Dog ben2 = new Dog();
    ben2.setName("Ben");
    em.persist(ben2);

    em.getTransaction().commit();
  }
}
