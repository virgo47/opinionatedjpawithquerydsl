package vexpressedmini.support;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Parameter;

/**
 * Annotates the method parameter - without parameters it is not necessary, as any method
 * parameter that is not VariableResolver is considered as possible function parameter.
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface FunctionParam {

  /** Name of the parameter - if empty, {@link Parameter#getName()} is used. */
  String name() default "";

  String defaultValue() default "";

  boolean required() default false;
}
