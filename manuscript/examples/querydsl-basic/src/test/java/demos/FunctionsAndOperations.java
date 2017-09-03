package demos;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import model00.QBreed;
import model00.QDog;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class FunctionsAndOperations {

  public static void main(String[] args) {
    run("demo-el");
    run("demo-hib");
  }

  private static void run(String persistenceUnitName) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnitName);
    try {
      EntityManager em = emf.createEntityManager();
      Subqueries.prepareData(em);

      // show half of dog's age (nonsense, I know)
      List<Tuple> dogsGettingYounger = new JPAQuery<>(em)
        .select(QDog.dog.name, QDog.dog.age.divide(2))
        .from(QDog.dog)
        .fetch();
      System.out.println("dogsGettingYounger = " + dogsGettingYounger);

      List<Tuple> dogsAndNameLengths = new JPAQuery<>(em)
        .select(QDog.dog.name, QDog.dog.name.length())
        .from(QDog.dog)
        .fetch();
      System.out.println("dogsAndNameLengths = " + dogsAndNameLengths);

      List<Tuple> dogAvgAgeByBreed = new JPAQuery<>(em)
        .select(QBreed.breed.id, QBreed.breed.name, QDog.dog.age.avg())
        .from(QDog.dog)
        .leftJoin(QDog.dog.breed, QBreed.breed)
        .groupBy(QBreed.breed.id, QBreed.breed.name)
        .orderBy(QBreed.breed.name.asc())
        .fetch();
      System.out.println("dogAvgAgeByBreed = " + dogAvgAgeByBreed);

      em.close();
    } finally {
      emf.close();
    }
  }
}
