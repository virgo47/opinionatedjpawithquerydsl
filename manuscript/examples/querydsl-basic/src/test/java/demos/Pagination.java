package demos;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQuery;
import model00.Breed;
import model00.QBreed;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import java.util.stream.IntStream;

public class Pagination {

  private static int pageSize = 10;
  private static int page = 2;

  public static void main(String[] args) {
    run("demo-el");
    run("demo-hib");
  }

  private static void run(String persistenceUnitName) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnitName);
    try {
      EntityManager em = emf.createEntityManager();
      prepareData(em);

      manualPagination(em);
      manualPaginationWithCount(em);
      queryResults(em);

      em.close();
    } finally {
      emf.close();
    }
  }

  private static void manualPagination(EntityManager em) {
    List<Breed> breedsPageX = new JPAQuery<>(em)
      .select(QBreed.breed)
      .from(QBreed.breed)
      .where(QBreed.breed.id.lt(17))
      .orderBy(QBreed.breed.name.asc())
      .offset((page - 1) * pageSize)
      .limit(pageSize)
      .fetch();
    System.out.println("breedsPageX = " + breedsPageX);
  }

  private static void manualPaginationWithCount(EntityManager em) {
    JPAQuery<Breed> breedQuery = new JPAQuery<>(em)
      .select(QBreed.breed)
      .from(QBreed.breed)
      .where(QBreed.breed.id.lt(17))
      .orderBy(QBreed.breed.name.asc());

    long total = breedQuery.fetchCount();
    List<Breed> breedsPageX = breedQuery
      .offset((page - 1) * pageSize)
      .limit(pageSize)
      .fetch();
    System.out.println("total count: " + total);
    System.out.println("breedsPageX = " + breedsPageX);
  }

  private static void queryResults(EntityManager em) {
    QueryResults<Breed> results = new JPAQuery<>(em)
      .select(QBreed.breed)
      .from(QBreed.breed)
      .where(QBreed.breed.id.lt(17))
      .orderBy(QBreed.breed.name.asc())
      .offset((page - 1) * pageSize)
      .limit(pageSize)
      .fetchResults();
    System.out.println("total count: " + results.getTotal());
    System.out.println("results = " + results.getResults());
  }

  public static void prepareData(EntityManager em) {
    em.getTransaction().begin();

    IntStream.range(1, 27)
      .forEach(i -> Tools.breed(em, "Breed" + i));

    em.getTransaction().commit();
  }
}
