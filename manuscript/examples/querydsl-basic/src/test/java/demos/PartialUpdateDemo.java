package demos;

import model00.Dog;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PartialUpdateDemo {

  public static void main(String[] args) {
    run("demo-el"); // EclipseLink generates UPDATE for a single changed column
    run("demo-hib"); // Hibernate updates all columns
  }

  private static void run(String persistenceUnitName) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnitName);
    try {
      EntityManager em = emf.createEntityManager();
      DogQueryDemo.prepareData(em);
      emf.getCache().evictAll();

      System.out.println("\nUPDATE with " + persistenceUnitName);
      em.getTransaction().begin();
      Dog dog1 = em.find(Dog.class, 1);
      dog1.setName("OtherName");
      em.getTransaction().commit();
    } finally {
      emf.close();
    }
  }
}
