package tests;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import nplusone.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

public class OneToManyPagination {

  public static void main(String[] args) {
    run("demo-el");
    run("demo-hib");
  }

  private static void run(String persistenceUnitName) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnitName);
    try {
      EntityManager em = emf.createEntityManager();
      NPlusOne.prepareData(em);

      naivePaginationWithNPlusOne(em);
      incorrectPaginationWithFetchJoin(em);
      incorrectJoinPagination(em);
      fullJoinedResult(em);
      joinedAndDistinctPaginated(em);
      nativeQueryPagination(em);
      twoJoinsFirstIdsOnly(em);
      relationshipFetcherPagination(em);

      em.close();
    } finally {
      emf.close();
    }
  }

  private static void naivePaginationWithNPlusOne(EntityManager em) {
    NPlusOne.clear(em);

    List<Owner> owners = new JPAQuery<>(em)
      .select(QOwner.owner)
      .from(QOwner.owner)
      .orderBy(QOwner.owner.name.asc())
      .offset(1)
      .limit(2)
      .fetch();
    for (Owner owner : owners) {
      owner.getDogs().isEmpty(); // to assure lazy load
      System.out.println(owner.getName() + "'s dogs = " + owner.getDogs());
    }
  }

  private static void incorrectPaginationWithFetchJoin(EntityManager em) {
    NPlusOne.clear(em);

    List<Owner> owners = new JPAQuery<>(em)
      .select(QOwner.owner)
      .from(QOwner.owner)
      .leftJoin(QOwner.owner.dogs).fetchJoin()
      .orderBy(QOwner.owner.name.asc())
      .offset(1)
      .limit(2)
      .fetch();
    for (Owner owner : owners) {
      owner.getDogs().isEmpty(); // to assure lazy load
      System.out.println(owner.getName() + "'s dogs = " + owner.getDogs());
    }
  }

  private static void incorrectJoinPagination(EntityManager em) {
    NPlusOne.clear(em);

    QOwnerRaw o = QOwnerRaw.ownerRaw;
    QDogRaw d = QDogRaw.dogRaw;
    List<Tuple> results = new JPAQuery<>(em)
      .select(o, d)
      .from(o)
      .leftJoin(d).on(d.ownerId.eq(o.id))
      .orderBy(o.name.asc(), d.name.asc())
      .offset(1)
      .limit(2)
      .fetch();
    for (Tuple row : results) {
      DogRaw dog = row.get(d);
      System.out.println(row.get(o).getName() + ", " + (dog != null ? dog.getName() : null));
    }

    List<OwnerRaw> owners = new JPAQuery<>(em)
      .select(o)
      .from(o)
      .leftJoin(d).on(d.ownerId.eq(o.id))
      .orderBy(o.name.asc(), d.name.asc())
      .offset(1)
      .limit(2)
      .fetch();
    owners.forEach(System.out::println);
  }

  private static void fullJoinedResult(EntityManager em) {
    NPlusOne.clear(em);

    QOwnerRaw o = QOwnerRaw.ownerRaw;
    QDogRaw d = QDogRaw.dogRaw;
    List<Tuple> results = new JPAQuery<>(em)
      .select(o.name, d.name)
      .from(o)
      .orderBy(o.name.asc(), d.name.asc())
      .leftJoin(d).on(d.ownerId.eq(o.id))
      .fetch();

    System.out.println("| owner | dog |\n|-------|-----|");
    for (Tuple row : results) {
      System.out.println("| " + row.get(o.name) + " | " + row.get(d.name) + " |");
    }
    System.out.println();
  }

  private static void joinedAndDistinctPaginated(EntityManager em) {
    NPlusOne.clear(em);

    QOwnerRaw o = QOwnerRaw.ownerRaw;
    QDogRaw d = QDogRaw.dogRaw;
    List<OwnerRaw> owners = new JPAQuery<>(em)
      .select(o)
      .distinct()
      .from(o)
      .leftJoin(d).on(d.ownerId.eq(o.id))
      .where(d.isNotNull())
      .orderBy(o.name.asc())
      .offset(1)
      .limit(2)
      .fetch();

    // should return owners 3 and 4 because of the where condition
    System.out.println("distinct owners (with join) = " + owners);

    owners = new JPAQuery<>(em)
      .select(o)
      .from(o)
      .where(new JPAQuery<>()
        .select(d)
        .from(d)
        .where(d.ownerId.eq(o.id))
        .exists())
      .orderBy(o.name.asc())
      .offset(1)
      .limit(2)
      .fetch();
    System.out.println("owners (where in subquery) = " + owners);
  }

  private static void nativeQueryPagination(EntityManager em) {
    NPlusOne.clear(em);

    // select * would be enough for EclipseLink
    // Hibernate complains about duplicated sql alias for ID (and would also about NAME)
    Query nativeQuery = em.createNativeQuery(
//      "SELECT o.name, d.name " + // still not good enough for Hibernate
      "SELECT o.name AS oname, d.name AS dname" +
        " FROM (SELECT * FROM owner LIMIT 2 OFFSET 1) o" +
        " LEFT JOIN dog d ON o.id=d.owner_id");
    List<Object[]> resultList = nativeQuery.getResultList();

    System.out.println("resultList = " + resultList.stream()
      .map(row -> Arrays.toString(row))
      .collect(Collectors.toList()));
  }

  private static void twoJoinsFirstIdsOnly(EntityManager em) {
    NPlusOne.clear(em);

    QOwnerRaw o = QOwnerRaw.ownerRaw;
    List<Integer> ownerIds = new JPAQuery<>(em)
      .select(o.id)
      .from(o)
      .orderBy(o.name.asc())
      // WHERE ad lib here
      .offset(1)
      .limit(2)
      .fetch();

    /*
    // If join across to-many is used, distinct is needed and things get complicated in select.
    List<Integer> ownerIds = new JPAQuery<>(em)
      // If distinct is used SQL requires o.name here because it is used to
      // order which happens after distinct. EclipseLink can handle this,
      // Hibernate generated SQL fails. JPA spec is not specific on this.
      .select(o.id, o.name)
      // .select(o) // probably better, implies o.name as well
      .distinct() // needed if join across to-many is used to allow WHERE
      .from(o)
      .orderBy(o.name.asc())
      // WHERE ad lib here
      .offset(1)
      .limit(2)
      .fetch()
      .stream()
      .map(t -> t.get(o.id)) // or .map(OwnerRaw::getId) if we selected "o"
      .collect(Collectors.toList());
    */

    QDogRaw d = QDogRaw.dogRaw;
    Map<OwnerRaw, List<DogRaw>> ownerDogs = new JPAQuery<>(em)
      .select(o, d)
      .from(o)
      .leftJoin(d).on(d.ownerId.eq(o.id))
      .where(o.id.in(ownerIds))
      .orderBy(o.name.asc()) // use the same order as in select #1
      .orderBy(d.name.desc()) // dogs in each list ordered DESC
      // no limit/offset, where took care of it
      .transform(groupBy(o).as(list(d)));
    System.out.println("ownerDogs = " + ownerDogs);
  }

  private static void relationshipFetcherPagination(EntityManager em) {
    NPlusOne.clear(em);

    QOwnerRaw o = QOwnerRaw.ownerRaw;
    List<OwnerRaw> owners = new JPAQuery<>(em)
      .select(o)
      .from(o)
      .orderBy(o.name.asc())
      .offset(1)
      .limit(2)
      .fetch();

    QDogRaw d = QDogRaw.dogRaw;
    List<OwnerWithDogs> ownersWithDogs = ToManyFetcher.forItems(owners)
      .by(OwnerRaw::getId)
      .from(d)
      .joiningOn(d.ownerId)
      .orderBy(d.name.desc())
      .fetchAs(em, OwnerWithDogs::new);

    System.out.println("ownersWithDogs = " + ownersWithDogs);
  }

  static class OwnerWithDogs {
    public final OwnerRaw owner;
    public final List<DogRaw> dogs;

    OwnerWithDogs(OwnerRaw owner, List<DogRaw> dogs) {
      this.owner = owner;
      this.dogs = dogs;
    }

    @Override public String toString() {
      return "OwnerWithDogs{" +
        "owner=" + owner +
        ", dogs=" + dogs +
        '}';
    }
  }
}
