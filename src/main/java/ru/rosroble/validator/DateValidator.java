package ru.rosroble.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Instant;
import java.util.Date;

public class DateValidator implements ConstraintValidator<ValidDateRange, Date> {
    @Override
    public boolean isValid(Date date, ConstraintValidatorContext constraintValidatorContext) {
        return date.after(Date.from(Instant.parse("1970-01-01T00:00:00.000Z")))
                && date.before(Date.from(Instant.parse("2099-12-31T23:59:59.000Z")));
    }

    @Override
    public void initialize(ValidDateRange constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
