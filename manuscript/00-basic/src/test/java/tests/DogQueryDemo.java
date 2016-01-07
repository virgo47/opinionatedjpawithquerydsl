package tests;

import com.querydsl.jpa.impl.JPAQueryFactory;
import model.Breed;
import model.Dog;
import model.Person;
import model.QDog;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashSet;
import java.util.List;

public class DogQueryDemo {

  public static void main(String[] args) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("sqldemo");
    try {
      EntityManager em = emf.createEntityManager();

      prepareData(em);
      jpqlVsQuerydslDemo(em);

      testManyToManyOnEntityWithCompositeKey(em);
    } finally {
      emf.close();
    }
  }

  private static void testManyToManyOnEntityWithCompositeKey(EntityManager em) {
    Person owner = new Person();
    owner.setId(1, 1);
    List<Dog> dogs = new JPAQueryFactory(em)
      .select(QDog.dog).from(QDog.dog)
      .orderBy(QDog.dog.id.asc())
      .fetch();
    owner.setDogs(new HashSet<Dog>(dogs));

    em.getTransaction().begin();
    em.persist(owner);
    em.getTransaction().commit();

    em.getTransaction().begin();
    owner.getDogs().remove(dogs.get(0)); // remove one of the dogs
    owner = em.merge(owner);
    em.getTransaction().commit();

    System.out.println("owner = " + owner);
  }

  private static void prepareData(EntityManager em) {
    em.getTransaction().begin();

    Breed collie = new Breed();
    collie.setName("collie");
    em.persist(collie);

    Dog lassie = new Dog();
    lassie.setName("Lassie");
    lassie.setBreed(collie);
    em.persist(lassie);

    Dog rexo = new Dog();
    rexo.setName("Rexo");
    rexo.setBreed(collie);
    em.persist(rexo);

    em.getTransaction().commit();
  }

  private static void jpqlVsQuerydslDemo(EntityManager em) {
    // JPA (JPQL)
    List<Dog> dogs = em.createQuery(
      "select d from Dog d where d.name like :name", Dog.class)
      .setParameter("name", "Re%").getResultList();
    System.out.println("dogs = " + dogs);

// Querydsl 4
    List<Dog> dogs2 = new JPAQueryFactory(em)
      .select(QDog.dog)
      .from(QDog.dog)
      .where(QDog.dog.name.startsWith("Re"))
      .fetch();
    System.out.println("dogs2 = " + dogs2);
  }
}
