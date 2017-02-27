package tests;

import com.querydsl.core.group.GroupBy;
import com.querydsl.jpa.impl.JPAQuery;
import modeladv.Dog;
import modeladv.QBreed;
import modeladv.QDog;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
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

      Map<String, List<Dog>> listByName = new JPAQuery<>(em)
        .from(QDog.dog)
        .transform(groupBy(QDog.dog.name).as(GroupBy.list(QDog.dog)));
      System.out.println("listByName = " + listByName);

      em.close();
    } finally {
      emf.close();
    }

  }

  private static void prepareData(EntityManager em) {
    em.getTransaction().begin();

    Dog rexo = new Dog();
    rexo.name = "Rex";
    em.persist(rexo);

    Dog ben = new Dog();
    ben.name = "Ben";
    em.persist(ben);

    Dog ben2 = new Dog();
    ben2.name = "Ben";
    em.persist(ben2);

    em.getTransaction().commit();
  }
}
