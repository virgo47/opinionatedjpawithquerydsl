import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import modelon.Breed;
import modelon.BreedLocalizedName;
import modelon.QBreed;
import modelon.QBreedLocalizedName;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class OnDemonstration {

  public static void main(String[] args) {
    run("demo-el");
    run("demo-hib");
  }

  private static void run(String persistenceUnitName) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnitName);
    try {
      EntityManager em = emf.createEntityManager();
      prepareData(em);

      QBreedLocalizedName qbn = QBreedLocalizedName.breedLocalizedName;
      List<Tuple> breedEnNames = new JPAQuery<>(em)
        .select(QBreed.breed.id, QBreed.breed.code, qbn.name)
        .from(QBreed.breed)
        .join(QBreed.breed.names, qbn)
        .where(qbn.language.eq("en"))
        .fetch();
      System.out.println("\nINNER JOIN and WHERE: breedEnNames = " + breedEnNames);

      breedEnNames = new JPAQuery<>(em)
        .select(QBreed.breed.id, QBreed.breed.code, qbn.name)
        .from(QBreed.breed)
        .leftJoin(QBreed.breed.names, qbn)
        .where(qbn.language.eq("en"))
        .fetch();
      System.out.println("\nLEFT JOIN, WHERE instead of ON: breedEnNames = " + breedEnNames);

      breedEnNames = new JPAQuery<>(em)
        .select(QBreed.breed.id, QBreed.breed.code, qbn.name)
        .from(QBreed.breed)
        .leftJoin(QBreed.breed.names, qbn).on(qbn.language.eq("en"))
        .fetch();
      System.out.println("\nLEFT JOIN with ON: breedEnNames = " + breedEnNames);

    } finally {
      emf.close();
    }
  }


  private static void prepareData(EntityManager em) {
    em.getTransaction().begin();
    Breed wolf = new Breed();
    wolf.setCode("WLF");
    em.persist(wolf);

    em.persist(new BreedLocalizedName(wolf, "sk", "vlk"));
    em.persist(new BreedLocalizedName(wolf, "en", "wolf"));

    Breed collie = new Breed();
    collie.setCode("COL");
    em.persist(collie);

    em.persist(new BreedLocalizedName(collie, "sk", "kólia"));

    em.getTransaction().commit();
    em.clear();
  }
}
