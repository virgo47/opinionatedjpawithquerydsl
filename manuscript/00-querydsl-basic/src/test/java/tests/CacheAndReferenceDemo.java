package tests;

import model00.Dog;

import javax.persistence.Cache;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public class CacheAndReferenceDemo {

  public static void main(String[] args) {
    Tools.setPrefix("EL> ");
    run("demo-el");
    Tools.setPrefix("HIB> ");
    run("demo-hib");
    Tools.printResult();
  }

  private static void run(String persistenceUnitName) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnitName);
    try {
      EntityManager em = emf.createEntityManager();
      DogQueryDemo.prepareData(em);

      System.out.println("\ngetReference");
      Dog reference = em.getReference(Dog.class, 1);
      System.out.println("\nfind");
      Map<String, Object> props = new HashMap<String, Object>();
      // TODO any hint to load "references" (objects with id only) instead of cascading?
      Dog dog = em.find(Dog.class, 1, props);
      Tools.println();

//      referenceAndCacheExperiments(emf, em);

      cacheMultiThreadExperiment(emf);
    } finally {
      emf.close();
    }
  }

  private static void cacheMultiThreadExperiment(EntityManagerFactory emf) {
    Cache cache = emf.getCache();
    Tools.println("Contains 1: " + cache.contains(Dog.class, 1));
    EntityManager em1 = emf.createEntityManager();
    EntityManager em2 = emf.createEntityManager();
    Dog dog1_1 = em1.find(Dog.class, 1);
    Tools.println("Contains 2: " + cache.contains(Dog.class, 1));
    Dog dog1_2 = em2.find(Dog.class, 1);
    Tools.println("Contains 3: " + cache.contains(Dog.class, 1));
    Tools.println("Equal objects? " + (dog1_1.equals(dog1_2)));
    Tools.println("Identical objects? " + (dog1_1 == dog1_2));

    em1.close();
    em2.close();
  }

  private static void referenceAndCacheExperiments(EntityManagerFactory emf, EntityManager em) {
    Tools.println("\nBEFORE cache evict");
    em.clear();
    Dog dog = em.find(Dog.class, 1);
    Tools.println("dog = " + dog);

    Tools.println("\nAFTER cache evict");
    emf.getCache().evictAll();
    em.clear();
    // EclipseLink: 2 selects, dog+breed
    // Hibernate: 1 select, clever enough to JOIN
    em.find(Dog.class, 1);
    dog = em.find(Dog.class, 1);
    Tools.println("dog = " + dog);

    Tools.println("\nREFERENCE");
    emf.getCache().evictAll();
    em.clear();
    // EclipseLink: This does not work lazily, but maybe with weaving it would? (2 selects)
    // Hibernate: Not lazy either out-of-the-box (1 select)
    Dog reference = em.getReference(Dog.class, 1);
    Tools.println("===");
    Tools.println(reference.getId());
    Tools.println("===");
    Tools.println(reference);
  }
}
