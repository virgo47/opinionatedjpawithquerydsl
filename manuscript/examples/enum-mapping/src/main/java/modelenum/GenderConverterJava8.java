package modelenum;

import support.AbstractAttributeConverter;

import javax.persistence.Converter;

@Converter
public class GenderConverterJava8
  extends AbstractAttributeConverter<Gender, Integer>
{
  public GenderConverterJava8() {
    super(Gender::toDbValue, Gender::fromDbValue);
  }
}