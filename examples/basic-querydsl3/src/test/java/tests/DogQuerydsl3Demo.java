package tests;

import com.mysema.query.jpa.impl.JPAQuery;
import modelq3.Breed;
import modelq3.Dog;
import modelq3.QDog;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class DogQuerydsl3Demo {

  public static void main(String[] args) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("demo-el");
    try {
      EntityManager em = emf.createEntityManager();
      prepareData(em);

      List<Dog> dogs = new JPAQuery(em)
        .from(QDog.dog)
        .where(QDog.dog.name.like("Re%"))
        .list(QDog.dog);

      System.out.println("\nQuerydsl: " + dogs);
    } finally {
      emf.close();
    }
  }

  public static void prepareData(EntityManager em) {
    em.getTransaction().begin();

    Breed collie = new Breed();
    collie.setName("collie");
    em.persist(collie);

    Breed germanShepherd = new Breed();
    germanShepherd.setName("german shepherd");
    em.persist(germanShepherd);

    Dog lassie = new Dog();
    lassie.setName("Lassie");
    lassie.setBreed(collie);
    em.persist(lassie);

    Dog rexo = new Dog();
    rexo.setName("Rex");
    rexo.setBreed(germanShepherd);
    em.persist(rexo);

    em.getTransaction().commit();
  }
}
