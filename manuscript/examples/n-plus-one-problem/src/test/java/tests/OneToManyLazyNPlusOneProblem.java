package tests;

import com.querydsl.jpa.impl.JPAQuery;
import nplusone.Owner;
import nplusone.QOwner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class OneToManyLazyNPlusOneProblem {

  public static void main(String[] args) {
    run("demo-el");
    run("demo-hib");
  }

  private static void run(String persistenceUnitName) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnitName);
    try {
      EntityManager em = emf.createEntityManager();
      NPlusOne.prepareData(em);
      NPlusOne.clear(em);

      List<Owner> owners = new JPAQuery<>(em)
        .select(QOwner.owner)
        .from(QOwner.owner)
        .fetch();
      System.out.println("\nowners = " + owners);
      for (Owner owner : owners) {
        // EclipseLink prints "{IndirectSet: not instantiated}" and does not load yet
        // Hibernate prints actual content after loading the collection
        System.out.println(owner.getName() + "'s dogs = " + owner.getDogs());
      }

      em.close();
    } finally {
      emf.close();
    }
  }
}
