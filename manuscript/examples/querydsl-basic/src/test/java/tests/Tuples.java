package tests;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import model00.Breed;
import model00.Dog;
import model00.QBreed;
import model00.QDog;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class Tuples {

  public static void main(String[] args) {
    run("demo-el");
    run("demo-hib");
  }

  private static void run(String persistenceUnitName) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnitName);
    try {
      EntityManager em = emf.createEntityManager();
      prepareData(em);

      tuplesOfColumns(em);
      tuplesOfEntities(em);

      em.close();
    } finally {
      emf.close();
    }
  }

  private static void tuplesOfColumns(EntityManager em) {
    List<Tuple> result = new JPAQuery<>(em)
      .select(QDog.dog.name, QBreed.breed.name)
      .from(QDog.dog)
      .join(QDog.dog.breed, QBreed.breed)
      .fetch();
    result.forEach(t -> {
      String name = t.get(QDog.dog.name);
      String breed = t.get(1, String.class);
      System.out.println("Dog: " + name + " is " + breed);
    });
  }

  private static void tuplesOfEntities(EntityManager em) {
    List<Tuple> result = new JPAQuery<>(em)
      .select(QDog.dog, QBreed.breed)
      .from(QDog.dog)
      .join(QDog.dog.breed, QBreed.breed)
      .fetch();
    result.forEach(t -> {
      Dog dog = t.get(QDog.dog);
      Breed breed = t.get(QBreed.breed);
      System.out.println("\nDog: " + dog);
      System.out.println("Breed: " + breed);
    });
  }

  public static void prepareData(EntityManager em) {
    em.getTransaction().begin();

    Breed breed = new Breed();
    breed.setName("collie");
    em.persist(breed);

    Dog dog = new Dog();
    dog.setName("Rex");
    dog.setAge(3);
    dog.setBreed(breed);
    em.persist(dog);

    em.getTransaction().commit();
  }
}
