package demos;

import com.querydsl.jpa.impl.JPAQuery;
import model00.Person;
import model00.QDog;
import model00.QPerson;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class LeftJoinWithAliasAndOnDemo {

  public static void main(String[] args) {
    run("demo-el"); // EclipseLink drops part of the ON condition for many-to-many (BUG)
    // select person from Person person   left join person.dogs as dog on dog.name = ?1
    // SELECT t1.ID, t1.NAME, t1.UNIQID
    // FROM {oj PERSON t1 LEFT OUTER JOIN (Person_Dog t2 JOIN DOG t0 ON (t0.ID = t2.dog_id))
    // ON (t2.puid = t1.UNIQID)}  -- and where is ON condition for dog's name?!


    run("demo-hib"); // Hibernate is OK
    // HQL: select person
    // from model00.Person person
    // left join person.dogs as dog with dog.name = ?1

    // select person0_.id as id1_3_, person0_.name as name2_3_, person0_.uniqId as uniqId3_3_
    // from Person person0_
    // left outer join Person_Dog dogs1_ on person0_.uniqId=dogs1_.puid
    // left outer join Dog dog2_ on dogs1_.dog_id=dog2_.id and (dog2_.name=?)
  }

  private static void run(String persistenceUnitName) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnitName);
    try {
      EntityManager em = emf.createEntityManager();
      DogQueryDemo.prepareData(em);
      emf.getCache().evictAll();

      List<Person> owners = new JPAQuery<>(em)
        .select(QPerson.person)
        .from(QPerson.person)
        .leftJoin(QPerson.person.dogs, QDog.dog).on(QDog.dog.name.eq("Rex"))
        .fetch();

      // The same with JPQL
//      List<Person> owners = em.createQuery(
//        "select p from Person p left join p.dogs d on d.name=:name", Person.class)
//        .setParameter("name", "Rex")
//        .getResultList();

      System.out.println("\nresults: " + owners);
    } finally {
      emf.close();
    }
  }
}
