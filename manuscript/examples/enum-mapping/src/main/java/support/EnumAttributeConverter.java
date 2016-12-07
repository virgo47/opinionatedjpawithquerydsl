package support;

import javax.persistence.AttributeConverter;

/**
 * Base implementation for converting enums stored in DB.
 * Enums must implement {@link ConvertedEnum}.
 */
public abstract class EnumAttributeConverter<X extends ConvertedEnum<Y>, Y>
  implements AttributeConverter<X, Y>
{
  @Override
  public final Y convertToDatabaseColumn(X enumValue) {
    return enumValue != null ? enumValue.toDbValue() : null;
  }

  @Override
  public final X convertToEntityAttribute(Y dbValue) {
    return dbValue != null ? notNull(fromDbValue(dbValue), dbValue) : null;
  }

  protected abstract X fromDbValue(Y dbValue);

  private X notNull(X x, Y dbValue) {
    if (x == null) {
      throw new IllegalArgumentException("No enum constant" +
        (dbValue != null ? (" for DB value " + dbValue) : ""));
    }
    return x;
  }
}
