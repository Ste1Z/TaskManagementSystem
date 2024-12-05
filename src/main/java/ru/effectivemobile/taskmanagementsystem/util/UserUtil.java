package ru.effectivemobile.taskmanagementsystem.util;

import ru.effectivemobile.taskmanagementsystem.domain.entity.Task;
import ru.effectivemobile.taskmanagementsystem.domain.entity.User;
import ru.effectivemobile.taskmanagementsystem.security.Role;

/**
 * Утилитный класс для проверки ролей и прав доступа текущего пользователя.
 */
public class UserUtil {

    /**
     * Проверяет, является ли текущий пользователь администратором.
     *
     * @param user объект пользователя для проверки
     * @return true, если пользователь имеет роль администратора, иначе false
     */
    public static boolean isCurrentUserAdmin(User user) {
        return user.getRoles().contains(Role.ROLE_ADMIN);
    }

    /**
     * Проверяет, является ли текущий пользователь владельцем указанной задачи.
     *
     * @param user объект пользователя для проверки
     * @param task объект задачи для проверки
     * @return true, если пользователь является автором задачи, иначе false
     */
    public static boolean isCurrentUserOwner(User user, Task task) {
        return user.getUsername().equals(task.getAuthor().getUsername());
    }
}
