package tests.exprdemo;

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
import java.util.stream.Stream;

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

      DogQuery dogQuery = new DogQuery(em);
      Stream
        .of(
          "d.name = 'Dunčo'",
          "lbn.langCode = 'sk' and lbn.name like '%a%'")
        .forEach(e -> System.out.println("EXPRESSION: " + e + "\n> " + dogQuery.list(e) + "\n\n"));

    } finally {
      emf.close();
    }
  }

  static class DogQuery {

    private final QDog dog = new QDog("d");
    private final QBreed breed = new QBreed("b");
    private final QLocalizedBreedName localizedBreedName = new QLocalizedBreedName("lbn");

    /**
     * This query does NOT allow to select dog and localized name for missing language,
     * for that we would need ON lang='...' as well in that left join.
     */
    private final JPAQuery<?> query = new JPAQuery<>()
      .from(dog)
      .leftJoin(breed).on(dog.breedId.eq(breed.id))
      .leftJoin(localizedBreedName).on(
        localizedBreedName.breedId.eq(breed.id));

    private final VariableResolver variableResolver = new QueryVariableResolver(query);

    private final EntityManager em;

    DogQuery(EntityManager em) {
      this.em = em;
    }

    public List<Dog> list(String expression) {
      ParseTree parseTree = VexpressedUtils.createParseTree(expression);
      Predicate predicate = new QueryExpressionVisitor(variableResolver)
        .predicate(parseTree);

      return query.clone(em)
        .where(predicate)
        .select(dog)
        .fetch();
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

    Dog goldie = new Dog();
    goldie.breedId = retriever.getId();
    goldie.name = "Goldie";
    em.persist(goldie);

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
