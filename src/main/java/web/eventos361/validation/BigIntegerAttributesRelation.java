package web.eventos361.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import web.eventos361.validation.util.AttributesRelation;
import web.eventos361.validation.validator.BigIntegerAttributesRelationValidator;

@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BigIntegerAttributesRelationValidator.class)
@Documented
public @interface BigIntegerAttributesRelation {

	String attribute1();

	String attribute2();

	AttributesRelation relation();

	String message() default "";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@interface List {
		BigIntegerAttributesRelation[] value();
	}

}
