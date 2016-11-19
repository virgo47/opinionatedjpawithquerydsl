package modelenum;

import javax.persistence.AttributeConverter;

/**
 * This is coupled too much to enum and you always have to change both classes in tandem.
 * That's a big STOP (and think) sign in any case.
 */
public class GenderConverter implements AttributeConverter<Gender, Integer> {
  @Override
  public Integer convertToDatabaseColumn(Gender someEntityType) {
    switch (someEntityType) {
      case MALE:
        return 0;
      case FEMALE:
        return 1;
      default:
        // do we need this?  it catches forgotten case when enum is modified
        throw new IllegalArgumentException("Invalid value " + someEntityType);
        // the value is valid, just this externalized switch sucks of course
    }
  }

  @Override
  public Gender convertToEntityAttribute(Integer dbValue) {
    switch (dbValue) {
      case 0:
        return Gender.MALE;
      case 1:
        return Gender.FEMALE;
      case 2:
        return Gender.OTHER;
    }
    // now what? probably exception would be better just to warn programmer
    return null;
  }
}
