package tests;

import com.querydsl.jpa.impl.JPAQuery;
import model00.Dog;
import model00.Person;
import model00.QPerson;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashSet;
import java.util.List;

public class ToManyPaginatingProblem {

  public static void main(String[] args) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("demo-hib");
    try {
      EntityManager em = emf.createEntityManager();
      prepareData(em);

      System.out.println("page 1, no join: " + listPeople(em, 0, 2, false));
      System.out.println("page 2, no join: " + listPeople(em, 2, 2, false));

      System.out.println("page 1, join: " + listPeople(em, 0, 2, true));
      System.out.println("page 2, join: " + listPeople(em, 2, 2, true));

    } finally {
      emf.close();
    }
  }

  private static List<String> listPeople(EntityManager em, int offset, int limit, boolean join) {
    QPerson p = QPerson.person;
    JPAQuery<String> query = new JPAQuery<Person>(em)
      .select(p.name)
      .from(p)
      .orderBy(p.name.asc())
      .offset(offset).limit(limit);
    if (join) {
      query.leftJoin(p.dogs);
    }
    return query.fetch();
  }

  public static void prepareData(EntityManager em) {
    em.getTransaction().begin();
    Person joe = person(em, "Joe", "Rex");
    Person mike = person(em, "Mike", "Lassie", "Dunco");
    Person charlie = person(em, "Charlie");
    Person alan = person(em, "Alan", "Alan", "Beastie", "Cessna");
    em.getTransaction().commit();
  }

  private static Person person(EntityManager em, String name, String... dogNames) {
    Person person = new Person();
    person.setName(name);
    person.setUniqId(name.hashCode());
    person.setDogs(new HashSet<Dog>());

    for (String dogName : dogNames) {
      Dog dog = new Dog();
      dog.setName(dogName);
      em.persist(dog);
      person.getDogs().add(dog);
    }
    em.persist(person);

    return person;
  }
}
