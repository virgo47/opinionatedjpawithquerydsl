package tests;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import modeltoone.Breed;
import modeltoone.Dog;
import modeltoone.QDog;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ProjectionToDogAndMerge {

  public static void main(String[] args) {
//    run("demo-el");
    run("demo-hib");
  }

  private static void run(String persistenceUnitName) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnitName);
    try {
      EntityManager em = emf.createEntityManager();
      prepareData(em);
      emf.getCache().evictAll();

      em.getTransaction().begin();
      QDog d = QDog.dog;
      Tuple result = new JPAQuery<>(em)
        .select(d.id, d.name)
        .from(d)
        .where(d.id.eq(1))
        .fetchOne();

      Dog dog = new Dog();
      dog.setId(result.get(d.id));
      dog.setName(result.get(d.name));
      System.out.println("dog from tuple = " + dog);

      // another select(s), including Breed table
      dog = em.merge(dog);
      dog.setName("Bassie");
      System.out.println("dog after merge = " + dog);

      // commit updates the whole entity setting the breed_id to null
      em.getTransaction().commit();
      em.close();
    } finally {
      emf.close();
    }
  }

  private static void prepareData(EntityManager em) {
    em.getTransaction().begin();

    Breed collie = new Breed();
    collie.setName("collie");
    em.persist(collie);

    Dog lassie = new Dog();
    lassie.setName("Lassie");
    lassie.setBreed(collie);
    em.persist(lassie);

    em.getTransaction().commit();
    em.clear();
  }
}
