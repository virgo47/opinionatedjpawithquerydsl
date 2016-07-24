package tests;

import com.querydsl.jpa.impl.JPAQuery;
import model00.Dog;
import model00.QDog;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FlushModeType;
import javax.persistence.Persistence;

public class QueryVsPersistenceContext {

  public static void main(String[] args) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("demo-el");
    try {
      EntityManager em = emf.createEntityManager();

      em.setFlushMode(FlushModeType.COMMIT); // AUTO is default

      DogQueryDemo.prepareData(em);
      em.getTransaction().begin();
      Dog dog = em.find(Dog.class, 1);
      dog.setName("Dex");
      System.out.println("dog.name = " + dog.getName()); // Rex

      String dogName = new JPAQuery<String>(em)
        .select(QDog.dog.name)
        .from(QDog.dog)
        .where(QDog.dog.id.eq(1))
        .fetchOne();

      System.out.println("dogName = " + dogName);
      em.getTransaction().commit();

      dogName = new JPAQuery<String>(em)
        .select(QDog.dog.name)
        .from(QDog.dog)
        .where(QDog.dog.id.eq(1))
        .fetchOne();

      System.out.println("dogName later = " + dogName);
    } finally {
      emf.close();
    }
  }

}
