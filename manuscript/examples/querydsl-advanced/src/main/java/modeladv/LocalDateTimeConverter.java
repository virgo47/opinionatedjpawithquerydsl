package modeladv;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * AutoApplied converter, no convert annotation is needed on fields.
 * EclipseLink (and specification) still requires to mention it in persistence.xml.
 */
@Converter(autoApply = true)
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Timestamp> {

  @Override
  public Timestamp convertToDatabaseColumn(LocalDateTime entityValue) {
    return entityValue != null
      ? Timestamp.valueOf(entityValue)
      : null;
  }

  @Override
  public LocalDateTime convertToEntityAttribute(Timestamp databaseValue) {
    return databaseValue != null
      ? databaseValue.toLocalDateTime()
      : null;
  }
}
