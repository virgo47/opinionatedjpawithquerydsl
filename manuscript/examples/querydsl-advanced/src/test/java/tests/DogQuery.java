package tests;

import com.querydsl.jpa.impl.JPAQuery;
import modeladv.Dog;
import modeladv.QDog;

import javax.persistence.EntityManager;

/**
 * Arbitrarily complex query builder object.
 */
public class DogQuery {

  private static QDog DOG_ALIAS = new QDog("d1");

  private final EntityManager em;

  public DogQuery(EntityManager em) {
    this.em = em;
  }

  public JPAQuery<Dog> query() {
    return new JPAQuery<>(em)
      .select(DOG_ALIAS)
      .from(DOG_ALIAS);
  }

  public JPAQuery<Dog> nameStartsWith(String namePrefix) {
    return query()
      .where(DOG_ALIAS.name.startsWith(namePrefix));
  }
}
