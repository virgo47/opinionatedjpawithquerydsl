package tests;

import com.querydsl.core.types.dsl.DateExpression;
import com.querydsl.jpa.impl.JPAQuery;
import modeladv.Dog;
import modeladv.QDog;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class DateExpressionFunction {

  public static void main(String[] args) throws IOException {
    run("demo-el");
    run("demo-hib");
  }

  private static void run(String persistenceUnit) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnit);
    try {
      EntityManager em = emf.createEntityManager();
      prepareData(em);

      QDog d = QDog.dog;
      List<Dog> liveDogs = new JPAQuery<Dog>(em)
        .select(d)
        .from(d)
        // client-side option
//        .where(d.birthdate.before(LocalDate.now()))
//        .where(d.died.after(LocalDate.now()))

        // using function on the database side
        .where(d.birthdate.before(DateExpression.currentDate(LocalDate.class))
          .and(d.died.after((DateExpression.currentDate(LocalDate.class)))
            .or(d.died.isNull())))
        .fetch();

      System.out.println("\nLIVE DOGS = " + liveDogs);
    } finally {
      emf.close();
    }
  }

  private static void prepareData(EntityManager em) {
    em.getTransaction().begin();

    em.createQuery("delete from Dog").executeUpdate();

    Dog lassie = new Dog();
    lassie.name = "Lassie";
    lassie.birthdate = LocalDate.of(1970, 1, 1);
    lassie.died = LocalDate.of(1980, 1, 1);
    em.persist(lassie);

    Dog rexo = new Dog();
    rexo.name = "Rex";
    rexo.birthdate = LocalDate.of(2010, 1, 1);
    em.persist(rexo);

    Dog oracle = new Dog();
    oracle.name = "Oracle";
    oracle.birthdate = LocalDate.of(2010, 1, 1);
    // there are books over 14 years old and still relevant, but I doubt this one is one of them
    oracle.died = LocalDate.of(2030, 1, 1);
    em.persist(oracle);

    em.getTransaction().commit();
  }
}
