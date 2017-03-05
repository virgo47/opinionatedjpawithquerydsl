package tests;

import com.google.common.collect.ImmutableList;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import modeladv.Dog;
import modeladv.QDog;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PredicateBuildingDemo {

  public static void main(String[] args) {
    List<Predicate> predicates = Arrays.asList(
      Expressions.booleanTemplate("e1"),
      Expressions.booleanTemplate("e2"),
      Expressions.booleanTemplate("e3"),
      Expressions.booleanTemplate("e4"));

    Predicate emptyGroup = naiveOrGrouping(Collections.emptyList());
    Predicate nonEmptyGroup = naiveOrGrouping(predicates);
    showGroup("Naive", emptyGroup, nonEmptyGroup);

    emptyGroup = buildOrGroup(Collections.emptyList());
    nonEmptyGroup = buildOrGroup(predicates);
    showGroup("BooleanBuilder", emptyGroup, nonEmptyGroup);

    emptyGroup = naiveOrGroupingSimplified(Collections.emptyList());
    nonEmptyGroup = naiveOrGroupingSimplified(predicates);
    showGroup("Naive simplified", emptyGroup, nonEmptyGroup);

    // shows only two first args
    System.out.println("\n=== ExpressionUtils: " +
      ExpressionUtils.predicate(Ops.OR, ImmutableList.copyOf(predicates)));
    Predicate orWithNoArgs = ExpressionUtils.predicate(
      Ops.OR, ImmutableList.of());
    // System.out.println("Empty: " + orWithNoArgs); // fails on index
  }

  private static void showGroup(
    String desc, Predicate emptyGroup, Predicate nonEmptyGroup)
  {
    System.out.println("\n=== " + desc);
    System.out.println("\nNon-empty: " + nonEmptyGroup);
    System.out.println(dogQuery().where(nonEmptyGroup));

    System.out.println("\nEmpty: " + emptyGroup);
    System.out.println(dogQuery().where(emptyGroup));
    System.out.println(dogQuery()
      .where(QDog.dog.name.eq("Rex").and(emptyGroup)));
  }

  private static Predicate naiveOrGrouping(List<Predicate> predicates) {
    Predicate result = null;
    for (Predicate predicate : predicates) {
      if (result == null) {
        result = predicate;
      } else {
        result = Expressions.booleanOperation(Ops.OR, result, predicate);
      }
    }

    return result;
  }

  private static Predicate buildOrGroup(List<Predicate> predicates) {
    BooleanBuilder bb = new BooleanBuilder();
    for (Predicate predicate : predicates) {
      bb.or(predicate);
    }

    return bb;
  }

  private static Predicate naiveOrGroupingSimplified(List<Predicate> predicates) {
    Predicate result = null;
    for (Predicate predicate : predicates) {
      result = ExpressionUtils.or(result, predicate);
    }
    return result;
  }

  private static JPAQuery<Dog> dogQuery() {
    return new JPAQuery<>()
      .select(QDog.dog)
      .from(QDog.dog);
  }
}
