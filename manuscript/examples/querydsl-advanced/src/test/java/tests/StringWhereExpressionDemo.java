package tests;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAQuery;
import modeladv.*;
import org.antlr.v4.runtime.tree.ParseTree;
import vexpressedmini.VexpressedUtils;
import vexpressedmini.core.VariableResolver;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class StringWhereExpressionDemo {

  public static void main(String[] args) throws IOException {
    run("demo-el");
    run("demo-hib");
  }

  private static void run(String persistenceUnit) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnit);
    try {
      EntityManager em = emf.createEntityManager();
      prepareData(em);

      QDog d = new QDog("d");
      QBreed b = new QBreed("b");
      QLocalizedBreedName lbn = new QLocalizedBreedName("lbd");
      JPAQuery<Dog> query = new JPAQuery<>()
        .select(d)
        .from(d)
        .join(b).on(d.breedId.eq(b.id))
        .join(lbn).on(lbn.breedId.eq(b.id));

      QDog qDog = new QDog("x");
      qDog.getMetadata();
      VariableResolver variableResolver = new QueryVariableResolver(query);

      String expression = "d.name = 'Dunčo'";
      ParseTree parseTree = VexpressedUtils.createParseTree(expression);
      Predicate predicate = new QueryExpressionVisitor(variableResolver)
        .predicate(parseTree);

      List<Dog> dogs = query.clone(em)
        .where(predicate)
        .fetch();

      System.out.println("DOGS: " + dogs);
    } finally {
      emf.close();
    }
  }

  private static void prepareData(EntityManager em) {
    em.getTransaction().begin();

    new JPADeleteClause(em, QLocalizedBreedName.localizedBreedName).execute();
    new JPADeleteClause(em, QBreed.breed).execute();
    new JPADeleteClause(em, QDog.dog).execute();

    Breed collie = createBreed(em, "COL");
    em.persist(new LocalizedBreedName(collie.getId(), "en", "collie"));
    em.persist(new LocalizedBreedName(collie.getId(), "sk", "kólia"));

    Breed retriever = createBreed(em, "RTR");
    em.persist(new LocalizedBreedName(retriever.getId(), "en", "retriever"));

    Breed cuvac = createBreed(em, "CVC");
    em.persist(new LocalizedBreedName(cuvac.getId(), "sk", "slovenský čuvač"));

    Dog dunco = new Dog();
    dunco.breedId = cuvac.getId();
    dunco.name = "Dunčo";
    dunco.birthdate = LocalDate.of(1970, 1, 1);
    dunco.died = LocalDate.of(1980, 1, 1);
    em.persist(dunco);

    em.getTransaction().commit();
  }

  private static Breed createBreed(EntityManager em, String name) {
    Breed breed = new Breed();
    breed.setCode(name);
    em.persist(breed);
    em.flush();
    return breed;
  }
}
