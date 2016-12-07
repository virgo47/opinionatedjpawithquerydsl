package tests;

import modelenum.Dog;
import modelenum.Gender;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.assertj.core.api.Assertions.assertThat;

public class EnumConversionTest {

  private EntityManagerFactory emf;

  @BeforeMethod
  public void prepareData() {
    emf = Persistence.createEntityManagerFactory("demo-el");
    EntityManager em = emf.createEntityManager();
    em.getTransaction().begin();

    Dog dog = new Dog();
    dog.setName("Lassie");
    dog.setGender(Gender.FEMALE);
    em.persist(dog);

    dog = new Dog();
    dog.setName("Genderless");
    em.persist(dog);

    em.getTransaction().commit();
    em.close();
    emf.getCache().evictAll();
  }

  @AfterMethod
  public void closeEntityManagerFactory() {
    if (emf != null) {
      emf.close();
    }
  }

  @Test
  public void referencesCheckerFindsExistingId() {
    EntityManager em = emf.createEntityManager();
    try {
      Dog dog = em.find(Dog.class, 1);
      assertThat(dog.getGender()).isEqualTo(Gender.FEMALE);

      dog = em.find(Dog.class, 2);
      assertThat(dog.getGender()).isNull();
    } finally {
      em.close();
    }
  }
}
