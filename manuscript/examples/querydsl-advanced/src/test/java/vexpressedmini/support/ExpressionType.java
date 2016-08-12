package vexpressedmini.support;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

public enum ExpressionType {

  STRING(String.class),

  BOOLEAN(Boolean.class),

  INTEGER(Integer.class),
  DECIMAL(BigDecimal.class),

  DATE(LocalDate.class),
  DATE_TIME(LocalDateTime.class),
  TIMESTAMP(Instant.class),

  /** For any other unspecified type. */
  OBJECT(Object.class);

  /** Value of the type can be converted and should fit into this class type. */
  private Class primaryType;

  ExpressionType(Class primaryType) {
    this.primaryType = primaryType;
  }

  public static ExpressionType fromClass(Class klass) {
    return String.class.isAssignableFrom(klass)
      ? STRING
      : Boolean.class.isAssignableFrom(klass) || klass == boolean.class
      ? BOOLEAN
      // well-known Java types up to Integer range are considered integer
      : Integer.class.isAssignableFrom(klass) || klass == int.class
      || Short.class.isAssignableFrom(klass) || klass == short.class
      || Byte.class.isAssignableFrom(klass) || klass == byte.class
      ? INTEGER
      // any other number class is decimal type, even for integer numbers
      : Number.class.isAssignableFrom(klass) || klass == long.class
      || klass == double.class || klass == float.class
      ? DECIMAL
      : LocalDate.class.isAssignableFrom(klass)
      ? DATE
      : LocalDateTime.class.isAssignableFrom(klass)
      ? DATE_TIME
      : Instant.class.isAssignableFrom(klass)
      ? TIMESTAMP
      // fallback type for anything else
      : OBJECT;
  }

  public Object promote(Object value) {
    if (value == null) {
      return null;
    }

    switch (this) {
      case STRING:
        return value.toString();
      case BOOLEAN:
        return value instanceof Boolean
          ? value
          : false;
      case INTEGER:
        return value instanceof Integer
          ? value
          : new Integer(value.toString());
      case DECIMAL:
        return value instanceof BigDecimal
          ? value
          : new BigDecimal(value.toString());
      case DATE:
        return value instanceof LocalDate
          ? value
          : LocalDate.parse(value.toString());
      case DATE_TIME:
        return value instanceof LocalDateTime
          ? value
          : LocalDateTime.parse(value.toString().replace(' ', 'T'));
      case TIMESTAMP:
        return value instanceof Instant
          ? value
          : value instanceof Number
          ? Instant.ofEpochMilli(Long.parseLong(value.toString()))
          : Instant.parse(value.toString().replace(' ', 'T'));
      case OBJECT:
        return value;
    }
    throw new IllegalArgumentException(
      "Unsupported type " + this + ", cannot convert value " + value);
  }
}
