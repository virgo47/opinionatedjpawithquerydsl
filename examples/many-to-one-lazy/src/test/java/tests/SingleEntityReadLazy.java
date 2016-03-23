package tests;

import modeltoone.Breed;
import modeltoone.Dog;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.lang.reflect.Field;

public class SingleEntityReadLazy {

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
      Dog dog = em.find(Dog.class, 1);
      System.out.println("\nhacking");
      Field breedField = dog.getClass().getDeclaredField("breed");
      System.out.println(breedField.getType());
      breedField.setAccessible(true);
      Object val = breedField.get(dog);
      System.out.println("val = " + val);

      System.out.println("\ntraversing");
      Breed breed = dog.getBreed();
      while (breed.getDerivedFrom() != null) {
        breed = breed.getDerivedFrom();
      }
      System.out.println("breed = " + breed.getName());
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
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
