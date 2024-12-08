package ru.effectivemobile.taskmanagementsystem.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

/**
 * Реализация интерфейса ConstraintValidator для проверки,
 * соответствует ли строковое значение одному из значений указанного перечисления (enum).
 */
public class EnumValidator implements ConstraintValidator<EnumValidation, String> {

    private Class<? extends Enum<?>> enumClass;

    /**
     * Инициализирует валидатор, устанавливая класс перечисления из аннотации.
     *
     * @param constraintAnnotation аннотация EnumValidation, содержащая настройки валидатора
     */
    @Override
    public void initialize(EnumValidation constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClass();
    }

    /**
     * Проверяет, является ли указанное значение допустимым для заданного перечисления.
     *
     * @param value   строковое значение для проверки
     * @param context контекст валидации
     * @return true, если значение допустимо или null, иначе false
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return Arrays.stream(enumClass.getEnumConstants())
                .anyMatch(e -> e.name().equals(value));
    }
}
