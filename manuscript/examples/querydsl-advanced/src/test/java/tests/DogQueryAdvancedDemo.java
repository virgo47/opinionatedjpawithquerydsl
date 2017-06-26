package tests;

import com.querydsl.core.types.dsl.Param;
import com.querydsl.jpa.impl.JPAQuery;
import modeladv.Breed;
import modeladv.Dog;
import modeladv.QDog;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.util.List;

/** @noinspection UnusedReturnValue*/
public class DogQueryAdvancedDemo {

  public static void main(String[] args) throws IOException {
    run("demo-el");
    run("demo-hib");
  }

  private static void run(String persistenceUnit) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnit);
    try {
      EntityManager em = emf.createEntityManager();
      prepareData(em);

      detachedQuery(em);
      queryObject(em);
    } finally {
      emf.close();
    }
  }

  private static QDog DOG_ALIAS = new QDog("d1");

  private static Param<String> DOG_NAME_PREFIX = new Param<>(String.class);

  private static JPAQuery<Dog> DOG_QUERY = new JPAQuery<>()
    .select(DOG_ALIAS)
    .from(DOG_ALIAS)
    .where(DOG_ALIAS.name.startsWith(DOG_NAME_PREFIX));

  private static void detachedQuery(EntityManager em) {
    List<Dog> dogs = DOG_QUERY.clone(em)
      .set(DOG_NAME_PREFIX, "Re")
      .fetch();

    System.out.println("\nDetached query: " + dogs);
  }

  private static void queryObject(EntityManager em) {
    List<Dog> dogs = new DogQuery(em)
      .nameStartsWith("Re")
      .fetch();

    System.out.println("\nQuery object: " + dogs);
  }

  private static void prepareData(EntityManager em) {
    em.getTransaction().begin();

    Breed collie = breed(em, "collie");
    Breed germanShepherd = breed(em, "german shepherd");
    dog(em, "Lassie", collie);
    dog(em, "Rex", germanShepherd);

    em.getTransaction().commit();
  }

  private static Breed breed(EntityManager em, String code) {
    Breed collie = new Breed();
    collie.setCode(code);
    em.persist(collie);
    return collie;
  }

  private static Dog dog(EntityManager em, String name, Breed breed) {
    Dog dog = new Dog();
    dog.name = name;
    dog.breedId = breed.getId();
    em.persist(dog);
    return dog;
  }
}
