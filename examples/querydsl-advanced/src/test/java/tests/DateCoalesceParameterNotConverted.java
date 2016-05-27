package tests;

import com.querydsl.core.types.dsl.Param;
import com.querydsl.jpa.impl.JPAQuery;
import modeladv.Dog;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static modeladv.QDog.dog;

public class DateCoalesceParameterNotConverted {

  public static void main(String[] args) throws IOException {
    run("demo-el");
    run("demo-hib");
  }

  private static void run(String persistenceUnit) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnit);
    try {
      EntityManager em = emf.createEntityManager();
      prepareData(em);


      // On h2 fails whenever dog with null died is encountered
      // On JTDS fails before executing
      try {
        em.createQuery(
          "select d from Dog d where coalesce(d.died, :date) > d.birthdate", Dog.class)
          .setParameter("date", LocalDate.now())
          .getResultList();
      } catch (Exception e) {
        e.printStackTrace();
      }

      // the same problem
      try {
        Param<LocalDate> SOME_DATE = new Param<>(LocalDate.class);
        List<Dog> funnyDogs = new JPAQuery<Dog>(em)
          .select(dog)
          .from(dog)
          .where(dog.died.coalesce(SOME_DATE).asDate().after(dog.birthdate))

//         IllegalArgumentException: You have attempted to set a value of type class java.sql.Date for parameter 1...
//        .set(SOME_DATE, new Date(System.currentTimeMillis()))
          .set(SOME_DATE, LocalDate.now()) // can be any date, cannot replace it with DateExpression.currentDate()
          .fetch();
      } catch (Exception e) {
        e.printStackTrace();
      }
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
