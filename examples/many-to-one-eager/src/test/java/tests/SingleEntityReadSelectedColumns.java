package tests;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import modeltoone.Breed;
import modeltoone.Dog;
import modeltoone.QDog;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class SingleEntityReadSelectedColumns {

  public static void main(String[] args) {
    run("demo-el");
    run("demo-hib");
  }

  private static void run(String persistenceUnitName) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnitName);
    try {
      EntityManager em = emf.createEntityManager();
      prepareData(em);
      emf.getCache().evictAll();

      System.out.println("\nfind");
      Tuple tuple = new JPAQuery<QDog>(em)
        .select(QDog.dog.id, QDog.dog.name)
        .from(QDog.dog)
        .where(QDog.dog.id.eq(1))
        .fetchOne();

      System.out.println("tuple = " + tuple);
    } finally {
      emf.close();
    }
  }

  private static void prepareData(EntityManager em) {
    em.getTransaction().begin();
    Breed wolf = new Breed();
    wolf.setName("wolf");
    em.persist(wolf);

    Breed germanShepherd = new Breed();
    germanShepherd.setName("german shepherd");
    germanShepherd.setDerivedFrom(wolf);
    em.persist(germanShepherd);

    Breed collie = new Breed();
    collie.setName("collie");
    collie.setDerivedFrom(germanShepherd);
    em.persist(collie);

    Dog lassie = new Dog();
    lassie.setName("Lassie");
    lassie.setBreed(collie);
    em.persist(lassie);

    em.getTransaction().commit();
    em.clear();
  }
}
