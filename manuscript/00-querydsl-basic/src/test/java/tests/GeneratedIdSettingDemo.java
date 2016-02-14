package tests;

import model00.Dog;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class GeneratedIdSettingDemo {

  public static void main(String[] args) {
    run("demo-el");
    run("demo-hib");
    Tools.printResult();
  }

  private static void run(String persistenceUnitName) {
    Tools.setPrefix(persistenceUnitName + "> ");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnitName);
    try {
      EntityManager em = emf.createEntityManager();
      em.getTransaction().begin();

      Dog dog = new Dog();
      em.persist(dog);
      Tools.println("before flush: dogId = " + dog.getId());
      em.flush();
      Tools.println("after flush: dogId = " + dog.getId());

      em.getTransaction().commit();
    } finally {
      emf.close();
    }
  }
}
