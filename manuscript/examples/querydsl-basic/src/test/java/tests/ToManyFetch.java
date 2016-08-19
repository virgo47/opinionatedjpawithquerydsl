package tests;

import com.querydsl.jpa.impl.JPAQuery;
import model00.Breed;
import model00.QBreed;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class ToManyFetch {

  public static void main(String[] args) {
    run("demo-el");
    run("demo-hib");
  }

  private static void run(String persistenceUnitName) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnitName);
    try {
      EntityManager em = emf.createEntityManager();
      prepareData(em);
      em.close();
      // We need to isolate EMs here, otherwise Breeds don't get their dogs reliably.

      // And for EclipseLink we also need to evict old breeds that don't know their dogs yet.
      // This would probably be necessary for Hibernate too, if we used some cache with it.
      emf.getCache().evictAll();

      em = emf.createEntityManager();

      naiveFetch(em);
      joinFetch(em);

      em.close();
    } finally {
      emf.close();
    }
  }

  private static void naiveFetch(EntityManager em) {
    List<Breed> breeds = new JPAQuery<>(em)
      .select(QBreed.breed)
      .from(QBreed.breed)
      .fetch();
    breeds.forEach(b ->
      System.out.println(b.toString() + ": " + b.getDogs()));
  }

  private static void joinFetch(EntityManager em) {
    List<Breed> breeds = new JPAQuery<>(em)
      .select(QBreed.breed)
      .from(QBreed.breed)
      .join(QBreed.breed.dogs).fetchJoin()
      .distinct()
      .fetch();
    breeds.forEach(b ->
      System.out.println(b.toString() + ": " + b.getDogs()));
  }

  public static void prepareData(EntityManager em) {
    em.getTransaction().begin();

    Breed collie = Tools.breed(em, "collie");
    Breed retriever = Tools.breed(em, "retriever");

    Tools.dog(em, "Rex", collie, 3);
    Tools.dog(em, "Ben", collie, 6);
    Tools.dog(em, "Mumu", retriever, 6);
    Tools.dog(em, "Lio", retriever, 5);
    Tools.dog(em, "Axor", retriever, 5);

    em.getTransaction().commit();
  }

}
