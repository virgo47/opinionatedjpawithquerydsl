package tests;

import com.querydsl.jpa.impl.JPAUpdateClause;
import model00.Dog;
import model00.QDog;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class BulkUpdateVsPersistenceContext {

  public static void main(String[] args) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("demo-el");
    try {
      EntityManager em = emf.createEntityManager();
      DogQueryDemo.prepareData(em);

      em.getTransaction().begin();
      Dog dog = em.find(Dog.class, 1);
      System.out.println("dog.name = " + dog.getName()); // Rex

      new JPAUpdateClause(em, QDog.dog)
        .set(QDog.dog.name, "Dex")
        .execute();

      dog = em.find(Dog.class, 1); // find does not do much here
      System.out.println("dog.name = " + dog.getName()); // still Rex

      em.refresh(dog); // this reads the real data now
      System.out.println("after refresh: dog.name = " + dog.getName()); // Dex

      em.getTransaction().commit();
    } finally {
      emf.close();
    }
  }
}
