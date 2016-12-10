package support;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Helps reverse resolving of enums from any value back to enum instance.
 * Resolver uses provided function that obtains value from enum instance.
 *
 * @param <T> type of an enum
 * @param <Y> type of a value
 */
public final class ReverseEnumResolver<T extends Enum, Y> {
  private final String classCanonicalName;
  private final Map<Y, T> valueMap = new HashMap<>();

  public ReverseEnumResolver(Class<T> enumClass,
    Function<T, Y> toValueFunction)
  {
    classCanonicalName = enumClass.getCanonicalName();
    for (T t : enumClass.getEnumConstants()) {
      valueMap.put(toValueFunction.apply(t), t);
    }
  }

  public T get(Y value) {
    T enumVal = valueMap.get(value);

    if (enumVal == null) {
      throw new IllegalArgumentException("No enum constant for '" +
        value + "' in " + classCanonicalName);
    }
    return enumVal;
  }
}