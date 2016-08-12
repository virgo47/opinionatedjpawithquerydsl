package vexpressedmini.support;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Annotates the method as a function. */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExpressionFunction {

	/** Name of the function - if empty, method name is used. */
	String value() default "";
}
