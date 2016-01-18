package tests;

import com.querydsl.jpa.impl.JPAQuery;
import model00.Breed;
import model00.Dog;
import model00.QDog;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class UpdateWithWrappedFkDemo {

  public static void main(String[] args) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("sqldemo");
    try {
      EntityManager em = emf.createEntityManager();
      DogQueryDemo.prepareData(em);

      em.getTransaction().begin();
      Dog lessie = findDogByName(em, "Lassie");
      System.out.println("\nHere the breed is already in session\n");

      Breed collie = lessie.getBreed();
      System.out.println("collie = " + collie);

      Dog rex = findDogByName(em, "Rex");

      Breed collieGhost = new Breed();
      collieGhost.setId(collie.getId());
      rex.setBreed(collieGhost);

      em.getTransaction().commit();

      Dog newRex = findDogByName(em, "Rex");
      System.out.println("New Rex: " + newRex);
      System.out.println("Rex's breed: " + newRex.getBreed());
      // here the breed.name is null, let's refresh
      em.refresh(newRex);
      System.out.println("Rex's breed: " + newRex.getBreed());
    } finally {
      emf.close();
    }
  }

  private static Dog findDogByName(EntityManager em, String name) {
    QDog d = QDog.dog;
    return new JPAQuery<Dog>(em).select(d)
          .from(d)
          .where(d.name.eq(name))
          .fetchOne();
  }
}
