package ru.rosroble.validator;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateValidator.class)
@Documented
public @interface ValidDateRange {
    String message() default "Date is out of the appropriate range.";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}

