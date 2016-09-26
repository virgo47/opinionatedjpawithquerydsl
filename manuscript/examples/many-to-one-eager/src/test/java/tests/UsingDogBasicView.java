package tests;

import modeltoone.Breed;
import modeltoone.Dog;
import modeltoone.DogBasicView;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class UsingDogBasicView {

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

      em.getTransaction().begin();
      DogBasicView dogView = em.find(DogBasicView.class, 1);
      System.out.println("dogView = " + dogView);
      Dog dog = em.find(Dog.class, 1);
      System.out.println("dog = " + dog);

      dogView.setName("Bassie");
      em.flush();

      System.out.println("dog after flushed UPDATE = " + dog);
      em.refresh(dog);
      System.out.println("dog after refresh = " + dog);

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
