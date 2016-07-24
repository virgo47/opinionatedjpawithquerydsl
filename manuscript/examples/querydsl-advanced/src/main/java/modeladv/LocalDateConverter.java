package modeladv;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Date;
import java.time.LocalDate;

/**
 * AutoApplied converter, no convert annotation is needed on fields.
 * EclipseLink (and specification) still requires to mention it in persistence.xml.
 */
@Converter(autoApply = true)
public class LocalDateConverter implements AttributeConverter<LocalDate, Date> {

  @Override
  public Date convertToDatabaseColumn(LocalDate entityValue) {
    if (entityValue == null) return null;

    return Date.valueOf(entityValue);
  }

  @Override
  public LocalDate convertToEntityAttribute(Date databaseValue) {
    if (databaseValue == null) return null;

    return databaseValue.toLocalDate();
  }
}
