package tests;

import com.querydsl.core.JoinExpression;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Operation;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.dsl.CollectionPathBase;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.util.ReflectionUtils;
import com.querydsl.jpa.impl.JPAQuery;
import vexpressedmini.core.UnknownVariable;
import vexpressedmini.core.VariableResolver;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.stream.Collectors;

public class QueryVariableResolver implements VariableResolver {

  private final Map<String, EntityPathBase<?>> aliases;

  public QueryVariableResolver(JPAQuery<?> query) {
    aliases = query.getMetadata().getJoins().stream()
      .map(this::joinsTargetExpression)
      .collect(Collectors.toMap(j -> j.getMetadata().getName(), j -> j));
  }

  /**
   * Resolves join's target which in case of {@link Operation} of type {@link Ops#ALIAS}
   * is taken from operation's second argument.
   */
  private EntityPathBase<?> joinsTargetExpression(JoinExpression join) {
    Expression<?> target = join.getTarget();
    if (target instanceof Operation) {
      Operation<?> operation = (Operation) target;
      if (operation.getOperator().equals(Ops.ALIAS)) {
        target = operation.getArg(1);
      }
    }
    return (EntityPathBase<?>) target;
  }


  @Override
  public Expression resolve(String variableName) throws UnknownVariable {
    String[] sa = variableName.split("\\.", 2);
    EntityPathBase<?> entityPathBase = aliases.get(sa[0]);
    return getExpression(entityPathBase, sa[1].split("\\."));
  }

  private static Expression getExpression(Expression<?> expression, String[] attributePath) {
    for (String attribute : attributePath) {
      expression = collectionPathToAny(expression);
      expression = getFieldExpression(expression, attribute);
    }

    return expression;
  }

  private static Expression<?> getFieldExpression(Expression<?> expression, String attribute) {
    Field field = ReflectionUtils.getFieldOrNull(expression.getClass(), attribute);
    assert field != null;
    try {
      return (Expression<?>) field.get(expression);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  private static Expression<?> collectionPathToAny(Expression<?> expression) {
    if (expression instanceof CollectionPathBase) {
      expression = ((CollectionPathBase) expression).any();
    }
    return expression;
  }
}
