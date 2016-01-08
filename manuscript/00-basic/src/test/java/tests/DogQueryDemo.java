package tests;

import com.querydsl.jpa.impl.JPAQueryFactory;
import model.Breed;
import model.Dog;
import model.QDog;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class DogQueryDemo {

  public static void main(String[] args) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("sqldemo");
    try {
      EntityManager em = emf.createEntityManager();

      prepareData(em);
      jpqlVsQuerydslDemo(em);
    } finally {
      emf.close();
    }
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
