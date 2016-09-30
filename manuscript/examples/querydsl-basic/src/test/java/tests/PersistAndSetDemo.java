package tests;

import model00.Dog;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PersistAndSetDemo {

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
      dog.setName("Toothless");
      /*
      With Hibernate:
      - if id can be obtained from sequence, only that is called
      - if insert needs to be made (as with H2 and IDENTITY column) then insert is made
      - in any case, both insert and update is generated in the end
      With EclipseLink if no explicit flush is called before set only one INSERT is generated.
      */
      em.persist(dog);
      dog.setAge(4);

      em.getTransaction().commit();
    } finally {
      emf.close();
    }
  }
}
