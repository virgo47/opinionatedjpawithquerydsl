package modelenum;

import support.EnumAttributeConverter;

import javax.persistence.Converter;

@Converter
public class GenderConverter extends EnumAttributeConverter<Gender, Integer> {
  @Override
  protected Gender fromDbValue(Integer dbValue) {
    return Gender.fromDbValue(dbValue);
  }
}