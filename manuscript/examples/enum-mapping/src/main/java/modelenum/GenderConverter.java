package modelenum;

import javax.persistence.AttributeConverter;

public class GenderConverter implements AttributeConverter<Gender, Integer> {
  @Override
  public Integer convertToDatabaseColumn(Gender gender) {
    return gender.getDbValue();
  }

  @Override
  public Gender convertToEntityAttribute(Integer dbValue) {
    // this can still return null unless it throws IllegalArgumentException
    // which would be in line with enums static valueOf method
    return Gender.fromDbValue(dbValue);
  }
}