package support;

import javax.persistence.AttributeConverter;
import java.util.function.Function;

/** Base implementation for converting values in DB, not only for enums. */
public abstract class AbstractAttributeConverter<X, Y>
  implements AttributeConverter<X, Y>
{
  private final Function<X, Y> toDbFunction;
  private final Function<Y, X> fromDbFunction;

  public AbstractAttributeConverter(
    Function<X, Y> toDbFunction, Function<Y, X> fromDbFunction)
  {
    this.toDbFunction = toDbFunction;
    this.fromDbFunction = fromDbFunction;
  }

  @Override
  public final Y convertToDatabaseColumn(X enumValue) {
    return enumValue != null ? toDbFunction.apply(enumValue) : null;
  }

  @Override
  public final X convertToEntityAttribute(Y dbValue) {
    return dbValue != null ? fromDbFunction.apply(dbValue) : null;
  }
}
