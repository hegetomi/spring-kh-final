package com.hegetomi.orderservice.enums;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class AllowedTypeValidator implements ConstraintValidator<AllowedStatus,Status> {

    private Status[] subset;

    @Override
    public void initialize(AllowedStatus constraint) {
        this.subset = constraint.anyOf();
    }

    @Override
    public boolean isValid(Status value, ConstraintValidatorContext context) {
        return value == null || Arrays.asList(subset).contains(value);
    }
}
