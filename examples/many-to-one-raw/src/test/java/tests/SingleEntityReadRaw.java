package tests;

import com.querydsl.jpa.impl.JPAQuery;
import modeltoone.Breed;
import modeltoone.Dog;
import modeltoone.QBreed;
import modeltoone.QDog;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class SingleEntityReadRaw {

  public static void main(String[] args) {
    run("demo-el");
    run("demo-hib");
  }

  private static void run(String persistenceUnitName) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnitName);
    try {
      EntityManager em = emf.createEntityManager();
      prepareData(em);
      emf.getCache().evictAll();

      System.out.println("\nfind");
      Dog dog = em.find(Dog.class, 1);

      System.out.println("\ntraversing");
      Breed breed = em.find(Breed.class, dog.getBreedId());
      while (breed.getDerivedFromId() != null) {
        breed = em.find(Breed.class, breed.getDerivedFromId());
      }
      System.out.println("breed = " + breed.getName());

      // This is invalid JPQL according to spec, because LEFT JOIN must be followed by
      // an association path
      List<Dog> dogs = em.createQuery(
        "select dog from Dog dog left join Breed breed on breed.id = dog.breedId" +
        " where breed.name like '%ll%'", Dog.class).getResultList();
      System.out.println("dogs = " + dogs);

      querydslDemo(em);
    } finally {
      emf.close();
    }
  }

  private static void querydslDemo(EntityManager em) {
    // Does not work on Hibernate - because of WITH generated instead of ON?
    JPAQuery<Dog> query = new JPAQuery<Dog>(em)
      .select(QDog.dog)
      .from(QDog.dog)
      .leftJoin(QBreed.breed).on(QBreed.breed.id.eq(QDog.dog.breedId))
      .where(QBreed.breed.name.contains("ll"));
    System.out.println("query = " + query);
    List<Dog> dogs = query
      .fetch();
    System.out.println("dogs = " + dogs);
  }

  private static void prepareData(EntityManager em) {
    em.getTransaction().begin();
    Breed wolf = new Breed();
    wolf.setName("wolf");
    em.persist(wolf);
    em.flush();

    Breed germanShepherd = new Breed();
    germanShepherd.setName("german shepherd");
    germanShepherd.setDerivedFromId(wolf.getId());
    em.persist(germanShepherd);
    em.flush();

    Breed collie = new Breed();
    collie.setName("collie");
    collie.setDerivedFromId(germanShepherd.getId());
    em.persist(collie);
    em.flush();

    Dog lassie = new Dog();
    lassie.setName("Lassie");
    lassie.setBreedId(collie.getId());
    em.persist(lassie);

    em.getTransaction().commit();
    em.clear();
  }
}
