package tests;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import model00.Breed;
import model00.Dog;
import model00.QBreed;
import model00.QDog;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

import static tests.Tools.breed;
import static tests.Tools.dog;

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

      // multi-row subquery
      List<Dog> dogsWithLongBreedName = new JPAQuery<>(em)
        .select(QDog.dog)
        .from(QDog.dog)
        .where(QDog.dog.breed.in(
          JPAExpressions.selectFrom(QBreed.breed)
            // no fetch on subquery
            .where(QBreed.breed.name.length().goe(7))))
        .fetch();
      System.out.println("dogsWithLongBreedName = " + dogsWithLongBreedName);

      // single-row subquery (aggregate function)
      List<Dog> dogsOlderThanAverage = new JPAQuery<>(em)
        .select(QDog.dog)
        .from(QDog.dog)
        .where(QDog.dog.age.gt(
          JPAExpressions.select(QDog.dog.age.avg())
            .from(QDog.dog)))
        .fetch();
      System.out.println("dogsOlderThanAverage = " + dogsOlderThanAverage);

      // correlated subquery - exists
      List<Breed> breedsWithoutDogs = new JPAQuery<>(em)
        .select(QBreed.breed)
        .from(QBreed.breed)
        .where(
          JPAExpressions.selectFrom(QDog.dog)
            .where(QDog.dog.breed.eq(QBreed.breed))
            .notExists())
        .fetch();
      System.out.println("breedsWithoutDogs = " + breedsWithoutDogs);

      // correlated subquery - aggregate function
      QDog innerDog = new QDog("innerDog");
      List<Dog> dogsOlderThanBreedAverage = new JPAQuery<>(em)
        .select(QDog.dog)
        .from(QDog.dog)
        .where(QDog.dog.age.gt(
          JPAExpressions.select(innerDog.age.avg())
            .from(innerDog)
            .where(innerDog.breed.eq(QDog.dog.breed))))
        .fetch();
      System.out.println("dogsOlderThanBreedAverage = " + dogsOlderThanBreedAverage);

      em.close();
    } finally {
      emf.close();
    }
  }

  public static void prepareData(EntityManager em) {
    em.getTransaction().begin();

    Breed collie = breed(em, "collie");
    Breed germanShepherd = breed(em, "german shepherd");
    breed(em, "retriever");

    dog(em, "Lassie", collie, 7);
    dog(em, "Rex", germanShepherd, 6);
    dog(em, "Ben", germanShepherd, 4);
    dog(em, "Mixer (unknown breed)", null, 3);

    em.getTransaction().commit();
  }
}
