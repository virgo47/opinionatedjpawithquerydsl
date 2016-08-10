package tests;

import com.querydsl.jpa.impl.JPAQuery;
import model00.Breed;
import model00.Dog;
import model00.QBreed;
import model00.QDog;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class Subqueries {

  public static void main(String[] args) {
    run("demo-el");
    run("demo-hib");
  }

  private static void run(String persistenceUnitName) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnitName);
    try {
      EntityManager em = emf.createEntityManager();
      prepareData(em);

      List<Breed> breedsWithDogs = new JPAQuery<>(em)
        .select(QBreed.breed)
        .from(QBreed.breed)
        .where(
          new JPAQuery<>()
            // select not needed here
            .from(QDog.dog)
            .where(QDog.dog.breed.eq(QBreed.breed))
            .exists())
        .fetch();

      System.out.println("breedsWithDogs = " + breedsWithDogs);

      List<Dog> dogsOlderThanAverage = new JPAQuery<>(em)
        .select(QDog.dog)
        .from(QDog.dog)
        .where(QDog.dog.age.gt(
          new JPAQuery<>()
            .select(QDog.dog.age.avg())
            .from(QDog.dog)))
        .fetch();

      System.out.println("dogsOlderThanAverage = " + dogsOlderThanAverage);

      em.close();
    } finally {
      emf.close();
    }

  }

  public static void prepareData(EntityManager em) {
    em.getTransaction().begin();

    Breed collie = new Breed();
    collie.setName("collie");
    em.persist(collie);

    Breed germanShepherd = new Breed();
    germanShepherd.setName("german shepherd");
    em.persist(germanShepherd);

    Breed retriever = new Breed();
    retriever.setName("retriever");
    em.persist(retriever);

    Dog lassie = new Dog();
    lassie.setName("Lassie");
    lassie.setBreed(collie);
    lassie.setAge(7);
    em.persist(lassie);

    Dog rexo = new Dog();
    rexo.setName("Rex");
    rexo.setBreed(germanShepherd);
    rexo.setAge(6);
    em.persist(rexo);

    Dog ben = new Dog();
    ben.setName("Ben");
    ben.setBreed(germanShepherd);
    ben.setAge(4);
    em.persist(ben);

    em.getTransaction().commit();
  }
}
