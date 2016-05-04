package tests;

import modeltoone.Breed;
import modeltoone.Dog;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DualMappingDemo {

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

      Dog dog = em.find(Dog.class, 1);
      System.out.println("dog = " + dog);
      System.out.println("dog.getBreedId() = " + dog.getBreedId());
      System.out.println("dog.getBreed() = " + dog.getBreed());
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
