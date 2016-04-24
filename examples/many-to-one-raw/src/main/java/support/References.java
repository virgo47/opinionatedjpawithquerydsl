package support;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates field with foreign key or any method (typically getter, but that is not required)
 * returning such foreign key for an entity, checking whether the object really exists.
 * If null ID is returned reports error if the field is marked with {@link #required()} otherwise
 * ignores it (optional). If ID is not null and object is not found, exception is thrown.
 * Optionally the found entity can be set to designated {@link #targetProperty()} or directly to
 * the {@link #targetField()}.
 * <p/>
 * <b>MUST be used on methods without parameters, otherwise runtime exception will be thrown
 * during check!</b> Usage on fields is preferred, but usage with methods allows to check
 * composite PKs that span multiple fields.
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface References {

  /** Type of the target entity. */
  Class<?> type();

  /** Optional (default false) - if true, id attribute must not be null (that is FK is NOT NULL). */
  boolean required() default false;

  /** When the object is loaded, optionally it can be stored into any property of the annotated bean. */
  String targetProperty() default "";

  /** When the object is loaded, optionally it can be stored into any field of the annotated bean. */
  String targetField() default "";
}
