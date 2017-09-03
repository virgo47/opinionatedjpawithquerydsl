package testng;

import com.querydsl.jpa.impl.JPAQueryFactory;
import model00.QDog;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * All the other test classes are just demo programs with main method,
 * so at least this one is here to test the tests during build.
 */
public class DemoTest {

  @Test
  public void testEclipselinkUnit() {
    test("demo-el");
  }

  @Test
  public void testHibernateUnit() {
    test("demo-hib");
  }

  private void test(String persistenceUnit) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnit);
    try {
      EntityManager em = emf.createEntityManager();

      long count = new JPAQueryFactory(em)
        .selectFrom(QDog.dog)
        .fetchCount();

      Assert.assertEquals(count, 0);
    } finally {
      emf.close();
    }
  }

}
