package com.hegetomi.orderservice.enums;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = AllowedTypeValidator.class)
@Target({ ElementType.TYPE, ElementType.PARAMETER, ElementType.FIELD })
@Retention(RUNTIME)
public @interface AllowedStatus {
    Status[] anyOf();

    String message() default "error.wrongType";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}