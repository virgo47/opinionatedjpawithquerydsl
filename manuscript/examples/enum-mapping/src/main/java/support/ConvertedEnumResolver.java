package support;

import java.util.HashMap;
import java.util.Map;

/**
 * Helps reverse resolving of {@link ConvertedEnum} from a DB value back to
 * enum instance. Enums that can be resolved this way must have unified
 * interface in order to obtain {@link ConvertedEnum#toDbValue()}.
 *
 * @param <T> type of an enum
 * @param <Y> type of DB value
 */
public class ConvertedEnumResolver<T extends ConvertedEnum<Y>, Y> {

  private final String classCanonicalName;
  private final Map<Y, T> dbValues = new HashMap<>();

  public ConvertedEnumResolver(Class<T> enumClass) {
    classCanonicalName = enumClass.getCanonicalName();
    for (T t : enumClass.getEnumConstants()) {
      dbValues.put(t.toDbValue(), t);
    }
  }

  public T get(Y dbValue) {
    T enumValue = dbValues.get(dbValue);
    if (enumValue == null) {
      throw new IllegalArgumentException("No enum constant for dbValue " +
        dbValue + " in " + classCanonicalName);
    }
    return enumValue;
  }
}
