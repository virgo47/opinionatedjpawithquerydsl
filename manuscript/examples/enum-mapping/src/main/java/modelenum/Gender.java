package modelenum;

import java.util.HashMap;
import java.util.Map;

public enum Gender {
  MALE(0),
  FEMALE(1),
  OTHER(-1);

  private final Integer dbValue;

  Gender(Integer dbValue) {
    this.dbValue = dbValue;
  }

  public Integer getDbValue() {
    return dbValue;
  }

  public static final Map<Integer, Gender> dbValues = new HashMap<>();

  static {
    for (Gender value : values()) {
      dbValues.put(value.dbValue, value);
    }
  }

  public static Gender fromDbValue(Integer dbValue) {
    // this returns null for invalid value,
    // check for null and throw exception if you need it
    return dbValues.get(dbValue);
  }
}