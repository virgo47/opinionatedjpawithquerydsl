package modelenum;

import support.ConvertedEnum;
import support.ReverseEnumResolver;

public enum Gender implements ConvertedEnum<Integer> {
  MALE(0, "mal"),
  FEMALE(1, "fem"),
  OTHER(-1, "oth");

  private final Integer dbValue;
  private final String code;

  Gender(Integer dbValue, String code) {
    this.dbValue = dbValue;
    this.code = code;
  }

  @Override
  public Integer toDbValue() {
    return dbValue;
  }

  public String toCode() {
    return code;
  }

  // static resolving:
  public static final ReverseEnumResolver<Gender, Integer> resolver =
    new ReverseEnumResolver<>(Gender.class, Gender::toDbValue);

  public static Gender fromDbValue(Integer dbValue) {
    return resolver.get(dbValue);
  }

  // static resolving to string:
  public static final ReverseEnumResolver<Gender, String> strResolver =
    new ReverseEnumResolver<>(Gender.class, Gender::toCode);

  public static Gender fromCode(String code) {
    return strResolver.get(code);
  }
}