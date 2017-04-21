package tests;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.dsl.Expressions;
import modeladv.QDog;

public class ConstantExpressionDemo {
  public static void main(String[] args) {
    System.out.println("Fluent: " + QDog.dog.name.eq("Axiom"));

    System.out.println("Expressions.constant: " +
      Expressions.booleanOperation(Ops.EQ,
        QDog.dog.name, Expressions.constant("Axiom")));

    System.out.println("ExpressionUtils.eqConst: " +
      ExpressionUtils.eqConst(QDog.dog.name, "Axiom"));
  }
}
