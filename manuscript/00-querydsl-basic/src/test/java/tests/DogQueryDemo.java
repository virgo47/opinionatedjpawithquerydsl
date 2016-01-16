package tests;

import com.querydsl.core.types.dsl.Param;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import model00.Breed;
import model00.Dog;
import model00.Dog_;
import model00.QDog;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class DogQueryDemo {

  public static void main(String[] args) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("sqldemo");
    try {
      EntityManager em = emf.createEntityManager();
      prepareData(em);

      // pure JPA
      jpqlDemo(em);
      criteriaDemo(em);
      typedCriteriaDemo(em);

      // querydsl options
      querydslDemo(em);
      querydslDemoWithFactory(em);
      querydslWithAliasDemo(em);
      querydslWithDetachedQuery(em);

//      referenceAndCacheExperiments(emf, em);
    } finally {
      emf.close();
    }
  }

  private static void referenceAndCacheExperiments(EntityManagerFactory emf, EntityManager em) {
    System.out.println("\nBEFORE cache evict");
    em.clear();
    Dog dog = em.find(Dog.class, 1);
    System.out.println("dog = " + dog);

    System.out.println("\nAFTER cache evict");
    emf.getCache().evictAll();
    em.clear();
    // EclipseLink: 2 selects, dog+breed
    // Hibernate: 1 select, clever enough to JOIN
    em.find(Dog.class, 1);
    dog = em.find(Dog.class, 1);
    System.out.println("dog = " + dog);

    System.out.println("\nREFERENCE");
    emf.getCache().evictAll();
    em.clear();
    // EclipseLink: This does not work lazily, but maybe with weaving it would? (2 selects)
    // Hibernate: Not lazy either out-of-the-box (1 select)
    Dog reference = em.getReference(Dog.class, 1);
    System.out.println("===");
    System.out.println(reference.getId());
    System.out.println("===");
    System.out.println(reference);
  }

  private static void jpqlDemo(EntityManager em) {
    List<Dog> dogs = em.createQuery(
      "select d from Dog d where d.name like :name", Dog.class)
      .setParameter("name", "Re%")
      .getResultList();

    System.out.println("\nJPQL: " + dogs);
  }

  private static void criteriaDemo(EntityManager em) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Dog> query = cb.createQuery(Dog.class);
    Root<Dog> dog = query.from(Dog.class);
    query.select(dog)
      .where(cb.like(dog.<String>get("name"), "Re%"));
    List<Dog> dogs = em.createQuery(query)
      .getResultList();

    System.out.println("\nCriteria: " + dogs);
  }

  private static void typedCriteriaDemo(EntityManager em) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Dog> query = cb.createQuery(Dog.class);
    Root<Dog> dog = query.from(Dog.class);
    query.select(dog)
      // this is actually the only place where we can use metamodel in this example
      .where(cb.like(dog.get(Dog_.name), "Re%"));
    // .where(cb.equal(dog.get(Dog_.id), "x")) but type-safety does NOT cover this id.eq(string)
    List<Dog> dogs = em.createQuery(query)
      .getResultList();

    System.out.println("\nTyped Criteria: " + dogs);

  }

  private static void querydslDemo(EntityManager em) {
    List<Dog> dogs = new JPAQuery<Dog>(em)
      .select(QDog.dog)
      .from(QDog.dog)
      .where(QDog.dog.name.like("Re%"))
//    .where(QDog.dog.name.startsWith("Re")) // communicates the intention even better
      .fetch();

    System.out.println("\nQuerydsl: " + dogs);
  }

  private static void querydslDemoWithFactory(EntityManager em) {
    List<Dog> dogs = new JPAQueryFactory(em)
      .select(QDog.dog)
      .from(QDog.dog)
      .where(QDog.dog.name.startsWith("Re"))
      .fetch();

    System.out.println("\nQuerydsl with JPAQueryFactory: " + dogs);
  }

  private static void querydslWithAliasDemo(EntityManager em) {
    QDog d = new QDog("d1");
    List<Dog> dogs = new JPAQuery<Dog>(em)
      .select(d)
      .from(d)
      .where(d.name.startsWith("Re"))
      .fetch();

    System.out.println("\nQuerydsl with alias: " + dogs);
  }

  private static QDog DOG_ALIAS = new QDog("d1");
  private static Param<String> DOG_NAME_PREFIX = new Param<String>(String.class);
  private static JPAQuery<Dog> DOG_QUERY = new JPAQuery<Dog>()
    .select(DOG_ALIAS)
    .from(DOG_ALIAS)
    .where(DOG_ALIAS.name.startsWith(DOG_NAME_PREFIX));

  private static void querydslWithDetachedQuery(EntityManager em) {
    List<Dog> dogs = DOG_QUERY.clone(em)
      .set(DOG_NAME_PREFIX, "Re")
      .fetch();

    System.out.println("\nQuerydsl with detached query: " + dogs);
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
}
