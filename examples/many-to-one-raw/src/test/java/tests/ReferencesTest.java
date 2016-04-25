package tests;

import modeltoone.Breed;
import modeltoone.Dog;
import org.assertj.core.api.Assertions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import support.ReferenceChecker;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ReferencesTest {

  private EntityManagerFactory emf;
  private Dog lessie;

  @BeforeMethod
  public void prepareData() {
    emf = Persistence.createEntityManagerFactory("demo-el");
    EntityManager em = emf.createEntityManager();
    em.getTransaction().begin();

    Breed collie = new Breed();
    collie.setName("collie");
    em.persist(collie);
    em.flush(); // needed to really get ID

    lessie = new Dog();
    lessie.setBreedId(collie.getId());
    em.persist(lessie);

    em.getTransaction().commit();
    em.close();
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
      new ReferenceChecker(em).checkReferences(lessie);
    } finally {
      em.close();
    }
  }

  @Test
  public void referencesCheckerThrowsExceptionForNonexistentId() {
    EntityManager em = emf.createEntityManager();
    try {
      lessie.setBreedId(-1);
      Assertions.assertThatThrownBy(() -> new ReferenceChecker(em).checkReferences(lessie))
        .hasMessage("Reference not found for class modeltoone.Breed and id=-1");
    } finally {
      em.close();
    }
  }
}
