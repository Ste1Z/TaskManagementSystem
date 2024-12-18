package ru.effectivemobile.taskmanagementsystem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

/**
 * Конфиг веб-уровня приложения.
 * Включает поддержку Spring Data Web с использованием сериализации страниц через DTO.
 */
@Configuration
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class WebConfig {
}
