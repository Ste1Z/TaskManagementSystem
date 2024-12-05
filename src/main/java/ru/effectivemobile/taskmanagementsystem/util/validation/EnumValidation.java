package ru.effectivemobile.taskmanagementsystem.util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import lombok.NonNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для валидации значений перечисления (enum).
 */
@Constraint(validatedBy = EnumValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@NonNull
public @interface EnumValidation {

    /**
     * Указывает класс перечисления, значения которого используются для проверки.
     *
     * @return {@link Class}
     */
    Class<? extends Enum<?>> enumClass();

    /**
     * Сообщение об ошибке, если значение не прошло валидацию.
     *
     * @return {@link String}
     */
    String message() default "Invalid value for enum";

    /**
     * Указывает группы валидации, к которым относится эта аннотация.
     *
     * @return {@link Class[]}
     */
    Class<?>[] groups() default {};

    /**
     * Указывает дополнительную информацию о нагрузке валидации.
     *
     * @return {@link Class[]}
     */
    Class<? extends Payload>[] payload() default {};
}
