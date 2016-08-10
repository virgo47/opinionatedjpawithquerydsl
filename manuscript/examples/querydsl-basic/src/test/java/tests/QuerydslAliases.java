package tests;

import com.querydsl.jpa.impl.JPAQuery;
import model00.aliasdemo.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class QuerydslAliases {

  public static void main(String[] args) {
    run("demo-el");
    run("demo-hib");
  }

  private static void run(String persistenceUnitName) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnitName);
    try {
      EntityManager em = emf.createEntityManager();

      pathUpToTwoLevelsAreOk(em);
//      pathTooDeepCausesNpe(em); // fails
      threeLevelPathWithOneLevelAlias(em);
      explicitAliasesForAllJoins(em);
      preCreatedAliases(em);
      em.close();
    } finally {
      emf.close();
    }
  }

  private static void pathUpToTwoLevelsAreOk(EntityManager em) {
    List<EntityC> result = new JPAQuery<>(em)
      .select(QEntityA.entityA.entityB.entityC)
      .from(QEntityA.entityA)
      .fetch();
  }

  private static void pathTooDeepCausesNpe(EntityManager em) {
    // fails with NPE, because 3rd level property entityD is not initialized
    // see: http://www.querydsl.com/static/querydsl/4.1.3/reference/html_single/#d0e2260
    List<EntityD> result = new JPAQuery<>(em)
      .select(QEntityA.entityA.entityB.entityC.entityD)
      .from(QEntityA.entityA)
      .fetch();
  }

  private static void threeLevelPathWithOneLevelAlias(EntityManager em) {
    // using alias to get path level down to two levels
    List<EntityD> result = new JPAQuery<>(em)
      .select(QEntityB.entityB.entityC.entityD)
      .from(QEntityA.entityA)
      .join(QEntityA.entityA.entityB, QEntityB.entityB)
      .fetch();
  }

  private static void explicitAliasesForAllJoins(EntityManager em) {
    // better way is to use aliases explicitly for all joins - here using existing default ones
    List<EntityD> result = new JPAQuery<>(em)
      .select(QEntityD.entityD)
      .from(QEntityA.entityA)
      // second parameter is alias for the path in the first parameter
      .join(QEntityA.entityA.entityB, QEntityB.entityB)
      // first parameter uses alias from the previous line
      .join(QEntityB.entityB.entityC, QEntityC.entityC)
      .join(QEntityC.entityC.entityD, QEntityD.entityD)
      .fetch();
  }

  private static void preCreatedAliases(EntityManager em) {
    QEntityA a = new QEntityA("a");
    QEntityB b = new QEntityB("b");
    QEntityC c = new QEntityC("c");
    QEntityD d = new QEntityD("d");
    List<EntityD> result = new JPAQuery<>(em)
      .select(d)
      .from(a)
      .join(a.entityB, b)
      .join(b.entityC, c)
      .join(c.entityD, d)
      .fetch();
  }
}
