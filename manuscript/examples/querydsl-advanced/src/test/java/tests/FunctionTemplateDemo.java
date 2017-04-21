package tests;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQuery;
import modeladv.Dog;
import modeladv.QDog;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class FunctionTemplateDemo {

  public static void main(String[] args) throws IOException {
    run("demo-el");
    run("demo-hib"); // in contrast with random function, dayname is not a problem for Hibernate (?!)
  }

  private static void run(String persistenceUnit) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnit);
    try {
      EntityManager em = emf.createEntityManager();
      prepareData(em);

      // JPQL version
      em.createQuery(
        "select d.name, function('dayname', d.died) from Dog d ", Object[].class)
        .getResultList()
        .stream()
        .map(Arrays::toString)
        .forEach(System.out::println);

      // Querydsl version
      QDog d = QDog.dog;
      List<Tuple> result = new JPAQuery<>(em)
        .from(d)
        .select(d.name, dayname(d.died))
        .fetch();
      System.out.println("result = " + result);

    } finally {
      emf.close();
    }
  }

  private static StringTemplate dayname(Expression<LocalDate> date) {
    return Expressions.stringTemplate("FUNCTION('dayname', {0})", date);
  }

  private static void prepareData(EntityManager em) {
    em.getTransaction().begin();

    em.createQuery("delete from Dog").executeUpdate();

    Dog lassie = new Dog();
    lassie.name = "Lassie";
    lassie.birthdate = LocalDate.of(1970, 1, 1);
    lassie.died = LocalDate.of(1980, 1, 1);
    em.persist(lassie);

    Dog rexo = new Dog();
    rexo.name = "Rex";
    rexo.birthdate = LocalDate.of(2010, 1, 1);
    em.persist(rexo);

    Dog oracle = new Dog();
    oracle.name = "Oracle";
    oracle.birthdate = LocalDate.of(2010, 1, 1);
    oracle.died = LocalDate.of(2020, 1, 1);
    em.persist(oracle);

    em.getTransaction().commit();
  }
}

/*
22:55:55.733 [main] DEBUG o.h.h.i.ast.QueryTranslatorImpl - --- SQL AST ---
 \-[SELECT] QueryNode: 'SELECT'  querySpaces (Dog)
    +-[SELECT_CLAUSE] SelectClause: '{select clause}'
    |  +-[DOT] DotNode: 'dog0_.name' {propertyName=name,dereferenceType=PRIMITIVE,getPropertyPath=name,path=d.name,tableAlias=dog0_,className=modeladv.Dog,classAlias=d}
    |  |  +-[ALIAS_REF] IdentNode: 'dog0_.id' {alias=d, className=modeladv.Dog, tableAlias=dog0_}
    |  |  \-[IDENT] IdentNode: 'name' {originalText=name}
    |  +-[SELECT_COLUMNS] SqlNode: ' as col_0_0_'
    |  +-[METHOD_CALL] MethodNode: 'function (dayname)'
    |  |  +-[METHOD_NAME] IdentNode: 'dayname' {originalText=dayname}
    |  |  \-[EXPR_LIST] SqlNode: 'exprList'
    |  |     \-[DOT] DotNode: 'dog0_.died' {propertyName=died,dereferenceType=PRIMITIVE,getPropertyPath=died,path=d.died,tableAlias=dog0_,className=modeladv.Dog,classAlias=d}
    |  |        +-[ALIAS_REF] IdentNode: 'dog0_.id' {alias=d, className=modeladv.Dog, tableAlias=dog0_}
    |  |        \-[IDENT] IdentNode: 'died' {originalText=died}
    |  \-[SELECT_COLUMNS] SqlNode: ' as col_1_0_'
    \-[FROM] FromClause: 'from' FromClause{level=1, fromElementCounter=1, fromElements=1, fromElementByClassAlias=[d], fromElementByTableAlias=[dog0_], fromElementsByPath=[], collectionJoinFromElementsByPath=[], impliedElements=[]}
       \-[FROM_FRAGMENT] FromElement: 'Dog dog0_' FromElement{explicit,not a collection join,not a fetch join,fetch non-lazy properties,classAlias=d,role=null,tableName=Dog,tableAlias=dog0_,origin=null,columns={,className=modeladv.Dog}}

 */