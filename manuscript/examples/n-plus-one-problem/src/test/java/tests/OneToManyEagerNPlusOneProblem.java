package tests;

import com.querydsl.jpa.impl.JPAQuery;
import nplusone.OwnerEager;
import nplusone.QOwnerEager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class OneToManyEagerNPlusOneProblem {

  public static void main(String[] args) {
    run("demo-el");
    run("demo-hib");
  }

  private static void run(String persistenceUnitName) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnitName);
    try {
      EntityManager em = emf.createEntityManager();
      NPlusOne.prepareData(em);

      naiveQuery(em);
      fetchJoinQuery(em);

      em.close();
    } finally {
      emf.close();
    }
  }

  private static void naiveQuery(EntityManager em) {
    NPlusOne.clear(em);

    List<OwnerEager> owners = new JPAQuery<>(em)
      .select(QOwnerEager.ownerEager)
      .from(QOwnerEager.ownerEager)
      .fetch();
    System.out.println("\nowners = " + owners);
    for (OwnerEager owner : owners) {
      System.out.println(owner.getName() + "'s dogs = " + owner.getDogs());
    }
  }

  private static void fetchJoinQuery(EntityManager em) {
    NPlusOne.clear(em);

    List<OwnerEager> owners = new JPAQuery<>(em)
      .select(QOwnerEager.ownerEager)
      .from(QOwnerEager.ownerEager)
      .leftJoin(QOwnerEager.ownerEager.dogs).fetchJoin()
      .fetch();
    System.out.println("\nowners = " + owners);
    for (OwnerEager owner : owners) {
      System.out.println(owner.getName() + "'s dogs = " + owner.getDogs());
    }
  }
}
