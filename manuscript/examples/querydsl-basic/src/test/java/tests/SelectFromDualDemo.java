package tests;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.SimpleTemplate;
import com.querydsl.jpa.impl.JPAQuery;
import model00.system.QDual;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.UUID;

public class SelectFromDualDemo {

  public static void main(String[] args) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("demo-el");
    try {
      EntityManager em = emf.createEntityManager();

      // We "map" DUAL too to call table unrelated functions, but it works for normal tables
      // and functions in WHERE as well, of course
      // HOWEVER: currently only in EclipseLink, not in Hibernate 5.1.0! (try unit "demo-hib")
      UUID uuid = new JPAQuery<UUID>(em)
        .select(Expressions.template(UUID.class, "FUNCTION('random_uuid')"))
        .from(QDual.dual)
        .fetchOne();

      System.out.println("uuid = " + uuid);

      Double result = new JPAQuery<Double>(em)
        .select(random(3))
        .from(QDual.dual)
        .fetchOne();

      System.out.println("result = " + result);
    } finally {
      emf.close();
    }
  }

  private static SimpleTemplate<Double> random(int seed) {
    return Expressions.template(Double.class, "FUNCTION('random', {0})", seed);
  }
}
