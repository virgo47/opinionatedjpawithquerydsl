package tests;

import com.querydsl.core.types.Ops;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

public class ToManyFetcher<T> {

  private final List<T> rows;

  private ToManyFetcher(List<T> rows) {
    this.rows = rows;
  }

  public static <T> ToManyFetcher<T> forItems(List<T> rows) {
    return new ToManyFetcher<>(rows);
  }

  public <PK> ToManyFetcherWithIdFunction<PK> by(Function<T, PK> idFunction) {
    return new ToManyFetcherWithIdFunction<>(idFunction);
  }

  public class ToManyFetcherWithIdFunction<PK> {
    private final Function<T, PK> idFunction;

    public ToManyFetcherWithIdFunction(Function<T, PK> idFunction) {
      this.idFunction = idFunction;
    }

    public <TMC> ToManyFetcherWithFrom<TMC> from(EntityPathBase<TMC> toManyEntityPathBase)
    {
      return new ToManyFetcherWithFrom<>(toManyEntityPathBase);
    }

    public class ToManyFetcherWithFrom<TMC> {
      private final EntityPathBase<TMC> toManyEntityPathBase;
      private Path<PK> fkPath;
      private OrderSpecifier orderSpecifier;

      public ToManyFetcherWithFrom(EntityPathBase<TMC> toManyEntityPathBase)
      {
        this.toManyEntityPathBase = toManyEntityPathBase;
      }

      public ToManyFetcherWithFrom<TMC> joiningOn(Path<PK> fkPath) {
        this.fkPath = fkPath;
        return this;
      }

      public ToManyFetcherWithFrom<TMC> orderBy(OrderSpecifier orderSpecifier) {
        this.orderSpecifier = orderSpecifier;
        return this;
      }

      public <R> List<R> fetchAs(EntityManager em, BiFunction<T, List<TMC>, R> combineFunction) {
        Map<PK, List<TMC>> toManyResults = getToManyMap(em);

        return rows.stream()
          .map(row -> combineFunction.apply(row,
            toManyResults.getOrDefault(idFunction.apply(row), Collections.emptyList())))
          .collect(Collectors.toList());
      }

      public List<T> fetchAndCombine(EntityManager em,
        BiConsumer<T, List<TMC>> combiner)
      {
        Map<PK, List<TMC>> toManyResults = getToManyMap(em);

        rows.forEach(row -> combiner.accept(row,
          toManyResults.getOrDefault(idFunction.apply(row), Collections.emptyList())));

        return rows;
      }

      private Map<PK, List<TMC>> getToManyMap(EntityManager em) {
        List<PK> ids = rows.stream()
          .map(idFunction)
          .collect(Collectors.toList());
        JPAQuery<TMC> tmcQuery = new JPAQuery<>(em)
          .select(toManyEntityPathBase)
          .from(toManyEntityPathBase)
          .where(Expressions.booleanOperation(Ops.IN, fkPath, Expressions.constant(ids)));
        if (orderSpecifier != null) {
          tmcQuery.orderBy(orderSpecifier);
        }
        return tmcQuery
          .transform(groupBy(fkPath).as(list(toManyEntityPathBase)));
      }
    }
  }
}
